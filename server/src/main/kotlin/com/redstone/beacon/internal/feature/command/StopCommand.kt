package com.redstone.beacon.internal.feature.command

import com.redstone.beacon.api.permission.LazyPermissionManager
import com.redstone.beacon.api.permission.Permission
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.entity.Player

object StopCommand: Command("stop") {

    init {
        setCondition { sender,_ ->
            if (sender is Player) {
                return@setCondition LazyPermissionManager[sender.uuid]?.hasPermission(Permission("beacon.stop")) ?: false
            }
            if (sender is ConsoleSender) return@setCondition true
            return@setCondition false
        }

        addSyntax({ _: CommandSender, _: CommandContext ->
            MinecraftServer.process().stop()
        })
    }

}