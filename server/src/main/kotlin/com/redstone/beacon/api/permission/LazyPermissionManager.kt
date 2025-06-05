package com.redstone.beacon.api.permission

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Entity
import java.util.*

object LazyPermissionManager: PermissionEntityManager() {
    
    private fun readResolve(): Any = LazyPermissionManager
    override fun removeEntity(entityID: UUID): Boolean {
        if (containsKey(entityID)) {
            return remove(entityID) != null
        }
        return false
    }

    override fun removeEntity(entity: Entity): Boolean {
        return removeEntity(entity.uuid)
    }

    override fun addEntity(entity: Entity): Boolean {
        return addEntity(entity.uuid)
    }

    override fun addEntity(entityID: UUID): Boolean {
        getEntity(entityID) ?: return false
        return get(entityID) != null
    }

    override fun get(key: UUID): PermissionHolder? {
        if (super.get(key) != null) return super.get(key)
        if (hasEntity(key)) {
            return put(key, PermissionHolder())
        }
        return null
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