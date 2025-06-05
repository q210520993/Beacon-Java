package com.redstone.beacon.api.permission

import net.kyori.adventure.nbt.CompoundBinaryTag
import java.util.*


/**
 * Representation of a permission granted to a [CommandSender].
 * Each permission has a string representation used as an identifier, and an optional
 * [CompoundBinaryTag] used to store additional data.
 *
 *
 * The class is immutable.
 */
class Permission
/**
 * Creates a new permission object without additional data
 *
 * @param permissionName the name of the permission
 */
@JvmOverloads constructor(
    /**
     * Gets the name of the permission.
     *
     * @return the permission name
     */
    val permissionName: String,
    /**
     * Gets the data associated to this permission.
     *
     * @return the nbt data of this permission, can be null if not any
     */
    val nbtData: CompoundBinaryTag? = null
) {
    /**
     * Creates a new permission object with optional data.
     *
     * @param permissionName the name of the permission
     * @param nBTData           the optional data of the permission
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Permission
        return permissionName == that.permissionName && nbtData == that.nbtData
    }

    override fun hashCode(): Int {
        return Objects.hash(permissionName, nbtData)
    }
}