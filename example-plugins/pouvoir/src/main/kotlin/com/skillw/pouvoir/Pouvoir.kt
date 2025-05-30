package com.skillw.pouvoir

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import org.slf4j.LoggerFactory

class Pouvoir(pluginWrapper: PluginWrapper) : SubPouvoir, Plugin(pluginWrapper) {
    companion object {
        val logger = LoggerFactory.getLogger(Pouvoir::class.java)!!
    }

    override lateinit var managerData: ManagerData
    override val plugin: Plugin = this
    override val key: String = "Pouvoir"

    override fun onLoad() {
        TotalManager.onServerLoad()
    }

    override fun onEnable() {
        load()
        TotalManager.onEnable()
    }


}