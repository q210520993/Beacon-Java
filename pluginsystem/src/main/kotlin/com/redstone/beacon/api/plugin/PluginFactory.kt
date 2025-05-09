package com.redstone.beacon.api.plugin

interface PluginFactory {
    fun create(pluginWrapper: PluginWrapper): Plugin?
}
