package com.redstone.beacon.api.plugin

open class DependencyResolver {
    open fun resolve(pluginWrapper: PluginWrapper) {
        val pluginClassLoader = pluginWrapper.classLoader
        val descriptor = pluginWrapper.pluginDescriptor
        descriptor.dependencies.forEach {
            if (it is Dependency.PluginDependency) {
                it.getClassLoader()?.let { it1 -> pluginClassLoader.dependenciesClassLoaders.add(it1) } ?: return@forEach
            }
            pluginClassLoader.mavenClassLoader = pluginWrapper.pluginManager.getMavenResolver().getClassLoader(descriptor.name)
        }
    }
}