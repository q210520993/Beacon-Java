package com.redstone.beacon

import com.redstone.beacon.api.plugin.ServerPluginManager
import java.util.UUID

object Beacon {
    // 其实主要是为了permission才配的一个uuid
    lateinit var serverUuid: UUID

    val pluginManager
        get() = ServerPluginManager.pluginManager
}