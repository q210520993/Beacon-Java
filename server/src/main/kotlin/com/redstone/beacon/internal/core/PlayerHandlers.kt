package com.redstone.beacon.internal.core

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent

object PlayerHandlers {
    fun init() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent::class.java) {

        }
    }
}