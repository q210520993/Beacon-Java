package com.skillw.pouvoir.api.plugin

import com.redstone.beacon.api.plugin.Plugin
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.map.component.Registrable

interface SubPouvoir : Registrable<String> {
    var managerData: ManagerData
    val plugin: Plugin


    fun load() {
        managerData.load()
        Pouvoir.logger.info("${plugin.pluginWrapper.pluginDescriptor.name} is loaded")
    }

    fun enable() {
        managerData.enable()
        Pouvoir.logger.info("${plugin.pluginWrapper.pluginDescriptor.name} is enabled")
    }

    fun active() {
        managerData.active()
    }

    fun disable() {
        managerData.disable()
    }

    override fun register() {
        TotalManager.register(this.managerData)
    }

    fun reload() {
        managerData.reload()
    }
}