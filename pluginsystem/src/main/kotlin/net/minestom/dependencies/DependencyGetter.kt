package net.minestom.dependencies

import net.minestom.dependencies.maven.MavenRepository
import net.minestom.dependencies.maven.MavenResolver
import java.io.File
import java.nio.file.Path

class DependencyGetter {

    private val resolverList = mutableListOf<DependencyResolver>()
    val dependencies = mutableMapOf<String, ResolvedDependency>()

    val classLoader by lazy {
        val loader = MavenClassLoader(arrayOf(), this::class.java.classLoader)
        dependencies.forEach { (_, u) ->
            loader.addURL(u.contentsLocation)
        }
        return@lazy loader
    }

    fun addResolver(resolver: DependencyResolver)
        = apply { resolverList += resolver }

    /**
     * Shorthand to add a MavenResolver with the given repositories
     */
    fun addMavenResolver(repositories: List<MavenRepository>) = addResolver(MavenResolver(repositories))

    fun get(ids: List<String>, targetFolder: Path) {
        ids.forEach {
            get(it, targetFolder)
        }
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