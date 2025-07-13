package com.redstone.beacon.internal.feature.listener

object ServerListeners {

    internal fun initListeners() {

        ServerListener.registerListener(PlayerChatPrintConsole)
        ServerListener.registerListener(PlayerJoin)
        ServerListener.registerListener(EntityKilled)

    }

}