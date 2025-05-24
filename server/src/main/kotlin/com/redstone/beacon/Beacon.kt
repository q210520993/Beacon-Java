package com.redstone.beacon

import com.redstone.beacon.internal.core.plugin.ServerPluginManager

object Beacon {
    val plguinManager
        get() = ServerPluginManager.pluginManager
}