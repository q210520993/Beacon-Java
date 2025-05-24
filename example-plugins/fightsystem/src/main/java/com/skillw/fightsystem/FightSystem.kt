package com.skillw.fightsystem

import com.c1ok.vanillafight.Vanilla
import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper
import com.skillw.pouvoir.api.plugin.map.BaseMap

class FightSystem(pluginWrapper: PluginWrapper): Plugin(pluginWrapper) {
    val vanilla by lazy {
        Vanilla()
    }
    override fun onLoad() {
        vanilla.onPreServerStart()
        println(BaseMap<String, String>())
        return
    }

    override fun onEnable() {

    }

}