package net.minestom.dependencies.maven

import net.minestom.dependencies.DependencyResolver
import net.minestom.dependencies.ResolvedDependency
import net.minestom.dependencies.UnresolvedDependencyException
import org.jboss.shrinkwrap.resolver.api.CoordinateParseException
import org.jboss.shrinkwrap.resolver.api.NoResolvedResultException
import org.jboss.shrinkwrap.resolver.api.maven.Maven
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.log

/**
 * Resolves maven dependencies.
 * *Does not use the local maven repository, but uses the folder passed as an argument in resolve(String, File)*
 *
 * Creates a temporary folder `.tmp` inside the target folder to store a Maven `settings.xml` file to specify
 * a local repository and remote repositories.
 *
 * @param repositories list of repositories to use to resolve artifacts
 */
class MavenResolver(val repositories: List<MavenRepository>): DependencyResolver {

    companion object {
        val logger = LoggerFactory.getLogger(MavenResolver::class.java)
    }

    /**
     * Resolves and downloads maven artifacts related to the given id.
     */
    override fun resolve(id: String, targetFolder: Path): ResolvedDependency {
        val tmpFolder = targetFolder.resolve(".tmp")
        try {
            // create a temporary settings file to change the local maven repository and remote repositories
            logger.info("Resolving $id to $targetFolder")
            Files.createDirectories(tmpFolder)
            val settingsFile = tmpFolder.resolve("settings.xml")
            val repoList =
                (repositories.joinToString("") { repo -> """
                <repository>
                    <id>${repo.name}</id>
                    <name>${repo.name}</name>
                    <url>${repo.url.toExternalForm()}</url>
                    <layout>default</layout>
                </repository>
            """ })
            // create the temporary settings.xml file
            Files.writeString(settingsFile, """
                <settings xmlns="http://maven.apache.org/SETTINGS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
                    <localRepository>${targetFolder.toAbsolutePath()}</localRepository>
                    <profiles>
                        <profile>
                            <id>dependency-getter-auto</id>
                            <repositories>
                            $repoList
                            </repositories>
                        </profile>
                    </profiles>
                    <activeProfiles>
                        <activeProfile>dependency-getter-auto</activeProfile>
                    </activeProfiles>
                </settings>
            """.trimIndent())
            // ShrinkWrap Resolver either always uses Central, or never, even if it is in the remote repositories
            val hasMavenCentral = repositories.any { it.url.sameFile(MavenRepository.Central.url) }
            val resolver = Maven.configureResolver().withMavenCentralRepo(hasMavenCentral).fromFile(settingsFile.toFile())
            val artifacts = resolver.resolve(id).withTransitivity().asResolvedArtifact()
            val dependencies = mutableListOf<ResolvedDependency>()
            for(dep in artifacts.drop(1)) { // [0] is the 'root' because we always resolve one artifact at once
                dependencies += convertToDependency(dep)
            }
            val coords = artifacts[0].coordinate
            val dependency = ResolvedDependency(coords.groupId, coords.artifactId, coords.version, artifacts[0].asFile().toURI().toURL(), dependencies)
            logger.info("Resolved $id successfully in $coords")
            return dependency
        } catch(e: CoordinateParseException) {
            throw UnresolvedDependencyException("Failed to resolve $id (not a Maven coordinate)", e)
        } catch(e: NoResolvedResultException) {
            throw UnresolvedDependencyException("Failed to resolve $id", e)
        } finally {
            newDeleteAllFiles(tmpFolder)
        }
    }

    private fun convertToDependency(artifact: MavenResolvedArtifact): ResolvedDependency {
        return ResolvedDependency(artifact.coordinate.groupId, artifact.coordinate.artifactId, artifact.coordinate.version, artifact.asFile().toURI().toURL(), emptyList())
    }

    @Deprecated("并发报错", ReplaceWith(
        "Files.walk(path).sorted(Comparator.reverseOrder()).forEach(Files::delete)",
        "java.nio.file.Files",
        "java.nio.file.Files",
        "java.nio.file.Files.delete"
    )
    )
    private fun deleteAllFiles(path: Path) {
        Files.walk(path).sorted(Comparator.reverseOrder()).forEach(Files::delete) // ensure the temporary settings.xml is actually temporary
    }

    private fun newDeleteAllFiles(path: Path) {
        path.toFile().deleteRecursively()
    }

    override fun toString(): String {
        return "MavenResolver[${repositories.joinToString { "${it.name} ${it.url.toExternalForm()}" }}]"
    }
}
