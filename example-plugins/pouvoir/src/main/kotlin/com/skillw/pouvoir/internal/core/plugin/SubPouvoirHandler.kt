package com.skillw.pouvoir.internal.core.plugin

import com.redstone.beacon.api.plugin.Plugin
import com.redstone.libs.tabooproject.reflex.ClassStructure
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager

object SubPouvoirHandler {
    fun inject(clazz: ClassStructure, plugin: Plugin) {

        val owner = clazz.owner.instance ?: return
        if (SubPouvoir::class.java.isAssignableFrom(owner) && clazz.simpleName != "SubPouvoir")
            TotalManager.pluginData[plugin] = PouManagerUtils.initPouManagers(owner) ?: return
    }
}
