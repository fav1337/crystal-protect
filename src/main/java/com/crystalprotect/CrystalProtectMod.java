package com.crystalprotect;

import com.crystalprotect.config.CrystalProtectConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrystalProtectMod implements ModInitializer {

    public static final String MOD_ID = "crystalprotect";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // UUID -> ticks còn lại
    public static final Map<UUID, Integer> PROTECTED_PLAYERS = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("[CrystalProtect] Khởi động mod v1.0.0 cho 1.21.4");

        // Load config
        CrystalProtectConfig.load();

        // Sự kiện player chết -> cấp bảo vệ cho kẻ giết
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(entity instanceof ServerPlayerEntity dead)) return;
            if (source.getAttacker() instanceof ServerPlayerEntity killer) {
                grantProtection(killer);
            }
        });

        // Đếm ngược tick
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PROTECTED_PLAYERS.replaceAll((uuid, ticks) -> ticks - 1);
            PROTECTED_PLAYERS.entrySet().removeIf(entry -> {
                if (entry.getValue() <= 0) {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
                    if (player != null) {
                        sendActionBar(player,
                            "✅ Bảo vệ Crystal đã hết! Crystal mở khoá.",
                            Formatting.GREEN);
                    }
                    return true;
                }
                return false;
            });
        });

        // Xoá khi disconnect
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
            PROTECTED_PLAYERS.remove(handler.getPlayer().getUuid()));

        LOGGER.info("[CrystalProtect] Sẵn sàng! Thời gian bảo vệ: {}s",
            CrystalProtectConfig.get().protectionSeconds);
    }

    public static void grantProtection(ServerPlayerEntity player) {
        int ticks = (int)(CrystalProtectConfig.get().protectionSeconds * 20);
        PROTECTED_PLAYERS.put(player.getUuid(), ticks);

        if (CrystalProtectConfig.get().showMessages) {
            sendActionBar(player,
                "🛡 Bảo vệ Crystal: " + CrystalProtectConfig.get().protectionSeconds + "s",
                Formatting.GOLD);
        }
    }

    public static boolean isProtected(ServerPlayerEntity player) {
        return PROTECTED_PLAYERS.containsKey(player.getUuid());
    }

    public static float getSecondsLeft(ServerPlayerEntity player) {
        return PROTECTED_PLAYERS.getOrDefault(player.getUuid(), 0) / 20f;
    }

    public static void sendBlockMessage(ServerPlayerEntity player, String action) {
        if (!CrystalProtectConfig.get().showMessages) return;
        float s = getSecondsLeft(player);
        sendActionBar(player,
            "🚫 Không thể " + action + " Crystal! Còn " + String.format("%.1f", s) + "s",
            Formatting.RED);
    }

    private static void sendActionBar(ServerPlayerEntity player, String msg, Formatting color) {
        player.sendMessage(Text.literal(msg).formatted(color), true);
    }
}
