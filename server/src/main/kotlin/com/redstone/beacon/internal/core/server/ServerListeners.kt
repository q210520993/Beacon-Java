package com.redstone.beacon.internal.core.server

import com.redstone.beacon.internal.core.server.listeners.player.PlayerChatPrintConsole
import java.util.concurrent.CopyOnWriteArrayList

class ServerListeners private constructor() {
    private val listeners = CopyOnWriteArrayList<ServerListener>()

    companion object {
        val PLAYER_LISTENER = ServerListeners().addListener(PlayerChatPrintConsole)
        // 通过调用直接初始化所有LISTENER
        internal fun initListeners() {
            PLAYER_LISTENER
        }
    }

    fun addListener(listener: ServerListener): ServerListeners {
        listeners.add(listener)
        ServerListener.registerListener(listener)
        return this
    }

    fun removeListener(listener: ServerListener): ServerListeners {
        listeners.remove(listener)
        ServerListener
        return this
    }


}