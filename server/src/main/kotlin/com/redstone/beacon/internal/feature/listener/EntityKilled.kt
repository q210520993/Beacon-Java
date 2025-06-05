package com.redstone.beacon.internal.feature.listener

import com.redstone.beacon.api.permission.LazyPermissionManager
import com.redstone.beacon.internal.core.event.SubscribeEvent
import net.minestom.server.event.entity.EntityDeathEvent
import net.minestom.server.event.entity.EntityDespawnEvent

object EntityKilled: ServerListener {

    @SubscribeEvent
    fun onEntityDeathEvent(event: EntityDeathEvent) {
        if (!LazyPermissionManager.containsKey(event.entity.uuid)) return
        LazyPermissionManager.removeEntity(event.entity)
    }

    @SubscribeEvent
    fun onRemove(event: EntityDespawnEvent) {
        if (!LazyPermissionManager.containsKey(event.entity.uuid)) return
        LazyPermissionManager.removeEntity(event.entity)
    }

}