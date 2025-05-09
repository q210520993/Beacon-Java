package com.redstone.beacon.internal.core.plugin

import com.redstone.beacon.api.event.plugin.PluginDisableEvent
import com.redstone.beacon.api.plugin.DefaultPluginManager
import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginState
import com.redstone.beacon.api.plugin.PluginWrapper
import net.minestom.server.event.EventDispatcher
import org.slf4j.LoggerFactory

class ServerPluginManager: DefaultPluginManager() {

    override fun disablePlugin(name: String): PluginState {
        val plugin = getPlugin(name) ?: return PluginState.FAILED
        EventDispatcher.call(PluginDisableEvent.Pre(plugin))
        plugin.plugin?.onDisable() ?: return PluginState.FAILED
        return PluginState.DISABLE // Implement disable logic
    }

    companion object {
        val pluginManager: ServerPluginManager by lazy {
            ServerPluginManager()
        }

        private val logger = LoggerFactory.getLogger("ServerPluginManager")
    }

}