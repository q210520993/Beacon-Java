package com.redstone.beacon.api.event.plugin

import com.redstone.beacon.api.plugin.PluginWrapper
import net.minestom.server.event.Event

interface PluginEvent: Event {
    val plugin: PluginWrapper
}