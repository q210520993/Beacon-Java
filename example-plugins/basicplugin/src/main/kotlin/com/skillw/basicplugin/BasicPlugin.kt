package com.skillw.basicplugin

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper
import com.redstone.beacon.internal.core.event.EventPriority
import com.redstone.beacon.api.event.plugin.plugin.PluginRegistry.registerCommand
import com.redstone.beacon.api.event.plugin.plugin.PluginRegistry.registerEvent
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.block.Block
import java.util.function.Consumer

class BasicPlugin(pluginWrapper: PluginWrapper) : Plugin(pluginWrapper) {

    val instance by lazy {
        val instancemanager = MinecraftServer.getInstanceManager()
        val instance = instancemanager.createInstanceContainer()
        instance.setGenerator {
            it.modifier().fillHeight(0,40,Block.STONE)
            if (it.absoluteStart().blockY() < 40 && it.absoluteEnd().blockY() > 40) {
                it.modifier().setBlock(
                    it.absoluteStart().blockX(),
                    40,
                    it.absoluteStart().blockZ(),
                    Block.TORCH
                )
            }
        }
        instance.setChunkSupplier { instance: Instance, chunkX: Int, chunkZ: Int ->
            LightingChunk(
                instance,
                chunkX,
                chunkZ
            )
        }
        instance.timeRate = 0
        instance.time = 12000
        return@lazy instance
    }

    override fun onEnable() {
        registerCommand(pluginWrapper, Command("basiccommand"))
        // init
        val eventListener = EventListener.builder<AsyncPlayerConfigurationEvent>(
            AsyncPlayerConfigurationEvent::class.java
        ).handler((Consumer<AsyncPlayerConfigurationEvent> { event: AsyncPlayerConfigurationEvent ->
            val player = event.player
            event.spawningInstance = instance
        })).build()
        registerEvent(
            pluginWrapper, eventListener, EventPriority.NORMAL
        )
    }

}