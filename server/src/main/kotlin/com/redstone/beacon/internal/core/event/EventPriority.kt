package com.redstone.beacon.internal.core.event

import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode

/**
 * 这是一种规范，SubscribeEvent的priority是可以随便设置的，但我觉得遵守规范是最好的选择
 * 值越小表示它的优先级越高代表越早被执行
 * SERVER是只能服务器包内使用的，我觉得它很关键，它处于HIGH与NORMAL的中间态
 *
 * */
enum class EventPriority(val priority: Int) {

    LOWEST(-24),
    LOW(-12),
    NORMAL(0),
    HIGH(12),
    HIGHEST(24),
    MONITOR(48),
    SERVER(6);

    companion object {
        internal fun registerPriorities() {
            for (priority in entries) {
                if (MinecraftServer.getGlobalEventHandler().findChildren(priority.name).isNotEmpty()) {
                    continue
                }
                val node = EventNode.all(priority.name).setPriority(priority.priority)
                MinecraftServer.getGlobalEventHandler().addChild(node)
            }
        }
    }

}