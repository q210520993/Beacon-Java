package com.redstone.beacon.api.permission

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Entity
import java.util.*

object LazyPermissionManager: PermissionEntityManager() {

    private fun readResolve(): Any = LazyPermissionManager
    override fun removeUUID(entityID: UUID): Boolean {
        if (containsKey(entityID)) {
            return remove(entityID) != null
        }
        return false
    }

    override fun removeEntity(entity: Entity): Boolean {
        return removeUUID(entity.uuid)
    }

    override fun addEntity(entity: Entity): Boolean {
        return addUUID(entity.uuid)
    }

    override fun addUUID(entityID: UUID): Boolean {
        putIfAbsent(entityID, PermissionHolder())
        return true
    }

    private fun getEntity(uuid: UUID): Entity? {
        var entity: Entity?
        MinecraftServer.getInstanceManager().instances.forEach { a ->
            a.entities.forEach {
                if (it.uuid == uuid) {
                    entity = it
                    return entity
                }
            }
        }
        return null
    }

    private fun hasEntity(uuid: UUID): Boolean {
        var contain = false
        MinecraftServer.getInstanceManager().instances.forEach {
            if (contain) return true
            contain = it.entities.map { it1 -> it1.uuid }.contains(uuid)
        }
        return contain
    }

}