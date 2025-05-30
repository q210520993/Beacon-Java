package com.redstone.beacon

import com.redstone.beacon.api.plugin.ServerPluginManager

object Beacon {
    val pluginManager
        get() = ServerPluginManager.pluginManager
}