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
    arrayURL: Array<URL>, parent: ClassLoader, val pluginManager: PluginManager, val strategy: List<DependencySource>, val descriptor: Descriptor
): URLClassLoader(arrayURL, parent) {

    val dependenciesClassLoaders = ArrayList<ClassLoader>()
    lateinit var mavenClassLoader: ClassLoader

    companion object {
        private val logger: Logger = LoggerFactory.getLogger("PluginClassLoader")
        const val JAVA_PACKAGE_PREFIX: String = "java."
        const val PLUGIN_PACKAGE_PREFIX: String = "com.redstone.beacon"
    }

    constructor(parent: ClassLoader, pluginManager: PluginManager, descriptor: Descriptor): this(emptyArray(), parent, pluginManager,
        listOf(DependencySource.PLUGIN, DependencySource.MAVEN, DependencySource.DEPENDENCIES, DependencySource.APPLICATION), descriptor
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
            // first check whether it's a system class, delegate to the system loader
            if (className.startsWith(JAVA_PACKAGE_PREFIX)) {
                return findSystemClass(className)
            }

            // if the class is part of the plugin engine use parent class loader
            if (className.startsWith(PLUGIN_PACKAGE_PREFIX)) {
                return parent.loadClass(className)
            }

            logger.trace("Received request to load class '{}'", className)

            // second check whether it's already been loaded
            val loadedClass = findLoadedClass(className)
            if (loadedClass != null) {
                logger.trace("Found loaded class '{}'", className)
                return loadedClass
            }

            for (classLoadingSource in strategy) {
                var c: Class<*>? = null
                try {
                    c = when (classLoadingSource) {
                        DependencySource.MAVEN -> {
                            if (!::mavenClassLoader.isInitialized) continue
                            mavenClassLoader.loadClass(className)
                        }
                        DependencySource.APPLICATION -> {
                            parent.loadClass(className)
                        }
                        DependencySource.PLUGIN -> {

                            findClass(className)
                        }
                        DependencySource.DEPENDENCIES -> loadClassFromDependencies(className)
                    }
                } catch (ignored: ClassNotFoundException) {
                }

                if (c != null) {
                    logger.trace(
                        "Found class '{}' in {} classpath",
                        className,
                        classLoadingSource
                    )
                    return c
                } else {
                    logger.trace(
                        "Couldn't find class '{}' in {} classpath",
                        className,
                        classLoadingSource
                    )
                }
            }

            throw ClassNotFoundException(className)
        }
    }
    
    protected fun loadClassFromDependencies(className: String): Class<*>? {
        logger.trace("Search in dependencies for class '{}'", className)
        dependenciesClassLoaders.forEach {
            it.loadClass(className)
        }

        return null
    }


}