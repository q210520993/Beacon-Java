package com.redstone.beacon.api.plugin

import java.net.URL

interface PluginLoader {
    fun isApplicable(url: URL): Boolean
    fun loadPlugin(descriptor: Descriptor): PluginClassLoader
}