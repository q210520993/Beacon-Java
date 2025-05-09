package com.redstone.beacon.internal.core.plugin

import com.redstone.beacon.api.event.plugin.PluginDisableEvent
import com.redstone.beacon.api.plugin.PluginWrapper
import com.redstone.beacon.utils.safe
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object PluginRegistry {
    private val listeners = ConcurrentHashMap<PluginWrapper, EventNode<Event>>()
    private val commands = ConcurrentHashMap<PluginWrapper, CopyOnWriteArrayList<Command>>()

    @Synchronized
    fun <T: Event> registerEvent(pluginWrapper: PluginWrapper, eventListener: EventListener<T>) {
        safe {
            if (listeners[pluginWrapper] == null) {
                listeners[pluginWrapper] = EventNode.all(pluginWrapper.pluginDescriptor.name)
            }
            listeners[pluginWrapper]!!.addListener(eventListener)
            if (MinecraftServer.getGlobalEventHandler().findChildren(pluginWrapper.pluginDescriptor.name).isEmpty()) {
                MinecraftServer.getGlobalEventHandler().addChild(listeners[pluginWrapper]!!)
            }
            MinecraftServer.getGlobalEventHandler().findChildren(pluginWrapper.pluginDescriptor.name).first().addListener(eventListener)
        }
    }

    @Synchronized
    fun registerCommand(pluginWrapper: PluginWrapper, command: Command) {
        safe {
            if (commands[pluginWrapper] == null) {
                commands[pluginWrapper] = CopyOnWriteArrayList()
            }
            commands[pluginWrapper]!!.add(command)
            MinecraftServer.getCommandManager().register(command)
        }
    }

    internal fun initServerListener() {

        MinecraftServer.getGlobalEventHandler().addListener(PluginDisableEvent.Post::class.java) {
            if (listeners.contains(it.plugin)) {
                MinecraftServer.getGlobalEventHandler().removeChild(listeners[it.plugin]!!)
            }
            if (commands.contains(it.plugin)) {
                commands[it.plugin]!!.forEach { command ->
                    MinecraftServer.getCommandManager().unregister(command)
                }
            }

        }
    }

}