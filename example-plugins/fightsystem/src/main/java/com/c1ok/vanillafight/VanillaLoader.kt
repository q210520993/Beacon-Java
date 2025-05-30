package com.c1ok.vanillafight

import com.redstone.beacon.api.event.plugin.plugin.ServerPluginManager
import com.redstone.beacon.utils.PluginUtils.createIfNotExists

class Vanilla {
    fun onPreServerStart() {
        val plugin = ServerPluginManager.pluginManager.getPlugin("FightSystem")!!.plugin!!
    }
}