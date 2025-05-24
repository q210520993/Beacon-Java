package com.redstone.beacon.api.plugin;

import com.redstone.beacon.utils.SafeKt;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minestom.dependencies.maven.MavenRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractPluginManager implements PluginManager {

    private static final Logger log = LoggerFactory.getLogger(AbstractPluginManager.class);

    private final Path root;

    @NotNull
    @Override
    public Path getRoot() {
        return root;
    }

    @Setter
    @Getter
    private DescriptionFinder pluginDescriptorFinder;
    private final Map<String, PluginWrapper> plugins = Collections.synchronizedMap(new Object2ObjectLinkedOpenHashMap<>());

    @Setter
    private ISorter sorter;

    @Getter
    @Setter
    private VersionChecker versionChecker;

    @Setter
    private MavenResolver mavenResolver;

    @Setter
    @Getter
    private PluginFactory pluginFactory;

    @Setter
    @Getter
    private PluginLoader pluginLoader;

    private final Map<String, ClassLoader> classLoaders = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private DependencyResolver dependencyResolver;

    private final List<String> notSortedPlugins = new ArrayList<>();
    private final Map<String, Descriptor> pluginDescriptors = new HashMap<>();
    private SortResult sortResult;

    public AbstractPluginManager(Path root) {
        this.root = root;
    }

    protected abstract ISorter createSorter();

    protected abstract DescriptionFinder createPluginDescriptorFinder();

    protected abstract MavenResolver createMavenResolver();

    protected abstract VersionChecker createVersionChecker();

    protected abstract PluginFactory createPluginFactory();

    protected abstract PluginLoader createPluginLoader();

    protected abstract DependencyResolver createDependencyResolver();

    @NotNull
    @Override
    public Map<String, PluginWrapper> getPlugins() {
        return plugins;
    }

    @NotNull
    @Override
    public MavenResolver getMavenResolver() {
        return mavenResolver;
    }

    @NotNull
    public ISorter getSorter() {
        if (sorter == null) {
            return new DefaultSorter();
        }
        return sorter;
    }

    @NotNull
    @Override
    public Map<String, ClassLoader> getClassLoaders() {
        return classLoaders;
    }

    public void initialize() {
        try {
            pluginDescriptorFinder = createPluginDescriptorFinder();
            sorter = createSorter();
            mavenResolver = createMavenResolver();
            versionChecker = createVersionChecker();
            pluginFactory = createPluginFactory();
            pluginLoader = createPluginLoader();
            dependencyResolver = createDependencyResolver();
        } catch (Exception ex) {
            log.error("Failed to initialize plugin manager", ex);
        }
    }

    @NotNull
    @Override
    public Map<String, PluginWrapper> loadPlugins() {
        File file = new File(root.toUri());
        if (!file.exists() && !file.mkdirs()) {
            throw new PluginException("Cannot find or create plugins directory, plugins will not be loaded!");
        }
        try {
            initDescriptors(file);
        } catch (Exception ex) {
            log.error("Failed to initialize plugin manager", ex);
        }
        downloadMaven();
        initPluginWrappers();
        try {
            sortResult.getSortedPlugins().forEach(pluginName -> {
                PluginWrapper plugin = plugins.get(pluginName);
                if (plugin != null) {
                    dependencyResolver.resolve(plugin);
                }
            });
        } catch (Exception ex) {
            log.error("Failed to initialize plugin manager", ex);
        }
        initPlugins();
        runLoad();
        return plugins;
    }

    // 这是从外部url导入plugin
    @Override
    public @NotNull PluginState loadPlugin(@NotNull URL url, @NotNull DescriptionFinder descriptionFinder) {
        return Objects.requireNonNull(SafeKt.safe(() -> {
            boolean canUse = descriptionFinder.isApplicable(url);
            if (!canUse) return PluginState.FAILED;
            Descriptor descriptor = descriptionFinder.find(url);
            if (descriptor == null) return PluginState.FAILED;
            pluginDescriptors.put(descriptor.getName(), descriptor);
            PluginWrapper wrapper = createPluginWrapper(descriptor);
            return wrapper.getPluginState();
        }));
    }

    @Override
    public @NotNull PluginState loadPlugin(@NotNull URL url) {
        return loadPlugin(url, pluginDescriptorFinder);
    }

    @Override
    public void enablePlugins() {
        try {
            plugins.forEach((key, value) -> {
                enablePlugin(key);
            });
        }catch (Exception ex) {
            log.error("Failed to enable plugins", ex);
        }
    }

    @NotNull
    @Override
    public PluginState activePlugin(@NotNull String name) {
        try {
            PluginWrapper plugin = getPlugins().get(name);
            if (plugin == null) {
                log.error("Plugin {} not found", name);
                return PluginState.FAILED;
            }
            PluginState state = plugin.getPluginState();
            if (state.getLevel() == 3) {
                log.warn("Plugin {} is already actived", name);
                return PluginState.ACTIVED;
            }
            if (plugin.getPlugin() == null) {
                log.error("Plugin {} not init", name);
                return PluginState.FAILED;
            }
            plugin.getPlugin().onActive();
            plugin.setPluginState(PluginState.ACTIVED);
            return PluginState.ACTIVED;
        }catch (Exception ex) {
            log.error("Failed to active plugin", ex);
            return PluginState.FAILED;
        }
    }

    @Override
    public void activePlugins() {
        plugins.forEach((key, value) -> {
            activePlugin(key);
        });
    }

    @NotNull
    @Override
    public PluginState enablePlugin(@NotNull String name) {
        PluginWrapper plugin = getPlugins().get(name);
        if (plugin == null) {
            log.error("Plugin {} not found", name);
            return PluginState.FAILED;
        }
        PluginState state = plugin.getPluginState();
        if (state == PluginState.STARTED) {
            log.warn("Plugin {} is already enabled", name);
            return PluginState.STARTED;
        }
        Plugin pluginInstance = plugin.getPlugin();
        if (pluginInstance == null) {
            log.error("Plugin {} not initialized", name);
            plugin.setPluginState(PluginState.FAILED);
            return PluginState.FAILED;
        }
        try {
            pluginInstance.onEnable();
        } catch (Exception ex) {
            log.error("Plugin enable failed", ex);
            plugin.setPluginState(PluginState.FAILED);
            return PluginState.FAILED;
        }
        plugin.setPluginState(PluginState.STARTED);
        return PluginState.STARTED;
    }

    @Override
    public void runLoad() {
        try {
            for(PluginWrapper a : plugins.values()) {
                Plugin plugin = a.getPlugin();
                if (plugin == null) {
                    continue;
                }
                plugin.onLoad();
            }
        } catch (Exception ex) {
            log.error("Failed to load", ex);
        }
    }

    @Nullable
    @Override
    public PluginWrapper getPlugin(@NotNull String name) {
        PluginWrapper plugin = getPlugins().get(name);
        if (plugin == null) {
            throw new PluginException("Plugin " + name + " not found");
        }
        return plugin;
    }

    @Override
    public void disablePlugins() {
        // Implement plugin disable logic
        try {
            plugins.forEach((key, value) -> {
                disablePlugin(key);
            });
        }catch ( Exception e) {
            log.error("Failed to disable plugins", e);        plugins.forEach((key, value) -> {
                disablePlugin(key);
            });
        }
    }

    @NotNull
    @Override
    public PluginState disablePlugin(@NotNull String name) {
        PluginWrapper plugin = getPlugin(name);
        // getPlugin never be a null on this function
        if (plugin != null) {
            Objects.requireNonNull(plugin.getPlugin()).onDisable();
            return PluginState.DISABLE; // Implement disable logic
        }
        return PluginState.FAILED;
    }

    @Nullable
    @Override
    public PluginWrapper whichPlugin(@NotNull Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        for (PluginWrapper plugin : plugins.values()) {
            if (plugin.getClassLoader() == classLoader) {
                return plugin;
            }
        }
        return null;
    }

    protected Plugin initPlugin(PluginWrapper pluginWrapper) {
        Plugin plugin = pluginWrapper.getPlugin();
        if (plugin == null) {
            pluginWrapper.setPluginState(PluginState.FAILED);
            if (plugins.containsValue(pluginWrapper)) {
                plugins.remove(pluginWrapper.getPluginDescriptor().getName());
            }
            return null;
        }
        pluginWrapper.setPluginState(PluginState.CREATED);
        log.info("Plugin {} initialized", pluginWrapper.getPluginDescriptor().getName());
        return plugin;
    }

    private void initPlugins() {
        plugins.forEach((name, wrapper) -> {
            if (initPlugin(wrapper) == null) {
                log.error("Failed to initialize plugin: {}", name);
            }
        });
        log.info("all plugins initialized!");
    }


    protected PluginWrapper createPluginWrapper(Descriptor descriptor) {
        try {
            log.debug("Wrapping plugin: {}", descriptor.getName());
            PluginWrapper wrapper = new PluginWrapper(this, descriptor);
            wrapper.setPluginFactory(pluginFactory);
            wrapper.setPluginState(PluginState.CREATED);
            PluginClassLoader classloader = pluginLoader.loadPlugin(descriptor);
            classloader.addURL(descriptor.getUrl());
            wrapper.setClassLoader(classloader);
            classLoaders.put(descriptor.getName(), classloader);
            dependencyResolver.resolve(wrapper);
            wrapper.setPluginState(PluginState.RESOLVED);
            return wrapper;
        }catch (Exception e) {
            log.error("Failed to create plugin wrapper", e);
        }
        return null;
    }

    private void initDescriptors(File file) {
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(v -> {
            try {
                boolean canUse = getPluginDescriptorFinder().isApplicable(v.toURI().toURL());
                if (!canUse) return;
            } catch (MalformedURLException e) {
                log.error("Failed to initialize descriptor", e);
            }
            Descriptor descriptor = null;
            try {
                descriptor = pluginDescriptorFinder.find(v.toURI().toURL());
            } catch (MalformedURLException e) {
                log.error("Failed to find plugin descriptor", e);
            }
            if (descriptor != null) {
                notSortedPlugins.add(descriptor.getName());
            }
            if (descriptor != null) {
                pluginDescriptors.put(descriptor.getName(), descriptor);
            }
        });

        List<Descriptor> sortedDescriptors = notSortedPlugins.stream()
                .map(pluginDescriptors::get)
                .collect(Collectors.toList());

        try {
            sortResult = getSorter().sort(sortedDescriptors);
        } catch (Exception e) {
            log.error("Failed to sort plugins", e);
        }
        log.info("sort finished!");
        log.info("  Sorted plugins: {}", sortResult.getSortedPlugins());
        log.info("  Sorted wrong dependencies plugins: {}", sortResult.getWrongDependencies());
        log.info("  Sorted wrong version plugins: {}", sortResult.getWrongVersion());
    }


    // 准备它们的Wrappers并且放进plugins
    private void initPluginWrappers() {
        // 用于避免重复加载的插件名集合
        Set<String> loadedPlugins = new HashSet<>();
        sortResult.getSortedPlugins().forEach((name) -> {
            PluginWrapper pluginWrapper = createPluginWrapper(pluginDescriptors.get(name));
            if (pluginWrapper != null) {
                plugins.put(name, pluginWrapper);
            }
        });
        // 处理错误依赖的插件：仅加载 unsafe 的
        sortResult.getWrongDependencies().forEach((pluginName, dependencies) -> {
            if (!loadedPlugins.contains(pluginName)) { // 避免重复加载
                Descriptor descriptor = pluginDescriptors.get(pluginName);
                if (descriptor != null && descriptor.getUnsafe()) { // 判断是否是 "unsafe" 插件
                    try {
                        PluginWrapper pluginWrapper = createPluginWrapper(descriptor);
                        if (pluginWrapper != null) {
                            plugins.put(pluginName, pluginWrapper);
                            loadedPlugins.add(pluginName); // 添加到已加载列表
                            log.warn("Loaded unsafe plugin (due to dependency errors): {}", pluginName);
                        }
                    } catch (Exception ex) {
                        log.error("Failed to load unsafe plugin (dependency error): {}", pluginName, ex);
                    }
                }
            }
        });

        // 处理版本错误的插件：仅加载 "unsafe" 的
        sortResult.getWrongVersion().forEach((pluginName, versions) -> {
            if (!loadedPlugins.contains(pluginName)) { // 避免重复加载
                Descriptor descriptor = pluginDescriptors.get(pluginName);
                if (descriptor != null && descriptor.getUnsafe()) { // 判断是否是 "unsafe" 插件
                    try {
                        PluginWrapper pluginWrapper = createPluginWrapper(descriptor);
                        if (pluginWrapper != null) {
                            plugins.put(pluginName, pluginWrapper);
                            loadedPlugins.add(pluginName); // 添加到已加载列表
                            log.warn("Loaded unsafe plugin (due to version errors): {}", pluginName);
                        }
                    } catch (Exception ex) {
                        log.error("Failed to load unsafe plugin (version error): {}", pluginName, ex);
                    }
                }
            }
        });

    }

    protected void downloadMaven() {
        try {
            notSortedPlugins.forEach(v ->{
                Descriptor descriptor = pluginDescriptors.get(v);
                descriptor.getDependencies().stream().filter(value -> value instanceof Dependency.MavenDependency).forEach(mavenDependency -> {
                    List<MavenRepository> res = ((Dependency.MavenDependency) mavenDependency).getRepositories();
                    getMavenResolver().get(v).addMavenResolver(res);
                    getMavenResolver().download(descriptor);
                });
            });
        } catch (Exception ex) {
            log.error("Download Maven caused an error", ex.getCause());
        }
    }

}