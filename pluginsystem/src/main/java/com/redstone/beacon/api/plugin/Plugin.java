package com.redstone.beacon.api.plugin;

import lombok.Getter;

import java.io.File;
import java.nio.file.Path;

@Getter
public class Plugin {

    private PluginWrapper pluginWrapper = null;

    protected Plugin(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;
    }

    protected Plugin() {
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

    public void setPluginWrapper(PluginWrapper pluginWrapper) {
        if (this.pluginWrapper != null) {
            return;
        }
        this.pluginWrapper = pluginWrapper;
    }

    public PluginWrapper getPluginWrapper() {
        return pluginWrapper;
    }
}
