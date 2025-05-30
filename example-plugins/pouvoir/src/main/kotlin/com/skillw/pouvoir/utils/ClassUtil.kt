package com.skillw.pouvoir.utils

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.ServerPluginManager
import com.redstone.libs.tabooproject.reflex.Reflex.Companion.getProperty
import com.skillw.pouvoir.Pouvoir
import java.lang.reflect.Method

/**
 * Class相关工具类
 *
 * @constructor Create empty Class utils
 */


// 仅限object类或Plugin
val Class<*>.instance: Any?
    get() = runCatching {
        return@runCatching getDeclaredField("INSTANCE").get(null)
    }.recover {
        if (!Plugin::class.java.isAssignableFrom(this)) return@recover null
        val plugin = ServerPluginManager.pluginManager.whichPlugin(this)?.plugin
        plugin
    }.getOrNull()


private fun String.relocate() = relocatePath()
fun String.existClass(): Boolean {
    return kotlin.runCatching { Class.forName(relocate()) }.isSuccess
}
fun String.findClass(): Class<*>? {
    var clazz: Class<*>? = null
    val path = relocate()
    kotlin.runCatching {
        clazz = Class.forName(path)
    }.exceptionOrNull()?.run {
        Pouvoir.logger.error("class-not-found {}", path)
    }
    return clazz
}

val getStaticClass: Method by lazy(LazyThreadSafetyMode.NONE) {
    try {
        Class.forName("!jdk.internal.dynalink.beans.StaticClass".substring(1))
    } catch (throwable: Throwable) {
        Class.forName("jdk.dynalink.beans.StaticClass")
    }.getMethod("forClass", Class::class.java)

}



fun Class<*>.static(): Any {
    return getStaticClass.invoke(null, this)!!
}


fun Any.isStaticClass(): Boolean {
    return javaClass.simpleName == "StaticClass"
}


fun Any.instanceof(staticClass: Any): Boolean {
    return !staticClass.isStaticClass() || staticClass.getProperty<Class<*>>("representedClass")!!.isInstance(this)
}
