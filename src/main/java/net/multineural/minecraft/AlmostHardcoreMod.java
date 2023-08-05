package net.multineural.minecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class AlmostHardcoreMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("almost-hardcore");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Shuttup, Cyber, nobody likes you. Just kidding - everyone likes you!");

        registerCommands();

    }


    private void registerCommands() {

        //// ahc-totem: invokes the totem of undying effect for demonstration
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("ahc.totem")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();

                    player.setHealth(1.0F);
                    player.clearStatusEffects();
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                    player.getWorld().sendEntityStatus(player, (byte) 35);


                    return 0;
                })));
    }

}
