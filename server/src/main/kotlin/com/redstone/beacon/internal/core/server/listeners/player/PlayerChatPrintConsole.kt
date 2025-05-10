package com.redstone.beacon.internal.core.server.listeners.player

import com.redstone.beacon.internal.core.event.SubscribeEvent
import com.redstone.beacon.internal.core.server.ServerListener
import net.minestom.server.event.player.PlayerChatEvent
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PlayerChatPrintConsole: ServerListener {


    private val logger = LoggerFactory.getLogger(PlayerChatPrintConsole::class.java)

    // 缓存 DateTimeFormatter
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * 记录玩家聊天内容，包括玩家名字、时间戳、UUID、以及聊天的内容。
     * @param event 玩家聊天事件
     */
    @SubscribeEvent
    fun playerChat(event: PlayerChatEvent) {
        // 获取事件相关的上下文信息
        val playerName = event.player.username           // 玩家名称
        val rawMessage = event.rawMessage               // 聊天的原始消息
        val timestamp = LocalDateTime.now().format(formatter) // 当前时间

        // 构造类似 NMS 风格的日志信息格式
        val logMessage = "[Chat] [$timestamp] $playerName : $rawMessage"

        // 打印日志
        logger.info(logMessage)
    }
}