package com.skillw.fightsystem

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir

class FightSystem(pluginWrapper: PluginWrapper): SubPouvoir, Plugin(pluginWrapper) {

    override lateinit var managerData: ManagerData
    override val plugin: Plugin = this
    override val key: String = "FightSystem"

    override fun onLoad() {

        return
    }

    override fun onEnable() {

    }


}