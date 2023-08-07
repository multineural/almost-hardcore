package net.multineural.minecraft;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.multineural.minecraft.persist.PlayerPojo;
import net.multineural.minecraft.persist.ServerPersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AlmostHardcoreMod implements ModInitializer {

    public static final String PROP_NAME_MOD_ID = "lives-ahc";
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(PROP_NAME_MOD_ID);

    @Override
    public void onInitialize() {

        // requires a log4j.xml in the resources folder
        System.setProperty("log4j.configurationFile", "log4j.xml");

        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Shuttup, Cyber, nobody likes you. Just kidding - everyone likes you!");

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

            if(!serverState.getPlayers().containsKey(localPlayer.getUuid())) {
                serverState.getPlayers().put(localPlayer.getUuid(), new PlayerPojo());
            }

            serverState.markDirty();
        });
    }


    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerTotemCommand(dispatcher);
            registerLivesCommand(dispatcher);
            //registerCHANGEMECommand(dispatcher);
        });
    }


    private void registerTotemCommand(CommandDispatcher<ServerCommandSource> dispatcher) {

        //// ahc.totem: invokes the totem of undying effect for demonstration
        dispatcher.register(literal("livesTotem")
                //.then(argument("amount", IntegerArgumentType.integer()))
                .executes(context -> PlayerEffect.doUndyingEffect(
                        context.getSource().getPlayer(), 1)));
    }


//    private void registerEchoCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
//        dispatcher.register(literal("echo")
//                .requires(source -> source.hasPermissionLevel(2)) // Require op permission (level 2)
//                .then(argument("value", IntegerArgumentType.integer())
//                        .executes(context -> {
//                            ServerPlayerEntity player = context.getSource().getPlayer();
//                            int value = IntegerArgumentType.getInteger(context, "value");
//                            Supplier<Text> message = () -> Text.of("You echoed the integer: " + value);
//                            player.sendMessage(message.get());
//                            return 1;
//                        }))
//        );
//    }


    private void registerLivesCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        //// ahc.lives: queries the number of lives remaining
        dispatcher.register(literal("livesRemaining")
                .executes(context -> {

                    PlayerEntity player = context.getSource().getPlayer();
                    String playerId = player.getUuid().toString();

                    ServerPersistentState serverPersistentState =
                            ServerPersistentState.getOrCreate(context.getSource().getPlayer().getServer());
                    int lives = serverPersistentState.getNumLives(player.getUuid());

                    String text = "lives";
                    if(lives == 1) {
                        text = "life";
                    }
                    player.sendMessage(Text.of(String.format("You have %s %s left ", lives, text)));

                    return 1; // this is the callback status, not the numLives

                }));

    }

}
