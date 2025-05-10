package com.redstone.beacon.api.plugin

import java.nio.file.Path

open class DefaultPluginManager(path: Path = Path.of("plugins")): AbstractPluginManager(path) {

    override fun createSorter(): ISorter {
        return DefaultSorter()
    }

    override fun createPluginDescriptorFinder(): DescriptionFinder {
        return FileDescriptionFinder(this)
    }

    override fun createMavenResolver(): MavenResolver {
        return MavenResolver(Path.of("libs"))
    }

    override fun createVersionChecker(): VersionChecker {
        return DefaultVersionChecker()
    }

    override fun createPluginFactory(): PluginFactory {
        return DefaultPluginFactory()
    }

    override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader().addLoader(DefaultPluginLoader(this))
    }

    override fun createDependencyResolver(): DependencyResolver {
        return DependencyResolver()
    }
}