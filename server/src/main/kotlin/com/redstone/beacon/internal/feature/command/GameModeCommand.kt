package com.redstone.beacon.internal.feature.command

import com.redstone.beacon.api.permission.LazyPermissionManager
import com.redstone.beacon.api.permission.Permission
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.ArgumentEnum
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

object GameModeCommand: Command("gamemode") {
    init {
        setCondition { sender,_ ->
            if (sender is Player) {
                return@setCondition LazyPermissionManager[sender.uuid]?.hasPermission(Permission("beacon.gamemode")) ?: false
            }
            return@setCondition false
        }

        val gamemode = ArgumentType.Enum("gamemode", GameMode::class.java).setFormat(ArgumentEnum.Format.UPPER_CASED)

        defaultExecutor = CommandExecutor { sender: CommandSender, context: CommandContext ->
            val commandName = context.commandName
            sender.sendMessage(Component.text("Tip: /${commandName} <gamemode>", NamedTextColor.RED))
        }

        gamemode.setCallback { commandSender, error ->
            commandSender.sendMessage(Component.text("Invalid gamemode ${error.input}"))
        }

        addSyntax({ sender, context ->
            val gameMode: GameMode = context.get(gamemode)
            (sender as Player).setGameMode(gameMode)
            sender.sendMessage(Component.text("You are ${gameMode}!", NamedTextColor.AQUA))
        }, gamemode)
    }
}