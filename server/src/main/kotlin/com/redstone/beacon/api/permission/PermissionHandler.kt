package com.redstone.beacon.api.permission


/**
 * Represents an object which can have permissions.
 *
 *
 * Permissions are in-memory only by default.
 * You have however the capacity to store them persistently as the [Permission] object
 * is serializer-friendly, [Permission.getPermissionName] being a [String]
 * and [Permission.getNBTData] serializable into a string using [net.kyori.adventure.nbt.TagStringIO].
 */
interface PermissionHandler {
    /**
     * Returns all permissions associated to this handler.
     * The returned collection should be modified only by subclasses.
     *
     * @return the permissions of this handler.
     */
    val allPermissions: MutableSet<Permission>

    /**
     * Adds a [Permission] to this handler.
     *
     * @param permission the permission to add
     */
    fun addPermission(permission: Permission) {
        allPermissions.add(permission)
    }

    /**
     * Removes a [Permission] from this handler.
     *
     * @param permission the permission to remove
     */
    fun removePermission(permission: Permission) {
        allPermissions.remove(permission)
    }

    /**
     * Removes a [Permission] based on its string identifier.
     *
     * @param permissionName the permission name
     */
    fun removePermission(permissionName: String) {
        allPermissions.removeIf { permission: Permission -> permission.permissionName == permissionName }
    }

    /**
     * Gets if this handler has the permission `permission`.
     * This method will also pattern match for wildcards. For example, if this handler has the permission `"*"`, this method will always return true.
     * However, if this handler has the permission `"foo.b*r.baz"`, this method will return true if `permission` is `"foo.baaar.baz"` or `"foo.br.baz`, but not `"foo.bar.bz"`.
     *
     *
     * Uses [Permission.equals] internally.
     *
     * @param permission the permission to check
     * @return true if the handler has the permission, false otherwise
     */
    fun hasPermission(permission: Permission): Boolean {
        for (permissionLoop in allPermissions) {
            if (permissionLoop.equals(permission)) {
                return true
            }
            val permissionLoopName = permissionLoop.permissionName
            if (permissionLoopName.contains("*")) {
                // Sanitize permissionLoopName
                val regexSanitized: String =
                    java.util.regex.Pattern.quote(permissionLoopName).replace("*", "\\E(.*)\\Q") // Replace * with regex
                // pattern matching for wildcards, where foo.b*r.baz matches foo.baaaar.baz or foo.bar.baz
                if (permission.permissionName.matches(regexSanitized.toRegex())) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Gets the [Permission] with the name `permissionName`.
     *
     *
     * Useful if you want to retrieve the permission data.
     *
     * @param permissionName the permission name
     * @return the permission from its name, null if not found
     */
    fun getPermission(permissionName: String): Permission? {
        for (permission in allPermissions) {
            // Verify permission name equality
            if (permission.permissionName == permissionName) {
                return permission
            }
        }
        return null
    }

    /**
     * Gets if this handler has the permission with the name `permissionName` and which verify the optional
     * [PermissionVerifier].
     *
     * @param permissionName     the permission name
     * @param permissionVerifier the optional verifier,
     * null means that only the permission name will be used
     * @return true if the handler has the permission, false otherwise
     */
    /**
     * Gets if this handler has the permission with the name `permissionName`.
     *
     * @param permissionName the permission name
     * @return true if the handler has the permission, false otherwise
     */
    fun hasPermission(permissionName: String, permissionVerifier: PermissionVerifier? = null): Boolean {
        var permission = getPermission(permissionName)

        if (permission == null && permissionVerifier == null) {
            permission = Permission(permissionName, null)
        } else if (permission == null) {
            return false
        }
        // If no permission verifier, hand off to no-verifier hasPermission for wildcard support
        if (permissionVerifier == null) {
            return hasPermission(permission)
        }
        // Verify using the permission verifier
        return permissionVerifier.isValid(permission.nbtData)
    }
}