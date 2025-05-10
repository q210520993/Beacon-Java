package com.skillw.fightsystem;

import com.redstone.beacon.api.plugin.Plugin;
import com.redstone.beacon.api.plugin.PluginWrapper;
import com.redstone.beacon.internal.core.event.EventPriority;
import com.redstone.beacon.internal.core.plugin.PluginRegistry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public class PluginMain extends Plugin {
    public PluginMain(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void onEnable() {
        PluginRegistry.INSTANCE.registerCommand(getPluginWrapper(), new net.minestom.server.command.builder.Command("1"));
        InstanceContainer instanceContainer = getInstanceContainer();
        EventListener<AsyncPlayerConfigurationEvent> eventListener = EventListener.builder(AsyncPlayerConfigurationEvent.class).handler((event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
        })).build();
        PluginRegistry.INSTANCE.registerEvent(getPluginWrapper(), eventListener, EventPriority.NORMAL);
    }

    private static @NotNull InstanceContainer getInstanceContainer() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40, Block.STONE);

            if (unit.absoluteStart().blockY() < 40 && unit.absoluteEnd().blockY() > 40) {
                unit.modifier().setBlock(unit.absoluteStart().blockX(), 40, unit.absoluteStart().blockZ(), Block.TORCH);
            }
        });
        instanceContainer.setChunkSupplier(LightingChunk::new);
        instanceContainer.setTimeRate(0);
        instanceContainer.setTime(12000);
        return instanceContainer;
    }

}
