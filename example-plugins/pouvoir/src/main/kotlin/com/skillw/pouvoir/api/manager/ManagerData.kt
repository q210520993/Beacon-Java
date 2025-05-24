package com.skillw.pouvoir.api.manager

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.utils.safe
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.map.SingleExecMap
import com.skillw.pouvoir.api.map.component.Registrable
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.core.plugin.PouManagerUtils.getPouManagers
import java.util.*
import java.util.concurrent.CompletableFuture

class ManagerData(val subPouvoir: SubPouvoir) : KeyMap<String, Manager>(), Registrable<SubPouvoir> {
    private val managers = ArrayList<Manager>()
    val plugin: Plugin = subPouvoir.plugin
    override val key: SubPouvoir = subPouvoir

    override fun put(key: String, value: Manager): Manager? {
        managers.add(value)
        managers.sort()
        return super.put(key, value)
    }

    init {
        for (manager in subPouvoir.getPouManagers()) {
            this.register(manager)
        }
        val dataField = subPouvoir.javaClass.getField("managerData")
        dataField.set(subPouvoir, this)
    }

    override fun register() {
        TotalManager.register(subPouvoir, this)
    }

    fun load() {
        managers.forEach {
            safe(it::onLoad)
        }
    }

    fun enable() {
        managers.forEach {
            safe(it::onEnable)
        }
    }

    fun active() {
        managers.forEach {
            safe(it::onActive)
        }
    }

    private var onReload = SingleExecMap()
    fun reload() {
        CompletableFuture.supplyAsync {
            managers.forEach {
                safe(it::onReload)
            }
            onReload.values.forEach { it() }
        }
    }

    fun onReload(key: String = UUID.randomUUID().toString(), exec: () -> Unit) {
        onReload[key] = exec
    }

    fun disable() {
        managers.forEach {
            safe(it::onDisable)
        }
    }

}