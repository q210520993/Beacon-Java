package com.redstone.beacon.api.plugin;

import lombok.Getter;

@Getter
public class Plugin {

    private final PluginWrapper pluginWrapper;

    protected Plugin(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;
    }

    public void onEnable() {}

    public void mixin() {}

    public void onActive() {}

    public void onDisable() {}

    public PluginWrapper getPluginWrapper() {
        return pluginWrapper;
    }
}
