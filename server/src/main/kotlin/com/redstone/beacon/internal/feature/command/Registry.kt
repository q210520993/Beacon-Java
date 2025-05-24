package com.redstone.beacon.internal.feature.command

import net.minestom.server.MinecraftServer

object CommandRegistry {
    fun register() {
        MinecraftServer.getCommandManager().register(StopCommand)
    }
}