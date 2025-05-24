package com.redstone.beacon.api.plugin

import com.redstone.beacon.utils.safe
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*

//strategy为加载顺序
open class PluginClassLoader(
    arrayURL: Array<URL>, parent: ClassLoader, val strategy: List<DependencySource>, val descriptor: Descriptor
): URLClassLoader(arrayURL, parent) {

    val dependenciesClassLoaders = ArrayList<ClassLoader>()
    lateinit var mavenClassLoader: ClassLoader

    companion object {
        private val logger: Logger = LoggerFactory.getLogger("PluginClassLoader")
        const val JAVA_PACKAGE_PREFIX: String = "java."
        const val PLUGIN_PACKAGE_PREFIX: String = "com.redstone.beacon"
    }

    constructor(parent: ClassLoader, descriptor: Descriptor): this(
        emptyArray(),
        parent,
        listOf(DependencySource.PLUGIN, DependencySource.MAVEN, DependencySource.DEPENDENCIES, DependencySource.APPLICATION),
        descriptor
    )

    fun addFile(file: File) {
        safe {
            addURL(file.canonicalFile.toURI().toURL())
        }
    }

    public override fun addURL(url: URL) {
        super.addURL(url)
    }

    @Throws(ClassNotFoundException::class)
    override fun loadClass(className: String): Class<*> {
        synchronized(getClassLoadingLock(className)) {

            if (className.startsWith(JAVA_PACKAGE_PREFIX)) {
                return findSystemClass(className)
            }


            if (className.startsWith(PLUGIN_PACKAGE_PREFIX) && !className.startsWith("com.redstone.beacon") && !className.startsWith(
                    "com.redstone.test"
                )
            ) {
                return parent.loadClass(className)
            }

            // 其他类的加载逻辑
            val loadedClass = findLoadedClass(className)
            if (loadedClass != null) {
                return loadedClass
            }

            for (classLoadingSource in strategy) {
                var c: Class<*>? = null
                try {
                    c = when (classLoadingSource) {
                        DependencySource.MAVEN -> {
                            if (!::mavenClassLoader.isInitialized) continue
                            try {
                                mavenClassLoader.loadClass(className)
                            } catch (e: Exception) {
                                continue
                            }
                        }
                        DependencySource.APPLICATION -> {
                            for (isolation in descriptor.isolations) {
                                if (className.startsWith(isolation)) {
                                    continue
                                }
                            }
                            super.loadClass(className)
                        }
                        DependencySource.PLUGIN -> findClass(className)
                        DependencySource.DEPENDENCIES -> {
                            loadClassFromDependencies(className)
                        }
                    }
                } catch (ignored: ClassNotFoundException) {
                }

                if (c != null) {
                    return c
                }
            }

            throw ClassNotFoundException("Failed to load class: '$className'")
        }
    }
    
    private fun loadClassFromDependencies(className: String): Class<*>? {
        logger.debug("Search in dependencies for class '{}'", className)
        dependenciesClassLoaders.forEach {
            val c = it.loadClass(className)
            if (c != null) {
                return c
            }
        }
        logger.debug("Cannot search in dependencies for class '{}'", className)
        return null
    }


}