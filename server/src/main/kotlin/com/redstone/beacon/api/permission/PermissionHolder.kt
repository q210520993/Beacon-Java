package com.redstone.beacon.api.permission

import java.util.concurrent.CopyOnWriteArraySet

class PermissionHolder: PermissionHandler {
    override val allPermissions = CopyOnWriteArraySet<Permission>()
}