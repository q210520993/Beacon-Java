package com.redstone.beacon.api.plugin

class PluginWrapper(
    val pluginManager: PluginManager, val pluginDescriptor: Descriptor
) {

    lateinit var pluginFactory: PluginFactory

    lateinit var classLoader: PluginClassLoader

    lateinit var pluginState: PluginState

    val plugin: Plugin? by lazy {
        return@lazy pluginFactory.create(this)
    }

}