package com.skillw.fightsystem

import com.c1ok.vanillafight.Vanilla
import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper

class FightSystem(pluginWrapper: PluginWrapper): Plugin(pluginWrapper) {
    val vanilla by lazy {
        Vanilla()
    }
    override fun onLoad() {
        vanilla.onPreServerStart()
        println(123)
        return
    }

    override fun onEnable() {

    }

}