package com.skillw.pouvoir.internal.core.event

import com.redstone.beacon.Beacon
import com.redstone.beacon.api.plugin.PluginWrapper
import com.redstone.beacon.internal.core.event.EventPriority
import com.redstone.libs.tabooproject.reflex.ClassStructure
import com.skillw.pouvoir.api.plugin.ClassHandler
import com.skillw.pouvoir.api.plugin.annotation.ClassHandled
import com.skillw.pouvoir.utils.instance
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener

object SubscribeEventHandler: ClassHandler(1) {
    override fun handle(clazz: ClassStructure) {
        if (clazz.isInterface || clazz.isAbstract || !clazz.isAnnotationPresent(ClassHandled::class.java)) return
        val pluginClazz = clazz.owner.instance ?: return
        val plugin = Beacon.pluginManager.whichPlugin(pluginClazz) ?: return
        val obj = clazz.owner.instance?.instance ?: return
        clazz.methods.forEach {
            val an = it.getAnnotationIfPresent(SubscribeEvent::class.java) ?: return@forEach
            val ignoreCancellable = an.property("ignoreCancelled", true)
            val priority = an.property("priority", EventPriority.NORMAL)
            @Suppress("UNCHECKED_CAST")
            val eventClass = (it.parameterTypes[0] as? Class<Event>) ?: throw IllegalArgumentException("${it.name} 参数不正确")
            val listener = EventListener.builder(eventClass).
                ignoreCancelled(ignoreCancellable).
                handler { clazz ->
                    it.invoke(obj, clazz)
                }.
            build()
            datas.add(EventData(plugin, Pair(listener, priority)))
        }
    }

    internal data class EventData(val plugin: PluginWrapper, val listeners: Pair<EventListener<*>, EventPriority>)

    internal val datas = mutableListOf<EventData>()

}