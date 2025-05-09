package com.redstone.beacon.api.plugin

interface PluginDependencyFinder {

    fun findDependency(map: Map<String, Any?>): Dependency?

}