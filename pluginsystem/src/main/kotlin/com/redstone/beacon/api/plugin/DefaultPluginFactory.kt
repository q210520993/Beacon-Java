package com.redstone.beacon.api.plugin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

open class DefaultPluginFactory: PluginFactory {
    companion object {
        private val log: Logger = LoggerFactory.getLogger("DefaultPluginFactory")
    }
    override fun create(pluginWrapper: PluginWrapper): Plugin? {
        val pluginClassName: String = pluginWrapper.pluginDescriptor.main
        log.debug("Creating Plugin Instance: ${pluginClassName}, pluginName: ${pluginWrapper.pluginDescriptor.name} ")
        var pluginClazz: Class<*>? = null
        try {
            pluginClazz = pluginWrapper.classLoader.loadClass(pluginClassName)
        } catch (e: ClassNotFoundException) {
            log.error(e.message, e)
            return null
        }
        val modifier = pluginClazz.modifiers
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            log.error("The plugin class '{}' is not valid", pluginClassName)
            return null
        }

        return createInstance(pluginClazz, pluginWrapper)
    }

    protected fun createInstance(clazz: Class<*>, pluginWrapper: PluginWrapper): Plugin? {
        try {
            val constructor: Constructor<*> = clazz.getConstructor(PluginWrapper::class.java)
            return (constructor.newInstance(pluginWrapper) as Plugin)
        } catch (e: NoSuchMethodException) {
            log.error(e.message, e)
            e.printStackTrace()
        } catch (e: Exception) {
            log.error(e.message, e)
        }

        return null
    }

}