package net.minestom.dependencies

import net.minestom.dependencies.maven.MavenRepository
import net.minestom.dependencies.maven.MavenResolver
import org.apache.maven.model.Parent
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Path

open class DependencyGetter {

    private val resolverList = mutableListOf<DependencyResolver>()
    val dependencies = mutableMapOf<String, ResolvedDependency>()

    companion object {
        var parentClassLoader: ClassLoader = this::class.java.classLoader
    }

    open val classLoader: URLClassLoader by lazy {
        val loader = MavenClassLoader(arrayOf(), parentClassLoader)
        dependencies.forEach { (_, u) ->
            loader.addURL(u.contentsLocation)
        }
        return@lazy loader
    }

    fun addResolver(resolver: DependencyResolver)
        = apply {
            resolverList += resolver
        }
    fun addDependency(resolver: ResolvedDependency) {
        dependencies[resolver.name] = resolver
    }

    /**
     * Shorthand to add a MavenResolver with the given repositories
     */
    fun addMavenResolver(repositories: List<MavenRepository>) = addResolver(MavenResolver(repositories))

    fun get(ids: List<String>, targetFolder: Path): List<ResolvedDependency> {
        val list = ArrayList<ResolvedDependency>()
        ids.forEach {
            val resolver = get(it, targetFolder)
            list.add(resolver)
        }
        return list
    }

    fun get(id: String, targetFolder: Path, isSaveInThisClass: Boolean = true): ResolvedDependency {
        resolverList.forEach { resolver ->
            try {
                val dependency = resolver.resolve(id, targetFolder)
                if (isSaveInThisClass) {
                    dependencies[id] = dependency
                }
                return dependency
            } catch (e: UnresolvedDependencyException) {
                // silence and go to next resolver
            }
        }

        throw UnresolvedDependencyException("Could not find $id inside resolver list: ${resolverList.joinToString { it.toString() }}")
    }
}