package com.redstone.beacon.internal.core

import com.redstone.beacon.utils.FileUtils
import org.slf4j.LoggerFactory
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import java.io.File

internal object ServerInfo {
    lateinit var minestomData: MinestomData
    private val logger= LoggerFactory.getLogger(ServerInfo::class.java)

    internal fun displayServerInfo() {
        // 输出网络信息
        logger.info("Network Information:")
        logger.info("   IP: {}", minestomData.network.ip)
        logger.info("   Port: {}", minestomData.network.port)
        logger.info("   Open to LAN: {}", minestomData.network.openToLan)

        // 输出代理信息
        logger.info("Proxy Information:")
        logger.info("   Enabled: {}", minestomData.proxy.enable)
        logger.info("   Type: {}", minestomData.proxy.type)
        logger.info("   Secret: {}", minestomData.proxy.secret)

        // 输出服务器信息
        logger.info("Server Information:")
        logger.info("   Ticks Per Second: {}", minestomData.server.ticksPerSecond)
        logger.info("   Chunk View Distance: {}", minestomData.server.chunkViewDistance)
        logger.info("   Entity View Distance: {}", minestomData.server.entityViewDistance)
        logger.info("   Online Mode: {}", minestomData.server.onlineMode)
        logger.info("   Terminal: {}", minestomData.server.terminal)
        logger.info("   Benchmark: {}", minestomData.server.benchmark)
    }

    internal fun setData() {
        val file: File = File("beacon.conf")
        if (!file.exists()) {
            FileUtils.extractResource("beacon.conf")
        }
        val config = Configuration.loadFromFile(file)
        var network = MinestomData.Network()
        if(config.getConfigurationSection("network") != null) {
            network = MinestomData.Network(config.getConfigurationSection("network")!!)
        }
        var proxy = MinestomData.Proxy()
        if(config.getConfigurationSection("proxy") != null) {
            proxy = MinestomData.Proxy(config.getConfigurationSection("proxy")!!)
        }
        var server = MinestomData.Server()
        if(config.getConfigurationSection("server") != null) {
            server = MinestomData.Server(config.getConfigurationSection("server")!!)
        }
        minestomData = MinestomData(network, proxy, server)
    }

}

data class MinestomData(val network: Network, val proxy: Proxy, val server: Server) {
    class Network() {
        var ip = "127.0.0.1"
            private set
        var port = 25565
            private set
        var openToLan = false
            private set

        constructor(configuration: ConfigurationSection): this() {
            ip = configuration.getString("ip", "127.0.0.1")!!
            port = configuration.getInt("port", 25565)
            openToLan = configuration.getBoolean("openToLan", false)
        }
    }

    class Proxy() {
        var enable = false
            private set
        var type = "velocity"
            private set
        var secret = ""
            private set

        constructor(configuration: ConfigurationSection): this() {
            enable = configuration.getBoolean("enable", false)
            type = configuration.getString("type", "velocity")!!
            secret = configuration.getString("secret", "")!!
        }
    }

    class Server() {

        var ticksPerSecond: Int = 20
            private set

        var chunkViewDistance: Int = 8
            private set

        var entityViewDistance: Int = 6
            private set

        var onlineMode: Boolean = true
            private set


        var terminal: Boolean = false
            private set

        var benchmark: Boolean = false
            private set

        constructor(configuration: ConfigurationSection): this() {
            ticksPerSecond = configuration.getInt("ticksPerSecond", 20).coerceIn(1, 128)
            chunkViewDistance = configuration.getInt("chunkViewDistance", 8).coerceIn(2, 32)
            entityViewDistance = configuration.getInt("entityViewDistance", 6).coerceIn(2, 32)
            onlineMode = configuration.getBoolean("onlineMode", true)
            terminal = configuration.getBoolean("terminal", false)
            benchmark = configuration.getBoolean("benchmark", false)
        }

    }



}