package net.multineural.minecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.multineural.minecraft.commands.EchoCommand;
import net.multineural.minecraft.commands.LivesCommand;
import net.multineural.minecraft.commands.TotemCommand;
import net.multineural.minecraft.persist.PlayerPojo;
import net.multineural.minecraft.persist.ServerPersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AlmostHardcoreMod implements ModInitializer {

    public static final String PROP_NAME_MOD_ID = "lives-ahc";

    public static final Logger LOGGER = LoggerFactory.getLogger(PROP_NAME_MOD_ID);

    @Override
    public void onInitialize() {

        // requires a log4j.xml in the resources folder
        // System.setProperty("log4j.configurationFile", "log4j.xml");

        LOGGER.info("Shuttup, Cyber, nobody likes you. Just kidding - everyone loves you!");

        registerPlayersJoining();
        registerCommands();

    }


    private void registerPlayersJoining() {

        LOGGER.debug(">>>> registerPlayersJoining method...");

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

            ServerPlayerEntity localPlayer = handler.player;

            // You can see we use the function getServer() that's on the player.
            ServerPersistentState serverState =
                    ServerPersistentState.getOrCreate(localPlayer.getWorld().getServer());

            if (!serverState.getPlayers().containsKey(localPlayer.getUuid())) {
                serverState.getPlayers().put(localPlayer.getUuid(), new PlayerPojo());
            }

            serverState.markDirty();
        });
    }


    private void registerCommands() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LivesCommand.register(dispatcher);
            TotemCommand.register(dispatcher);
            EchoCommand.register(dispatcher);
        });

    }

}
