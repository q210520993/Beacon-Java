package com.redstone.beacon.api.plugin

import com.github.zafarkhaja.semver.Version
import net.minestom.dependencies.maven.MavenRepository

sealed class Dependency {

    data class PluginDependency(
        val pluginManager: PluginManager,
        val pluginId: String,
        val version: Version,
        val versionLimitType: VersionCheckType,
        val optional: Boolean = true
    ) : Dependency() {
        fun getClassLoader(): ClassLoader? {
            return pluginManager.getClassLoaders()[pluginId]
        }
    }

    // 支持多仓库和依赖项的 Maven 依赖
    data class MavenDependency(
        val pluginManager: PluginManager,
        val repositories: List<MavenRepository> = emptyList(),
        val artifacts: List<String> = emptyList(),
        val resolverName: String
    ) : Dependency()
}

