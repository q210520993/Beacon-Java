package com.redstone.beacon.api.plugin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

class CompoundPluginLoader: PluginLoader {

    private val loaders = ArrayList<PluginLoader>()

    override fun isApplicable(url: URL): Boolean {
        for (loader in loaders) {
            if (loader.isApplicable(url)) {
                return true
            }
        }

        return false
    }

    override fun loadPlugin(descriptor: Descriptor): PluginClassLoader {
        for (loader in loaders) {
            if (loader.isApplicable(descriptor.origin)) {
                logger.debug("'{}' is applicable for plugin '{}'", loader, descriptor.origin)
                try {
                    val classLoader = loader.loadPlugin(descriptor)
                    return classLoader
                } catch (e: Exception) {
                    // log the exception and continue with the next loader
                    logger.error(e.message) // ?!
                }
            } else {
                logger.debug("'{}' is not applicable for plugin '{}'", loader, descriptor.origin)
            }
        }

        throw RuntimeException("No PluginLoader for plugin ${descriptor.name}")
    }

    fun addLoader(loader: PluginLoader): CompoundPluginLoader {
        loaders.add(loader)
        return this
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger("CompoundPluginLoader")
    }

}