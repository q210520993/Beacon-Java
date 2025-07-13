package com.redstone.beacon.api.plugin

import com.redstone.beacon.api.event.plugin.PluginDisableEvent
import com.redstone.beacon.internal.core.event.EventPriority
import com.redstone.beacon.utils.safe
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList


/**
 * @author c1ok
 * 插件注册表，负责管理插件的指令(Command)和事件监听器(EventListener)。
 * 支持对插件生命周期进行事件监听和资源清理。
 */
object PluginRegistry {
    // 存储每个插件与其对应指令的映射关系
    private val commands = ConcurrentHashMap<PluginWrapper, MutableList<Command>>()

    // 存储每个插件与其对应事件监听器的映射关系
    private val listeners = ConcurrentHashMap<PluginWrapper, MutableList<EventListener<*>>>()
    private val nodes = ConcurrentHashMap<PluginWrapper, MutableList<EventNode<out Event>>>()

    /**
     * 注册事件事件树到指定插件
     * @param pluginWrapper 所属插件的包装器
     * @param eventNode 要注册的事件监听器
     */
    @Synchronized
    fun registerNode(
        pluginWrapper: PluginWrapper,
        eventNode: EventNode<out Event>,
    ) {
        safe {
            nodes.computeIfAbsent(pluginWrapper) { CopyOnWriteArrayList() }.add(eventNode)
            MinecraftServer.getGlobalEventHandler().addChild(eventNode)
        }
    }

    /**
     * 卸载事件树到指定插件
     * @param pluginWrapper 所属插件的包装器
     * @param eventNode 要注册的事件监听器
     */
    @Synchronized
    fun unregisterNode(
        pluginWrapper: PluginWrapper,
        eventNode: EventNode<out Event>,
    ) {
        safe {
            if (!nodes.containsKey(pluginWrapper)) return@safe
            if (!nodes[pluginWrapper]!!.contains(eventNode)) return@safe
            nodes[pluginWrapper]?.remove(eventNode)
            MinecraftServer.getGlobalEventHandler().removeChild(eventNode)
        }
    }

    /**
     * 注册事件监听器到指定插件。
     * @param pluginWrapper 所属插件的包装器
     * @param eventListener 要注册的事件监听器
     * @param eventPriority 事件监听器的优先级，默认为 NORMAL
     */
    @Synchronized
    fun <T: Event> registerEvent(
        pluginWrapper: PluginWrapper,
        eventListener: EventListener<T>,
        eventPriority: EventPriority = EventPriority.NORMAL,
    ) {
        safe {
            listeners.computeIfAbsent(pluginWrapper) { CopyOnWriteArrayList() }.add(eventListener)
            MinecraftServer.getGlobalEventHandler()
                .findChildren(eventPriority.name)
                .first()
                .addListener(eventListener)
        }
    }

    /**
     * 注销单个事件监听器。
     * @param eventListener 需要注销的事件监听器
     */
    @Synchronized
    fun <T: Event> unregisterEvent(eventListener: EventListener<T>) {
        listeners.forEach { (pluginWrapper, eventList) ->
            if (unregisterEvent(pluginWrapper, eventListener)) return
        }
    }

    /**
     * 从指定插件中注销监听器。
     * @param plugin 插件包装器
     * @param eventListener 要注销的事件监听器
     * @return 返回是否成功注销该监听器
     */
    @Synchronized
    fun <T: Event> unregisterEvent(plugin: PluginWrapper, eventListener: EventListener<T>): Boolean {
        listeners[plugin]?.let { eventList ->
            if (eventList.remove(eventListener)) {
                MinecraftServer.getGlobalEventHandler().removeListener(eventListener)
                return true
            }
        }
        return false
    }

    /**
     * 注册指令到插件内部。
     * @param pluginWrapper 所属插件的包装器
     * @param command 要注册的指令对象
     */
    @Synchronized
    fun registerCommand(pluginWrapper: PluginWrapper, command: Command) {
        safe {
            commands.computeIfAbsent(pluginWrapper) { CopyOnWriteArrayList() }.add(command)
            MinecraftServer.getCommandManager().register(command)
        }
    }

    /**
     * 清理插件注册的所有资源，包括事件监听器和指令。
     * @param pluginWrapper 所属插件的包装器
     */
    @Synchronized
    fun unregister(pluginWrapper: PluginWrapper) {
        // 注销监听器
        listeners[pluginWrapper]?.forEach { listener ->
            MinecraftServer.getGlobalEventHandler().removeListener(listener)
        }
        listeners.remove(pluginWrapper)

        // 注销命令
        commands[pluginWrapper]?.forEach { command ->
            MinecraftServer.getCommandManager().unregister(command)
        }
        commands.remove(pluginWrapper)

        nodes[pluginWrapper]?.forEach {
            MinecraftServer.getGlobalEventHandler().removeChild(it)
        }
        nodes.remove(pluginWrapper)
    }

    /**
     * 初始化服务器监听器，用于监听插件卸载事件以进行资源清理。
     */
    internal fun initPluginListener() {
        MinecraftServer.getGlobalEventHandler().addListener(PluginDisableEvent.Post::class.java) { event ->
            unregister(event.plugin)
        }
    }
}