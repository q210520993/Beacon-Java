package com.redstone.beacon.internal.core.plugin

import com.redstone.beacon.api.plugin.Plugin


interface PluginListener {
    companion object {
        fun registerListener(plugin: Plugin) {}
    }
}