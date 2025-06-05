package com.redstone.beacon.api.permission

import net.kyori.adventure.nbt.CompoundBinaryTag


/**Add commentMore actions
 * Interface used to check if the [nbt data][CompoundBinaryTag] of a [Permission] is correct.
 */
fun interface PermissionVerifier {
    /**
     * Called when using [PermissionHandler.hasPermission].
     *
     * @param nbtCompound the data of the permission, can be null if not any
     * @return true if [PermissionHandler.hasPermission]
     * should return true, false otherwise
     */
    fun isValid(nbtCompound: CompoundBinaryTag?): Boolean
}