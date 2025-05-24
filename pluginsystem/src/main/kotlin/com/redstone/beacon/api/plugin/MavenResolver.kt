package com.redstone.beacon.api.plugin

import com.redstone.beacon.utils.safe
import net.minestom.dependencies.DependencyGetter
import net.minestom.dependencies.ResolvedDependency
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

open class MavenResolver(
    root: Path,
): ConcurrentHashMap<String, DependencyGetter>() {

    private val targetPath = root
    private val caches = ConcurrentHashMap<String, ResolvedDependency>()

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
                    it.artifacts.forEach com@{ art->
                        if (caches.containsKey(art)) {
                            val dependency = caches[art]!!
                            getter.addDependency(dependency)
                            return@com
                        }
                        val depend = this[descriptor.name].get(art, targetPath)
                        caches[art] = depend
                    }
                }
            }
        }
    }

    fun getClassLoader(name: String): ClassLoader {
        return this[name].classLoader
    }

}