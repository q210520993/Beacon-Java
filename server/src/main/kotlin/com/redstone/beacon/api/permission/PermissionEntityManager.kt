package com.redstone.beacon.api.permission

import net.minestom.server.entity.Entity
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

abstract class PermissionEntityManager: ConcurrentHashMap<UUID, PermissionHolder>() {
    abstract fun removeUUID(entityID: UUID): Boolean
    abstract fun removeEntity(entity: Entity): Boolean
    abstract fun addUUID(entityID: UUID): Boolean
    abstract fun addEntity(entity: Entity): Boolean
}