package com.redstone.beacon.api.plugin

import com.github.zafarkhaja.semver.Version
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import java.io.File
import java.net.URL
import java.util.jar.JarFile

class FileDescriptionFinder(private val manager: PluginManager): DescriptionFinder {

    override fun find(pluginURL: URL): Descriptor {
        val file = File(pluginURL.toURI())
        return getPluginDescription(file)
    }

    private fun getPluginDescription(file: File): Descriptor {
        val jar = JarFile(file)
        val entry = jar.getJarEntry("plugin.yml") ?: jar.getJarEntry("plugin.conf")
        val stream = jar.getInputStream(entry)
        val config = Configuration.loadFromInputStream(
            stream,
            Configuration.getTypeFromExtension(entry.name.replace("plugin.", ""))
        )

        return object : Descriptor {
            override val name: String = config.getString("name") ?: throw PluginException("Missing plugin name")
            override val main: String = config.getString("main") ?: throw PluginException("Missing main class")
            override val version: Version = Version.parse(config.getString("version") ?: throw PluginException("Missing version"))
            override val dependencies: List<Dependency> = parseDependencies(config.getConfigurationSection("dependencies"), name)
            override val url: URL = file.toURI().toURL()
            override val origin: URL = file.toURI().toURL()
        }
    }

    private fun parseDependencies(dependenciesSection: ConfigurationSection?, pluginName: String): List<Dependency> {
        if (dependenciesSection == null) return emptyList()

        return buildList {
            dependenciesSection.getConfigurationSection("maven")?.let { mavenBlock ->
                // 解析 Maven 仓库列表
                val repositories = mavenBlock.getMapList("repositories").map { repoMap ->
                    Dependency.Companion.MavenRepository(
                        name = repoMap["name"]?.toString() ?: throw PluginException("Missing repository name"),
                        url = repoMap["url"]?.toString() ?: throw PluginException("Missing repository url")
                    )
                }

                // 创建 MavenDependency 对象
                add(
                    Dependency.MavenDependency(
                        manager,
                        repositories = repositories,
                        artifacts = mavenBlock.getStringList("artifacts"),
                        pluginName
                    )
                )
            }

            // 解析插件依赖
            dependenciesSection.getConfigurationSection("plugins")?.let { plugins ->
                fun parsePluginList(key: String, isOptional: Boolean) = plugins.getMapList(key).map { map ->
                    Dependency.PluginDependency(
                        manager,
                        pluginId = map["name"]?.toString() ?: throw PluginException("Missing plugin name"),
                        version = Version.parse(map["version"]?.toString() ?: throw PluginException("Missing version")),
                        versionLimitType = when (map["versionLimit"]?.toString()?.lowercase()) {
                            "up","UP","Up","uP" -> VersionCheckType.UP
                            "down","DOWN","Down" -> VersionCheckType.DOWN
                            else -> throw PluginException("Invalid versionLimit: ${map["versionLimit"]}")
                        },
                        optional = isOptional
                    )
                }

                addAll(parsePluginList("depend", isOptional = false)) // 硬依赖
                addAll(parsePluginList("softDepend", isOptional = true)) // 软依赖
            }
        }
    }

    override fun isApplicable(pluginPath: URL): Boolean {
        return pluginPath.protocol == "file" || pluginPath.protocol == "file" || pluginPath.protocol == "jar"
    }

}