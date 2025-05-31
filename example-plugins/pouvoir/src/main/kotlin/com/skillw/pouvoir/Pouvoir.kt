package com.skillw.pouvoir

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.api.plugin.PluginWrapper
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.sub.AwakeManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.api.plugin.annotation.ClassHandled
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import com.skillw.pouvoir.internal.core.awake.Awake
import com.skillw.pouvoir.internal.core.awake.AwakeType
import org.slf4j.LoggerFactory

@ClassHandled
class Pouvoir(pluginWrapper: PluginWrapper) : SubPouvoir, Plugin(pluginWrapper) {

    companion object {
        val logger = LoggerFactory.getLogger(Pouvoir::class.java)!!
    }

    override lateinit var managerData: ManagerData
    override val plugin: Plugin = this
    override val key: String = "Pouvoir"

    override fun onLoad() {
        TotalManager.onServerLoad()
        load()
    }

    override fun onEnable() {
        enable()
        TotalManager.onEnable()
    }

    override fun onDisable() {
        disable()
    }

    override fun onActive() {
        active()
    }

    @PouManager("com.skillw.pouvoir.internal.manager.AwakeManagerImpl")
    lateinit var awakeManager: AwakeManager


}