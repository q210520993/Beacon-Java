package com.redstone.beacon.api.plugin

import java.net.URL
import java.nio.file.Path

interface PluginManager {

    fun getRoot(): Path

    fun runLoad()

    fun getPlugin(name: String): PluginWrapper?

    fun getPlugins(): Map<String, PluginWrapper>

    fun whichPlugin(clazz: Class<*>): PluginWrapper?

    fun getClassLoaders(): Map<String, ClassLoader>

    fun loadPlugins(): Map<String, PluginWrapper>

    fun loadPlugin(url: URL): PluginState

    fun loadPlugin(url: URL, descriptionFinder: DescriptionFinder): PluginState

    fun activePlugin(name: String): PluginState

    fun activePlugins()

    fun disablePlugin(name: String): PluginState

    fun disablePlugins()

    fun enablePlugin(name: String): PluginState

    fun enablePlugins()

    fun getMavenResolver(): MavenResolver

}