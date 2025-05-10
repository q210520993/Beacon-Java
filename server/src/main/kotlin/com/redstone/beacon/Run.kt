package com.redstone.beacon

import com.redstone.beacon.internal.core.MinestomData
import com.redstone.beacon.internal.core.ServerInfo
import com.redstone.beacon.internal.core.ServerInfo.minestomData
import com.redstone.beacon.internal.core.event.EventPriority
import com.redstone.beacon.internal.core.plugin.PluginRegistry
import com.redstone.beacon.internal.core.plugin.ServerPluginManager
import com.redstone.beacon.internal.core.server.ServerListener
import com.redstone.beacon.internal.core.server.ServerListeners
import com.redstone.beacon.internal.core.terminal.EasyTerminal
import com.redstone.beacon.utils.safe
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.lan.OpenToLAN
import net.minestom.server.extras.velocity.VelocityProxy
import net.minestom.server.utils.time.TimeUnit
import java.time.Duration

fun main() {
    EasyTerminal.start()
    val pluginmanaer = ServerPluginManager.pluginManager
    pluginmanaer.initialize()
    pluginmanaer.loadPlugins()

    ServerInfo.setData()
    ServerInfo.displayServerInfo()
    val minestomData = ServerInfo.minestomData
    val networkData: MinestomData.Network = minestomData.network
    val proxyData: MinestomData.Proxy = minestomData.proxy
    val serverData: MinestomData.Server = minestomData.server

    val process = MinecraftServer.init()
    // 监听器注册
    EventPriority.registerPriorities()
    ServerListeners.initListeners()

    proxyHandle(proxyData, networkData)
    runBenchMark(serverData)

    PluginRegistry.initServerListener()
    safe { pluginmanaer.enablePlugins() }
    process.start(networkData.ip, networkData.port)


    MinecraftServer.getSchedulerManager().buildShutdownTask {
        EasyTerminal.stop()
    }
}

private fun runBenchMark(serverData: MinestomData.Server) {
    if (serverData.benchmark) {
        MinecraftServer.getBenchmarkManager().enable(Duration.of(10, TimeUnit.SECOND))
    }
}

private fun proxyHandle(proxyData: MinestomData.Proxy, networkData: MinestomData.Network) {
    if (networkData.openToLan) {
        OpenToLAN.open()
    }
    if (proxyData.enable) {
        val proxyType: kotlin.String = proxyData.type

        if (proxyType.equals("velocity", ignoreCase = true)) {
            VelocityProxy.enable(proxyData.secret)
        } else if (proxyType.equals("bungeecord", ignoreCase = true)) {
            BungeeCordProxy.enable()
        }
    }
}

private fun setProperty() {
    //设置服务器每秒的tps
    System.setProperty("minestom.tps",
        minestomData.server.ticksPerSecond.toString()
    )

    System.setProperty("minestom.chunk-view-distance",
        minestomData.server.chunkViewDistance.toString()
    )

    System.setProperty("minestom.entity-view-distance",
        minestomData.server.entityViewDistance.toString()
    )
}