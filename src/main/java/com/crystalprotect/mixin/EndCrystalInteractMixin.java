package com.crystalprotect.mixin;

import com.crystalprotect.CrystalProtectMod;
import com.crystalprotect.config.CrystalProtectConfig;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public class EndCrystalInteractMixin {

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!CrystalProtectConfig.get().blockInteract) return;
        if (player instanceof ServerPlayerEntity sp && CrystalProtectMod.isProtected(sp)) {
            CrystalProtectMod.sendBlockMessage(sp, "tương tác");
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
