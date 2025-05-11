package com.redstone.beacon.api.plugin

import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

open class DefaultPluginLoader : PluginLoader {

    override fun isApplicable(url: URL): Boolean {
        val path = Path.of(url.toURI())
        return Files.exists(path) && Files.isRegularFile(path) && path.toString().lowercase(Locale.getDefault()).endsWith(".jar");
    }

    override fun loadPlugin(descriptor: Descriptor): PluginClassLoader {
        val pluginClassLoader = PluginClassLoader(this::class.java.classLoader, descriptor)
        pluginClassLoader.addURL(descriptor.url)
        return pluginClassLoader
    }

}