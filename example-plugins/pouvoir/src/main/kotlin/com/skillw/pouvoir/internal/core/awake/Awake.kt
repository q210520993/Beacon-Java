package com.skillw.pouvoir.internal.core.awake

import com.redstone.beacon.Beacon
import com.redstone.libs.tabooproject.reflex.ClassStructure
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.ClassHandler
import com.skillw.pouvoir.api.plugin.annotation.ClassHandled
import com.skillw.pouvoir.utils.instance
import com.skillw.pouvoir.utils.run
import java.lang.reflect.Method

class AwakeMethod(
    val method: Method,
    val src: Any
) {

    var isExec: Boolean = false
        private set

    fun exec() {
        method.run(src)
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Awake(val type: AwakeType)

object AwakeClassHandler: ClassHandler(1) {
    override fun handle(clazz: ClassStructure) {
        if (clazz.isInterface || clazz.isAbstract || !clazz.isAnnotationPresent(ClassHandled::class.java)) return
        val src = clazz.owner.instance?.instance ?: return
        val javaClazz = clazz.owner.instance
        val methods = javaClazz?.methods ?: return
        methods.filter{
            it.isAnnotationPresent(Awake::class.java)
        }.forEach {
            val anno = it.getAnnotation(Awake::class.java) ?: return@forEach
            val enum = anno.type
            val pouvoir = (Beacon.pluginManager.getPlugin("Pouvoir")?.plugin as? Pouvoir) ?: return
            val awakeMethod = AwakeMethod(it, src)
            pouvoir.awakeManager[enum]!!.add(awakeMethod)
        }
    }
}