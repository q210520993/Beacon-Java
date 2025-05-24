package com.redstone.beacon.internal.feature.command

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.condition.Conditions

object StopCommand: Command("stop") {

    init {
        setCondition(Conditions::consoleOnly)
        addSyntax({ _: CommandSender, _: CommandContext ->
            MinecraftServer.process().stop()
        })
    }
}