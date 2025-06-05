package com.redstone.beacon.internal.feature.listener

import java.util.concurrent.CopyOnWriteArrayList

class ServerListeners private constructor() {
    private val listeners = CopyOnWriteArrayList<ServerListener>()

    companion object {
        val LISTENER = ServerListeners().addListener(PlayerChatPrintConsole, EntityKilled)
        // 通过调用直接初始化所有LISTENER
        internal fun initListeners() {
            LISTENER
        }
    }

    fun addListener(vararg listener: ServerListener): ServerListeners {
        listener.forEach {
            listeners.add(it)
            ServerListener.registerListener(it)
        }
        return this
    }

    fun removeListener(listener: ServerListener): ServerListeners {
        listeners.remove(listener)
        ServerListener
        return this
    }


}