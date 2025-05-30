package com.redstone.beacon.internal.core.server

import com.redstone.beacon.internal.core.event.EventPriority
import com.redstone.beacon.internal.core.event.SubscribeEvent
import com.redstone.beacon.utils.run
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener


interface ServerListener {

    companion object {
        internal fun registerListener(pluginListener: ServerListener) {
            val clazz = pluginListener::class.java
            clazz.methods.filter { it.isAnnotationPresent(SubscribeEvent::class.java) }.forEach {
                // 验证参数类型
                if (!Event::class.java.isAssignableFrom(it.parameterTypes[0])) {
                    throw IllegalArgumentException("${it.name} 参数不正确")
                }
                // 获取注解值
                val annotation = it.getAnnotation(SubscribeEvent::class.java)
                val ignoreCancelled = annotation.ignoreCancelled

                // 安全地将参数类型转换为 Event 类
                @Suppress("UNCHECKED_CAST")
                val eventClass = (it.parameterTypes[0] as? Class<Event>) ?: throw IllegalArgumentException("${it.name} 参数不正确")
                // 构建事件监听器
                val listener = EventListener.builder(eventClass).
                    ignoreCancelled(ignoreCancelled).
                    handler { clazz ->
                        it.run(clazz)
                    }.
                build()
                MinecraftServer.getGlobalEventHandler().findChildren(EventPriority.SERVER.name).first().addListener(listener)
            }
        }
    }
}