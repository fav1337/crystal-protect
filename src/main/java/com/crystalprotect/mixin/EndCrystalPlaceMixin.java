package com.crystalprotect.mixin;

import com.crystalprotect.CrystalProtectMod;
import com.crystalprotect.config.CrystalProtectConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public class EndCrystalPlaceMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void onPlace(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        if (!CrystalProtectConfig.get().blockPlace) return;
        PlayerEntity player = ctx.getPlayer();
        if (player instanceof ServerPlayerEntity sp && CrystalProtectMod.isProtected(sp)) {
            CrystalProtectMod.sendBlockMessage(sp, "đặt");
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
