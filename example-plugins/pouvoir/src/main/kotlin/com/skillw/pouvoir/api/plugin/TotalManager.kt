package com.skillw.pouvoir.api.plugin

import com.redstone.beacon.Beacon
import com.redstone.beacon.api.plugin.Dependency
import com.redstone.beacon.api.plugin.Plugin
import com.redstone.beacon.utils.FileUtils.getPluginDependencies
import com.redstone.libs.tabooproject.reflex.ClassStructure
import com.redstone.libs.tabooproject.reflex.ReflexClass
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.map.component.Registrable
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.internal.core.plugin.SubPouvoirHandler
import com.skillw.pouvoir.utils.existClass
import com.skillw.pouvoir.utils.instance
import com.skillw.pouvoir.utils.safe
import java.util.concurrent.ConcurrentHashMap

internal object TotalManager: KeyMap<SubPouvoir, ManagerData>() {
    private fun readResolve(): Any = TotalManager
    internal val pluginData = ConcurrentHashMap<Plugin, SubPouvoir>()
    private val allClasses = HashSet<ClassStructure>()

    fun onServerLoad() {
        val postLoads = ArrayList<() -> Unit>()
        Beacon.plguinManager.plugins.filter {
            isDependPouvoir(plugin = it.value.plugin)
        }.values.sortedWith { p1, p2 ->
            if (p1.plugin!!.isDepend(p2.plugin!!)) 1 else -1
        }.forEach {
            safe {
                loadSubPou(it.plugin!!, postLoads)
            }
        }
        postLoads.forEach { safe(it) }
        postLoads.clear()
    }

    fun loadSubPou(plugin: Plugin, postLoads: ArrayList<() -> Unit>)  {
        if (!isDependPouvoir(plugin)) return

        val classes = PluginUtils.getClasses(plugin::class.java).map {
            ReflexClass.of(it).structure
        }
        allClasses.addAll(classes)

        classes.forEach classFor@{ clazz ->
            //优先加载Managers
            safe { SubPouvoirHandler.inject(clazz, plugin) }
        }

        pluginData[plugin]?.let {
            ManagerData(it).register()
        }
        classes.filter { clazz ->
            clazz.isAnnotationPresent(AutoRegister::class.java)
        }.forEach { clazz ->
            kotlin.runCatching {
                val auto = clazz.getAnnotation(AutoRegister::class.java)
                val postLoad = auto.property<Boolean>("postLoad") ?: false
                val test = auto.property<String>("test") ?: ""
                if ((test.isEmpty() || test.existClass()))
                    if (postLoad) {
                        postLoads.add {
                            (clazz.owner.instance?.instance as? Registrable<*>?)?.register()
                        }
                    } else (clazz.owner.instance?.instance as? Registrable<*>?)?.register()
            }.exceptionOrNull()?.printStackTrace()
        }

    }

    private fun Plugin.isDepend(other: Plugin) =
        getPluginDependencies<Dependency.PluginDependency>().all {
            it.pluginId == other.pluginWrapper.pluginDescriptor.name
        }

    private fun isDependPouvoir(plugin: Plugin?): Boolean {
        val depends = plugin?.pluginWrapper?.pluginDescriptor?.dependencies?.filterIsInstance<Dependency.PluginDependency>() ?: return false
        return depends.any { it.pluginId == "Pouvoir" } || plugin.pluginWrapper.pluginDescriptor.name == "Pouvoir"
    }
}