package net.multineural.minecraft;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.multineural.minecraft.persist.ServerPersistentState;

import java.util.UUID;

public class PlayerEffect {

    public static int doUndyingEffect(PlayerEntity player, int numLivesToSubtract) {

        player.setHealth(1.0F);
        player.canModifyBlocks();
        player.clearStatusEffects();
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
        player.getWorld().sendEntityStatus(player, (byte) 35);

        subtractLives(player, numLivesToSubtract);

        return 1;
    }


    private static void subtractLives(PlayerEntity player, int toSubtract) {

        ServerPersistentState state = ServerPersistentState.getOrCreate(player.getServer());
        UUID playerId = player.getUuid();
        int current = state.getNumLives(playerId);
        state.setNumLives(playerId, current - toSubtract);

    }


}
