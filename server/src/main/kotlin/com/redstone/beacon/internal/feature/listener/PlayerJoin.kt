package com.redstone.beacon.internal.feature.listener

import com.redstone.beacon.api.permission.LazyPermissionManager
import com.redstone.beacon.internal.core.event.SubscribeEvent
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

object PlayerJoin: ServerListener {
    @SubscribeEvent
    fun onPlayerJoin(event: AsyncPlayerConfigurationEvent) {
        LazyPermissionManager.addEntity(event.entity)
    }
}