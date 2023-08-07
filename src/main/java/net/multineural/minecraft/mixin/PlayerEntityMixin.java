package net.multineural.minecraft.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.multineural.minecraft.AlmostHardcoreMod;
import net.multineural.minecraft.PlayerEffect;
import net.multineural.minecraft.persist.ServerPersistentState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "onDeath", cancellable = true)
    private void mixinOnDeath(DamageSource damageSource, CallbackInfo ci) {

        AlmostHardcoreMod.LOGGER.debug("according to mixin, mixinOnDeath called...");

        ServerPlayerEntity playerEntity = (ServerPlayerEntity) (Object) this;

        ServerPersistentState serverPersistentState =
                ServerPersistentState.getOrCreate(playerEntity.getServer());

        int livesRemaining = serverPersistentState.getNumLives(playerEntity.getUuid());

        AlmostHardcoreMod.LOGGER.debug("according to mixin, livesRemaining= " + livesRemaining);
        AlmostHardcoreMod.LOGGER.debug("according to mixin, localEntity.getHealth()= " + playerEntity.getHealth());

        if (livesRemaining > 0) {
            PlayerEffect.doUndyingEffect(playerEntity, 1);
            ci.cancel();
        }

    }


}
