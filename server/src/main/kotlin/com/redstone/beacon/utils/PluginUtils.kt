package com.redstone.beacon.utils

import com.redstone.beacon.api.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URL

// some bukkit and pouvoir functions
object PluginUtils {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Create if not exists
     * @author Glom_
     * @param name
     * @param fileNames
     */
    fun Plugin.create(name: String, vararg fileNames: String) {
        val path = dataFolder.path
        val dir = File("$path/$name")
        dir.mkdir()
        for (fileName in fileNames) {
            safe { saveResource("$name/$fileName", true) }
        }
    }

    /**
     * Create if not exists
     *
     * @param name
     * @param fileNames
     */
    fun Plugin.createIfNotExists(name: String, vararg fileNames: String) {
        val path = dataFolder.path
        val dir = File("$path/$name")
        if (!dir.exists()) {
            dir.mkdir()
            for (fileName in fileNames) {
                safe { saveResource("$name/$fileName", true) }
            }
        }
    }

    fun Plugin.saveResource(resourcePath: String?, replace: Boolean) {
        var resourcePath = resourcePath
        require(!(resourcePath == null || resourcePath == "")) { "ResourcePath cannot be null or empty" }

        resourcePath = resourcePath.replace('\\', '/')
        val `in`: InputStream = getResource(resourcePath)
            ?: throw IllegalArgumentException("The embedded resource '$resourcePath' cannot be found in ${pluginWrapper.pluginDescriptor.name}")

        val outFile: File = File(dataFolder, resourcePath)
        val lastIndex = resourcePath.lastIndexOf('/')
        val outDir: File = File(dataFolder, resourcePath.substring(0, if (lastIndex >= 0) lastIndex else 0))

        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        try {
            if (!outFile.exists() || replace) {
                val out: OutputStream = FileOutputStream(outFile)
                val buf = ByteArray(1024)
                var len: Int
                while ((`in`.read(buf).also { len = it }) > 0) {
                    out.write(buf, 0, len)
                }
                out.close()
                `in`.close()
            } else {
                logger.warn(
                    "Could not save " + outFile.name + " to " + outFile + " because " + outFile.name + " already exists."
                )
            }
        } catch (ex: IOException) {
            logger.error( "Could not save " + outFile.name + " to " + outFile, ex)
        }
    }


    @JvmStatic
    fun Plugin.getResource(name: String): InputStream? {
        try {
            val url: URL = this.pluginWrapper.classLoader.getResource(name) ?: return null

            val connection = url.openConnection()
            connection.useCaches = false
            return connection.getInputStream()
        } catch (ex: IOException) {
            return null
        }
    }
}