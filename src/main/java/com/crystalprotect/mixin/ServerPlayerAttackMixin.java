package com.crystalprotect.mixin;

import com.crystalprotect.CrystalProtectMod;
import com.crystalprotect.config.CrystalProtectConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerAttackMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        if (!CrystalProtectConfig.get().blockAttack) return;
        if (!(target instanceof EndCrystalEntity)) return;

        ServerPlayerEntity self = (ServerPlayerEntity)(Object) this;
        if (CrystalProtectMod.isProtected(self)) {
            CrystalProtectMod.sendBlockMessage(self, "đập");
            ci.cancel();
        }
    }
}
