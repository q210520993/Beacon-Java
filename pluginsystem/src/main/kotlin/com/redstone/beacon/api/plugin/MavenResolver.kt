package com.redstone.beacon.api.plugin

import com.redstone.beacon.utils.safe
import net.minestom.dependencies.DependencyGetter
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

open class MavenResolver(
    private val root: Path,
): ConcurrentHashMap<String, DependencyGetter>() {

    private val targetPath = root

    override fun get(key: String): DependencyGetter {

        val value = super.get(key)
        if (value != null) return value
        val getter = DependencyGetter()
        set(key, getter)
        return getter
    }

    // 下载
    fun download(descriptor: Descriptor) {

        safe {
            descriptor.dependencies.forEach {
                if (it is Dependency.MavenDependency) {
                    val getter = this[descriptor.name]
                    getter.addMavenResolver(it.repositories)
                    this[descriptor.name].get(it.artifacts, targetPath)
                }
            }
        }
    }

    fun getClassLoader(name: String): ClassLoader {
        return this[name].classLoader
    }

}