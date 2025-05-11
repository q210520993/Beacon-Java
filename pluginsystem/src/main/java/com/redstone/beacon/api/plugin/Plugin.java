package com.redstone.beacon.api.plugin;

import lombok.Getter;

import java.io.File;
import java.nio.file.Path;

@Getter
public class Plugin {

    private final PluginWrapper pluginWrapper;

    protected Plugin(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;
    }

    public void onEnable() {}

    public void onLoad() {}

    public void onActive() {}

    public void onDisable() {}

    public File getDataFolder() {
        // 创建两个路径
        Path path = pluginWrapper.getPluginManager().getRoot().resolve(Path.of(pluginWrapper.getPluginDescriptor().getName()));
        return path.toFile();
    }

    public PluginWrapper getPluginWrapper() {
        return pluginWrapper;
    }
}
