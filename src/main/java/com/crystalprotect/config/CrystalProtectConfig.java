package com.crystalprotect.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class CrystalProtectConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir().resolve("crystalprotect.json");

    private static ConfigData instance = new ConfigData();

    public static class ConfigData {
        /** Thời gian bảo vệ sau khi kill (giây) */
        public float protectionSeconds = 3.0f;

        /** Hiển thị thông báo action bar */
        public boolean showMessages = true;

        /** Chặn đặt crystal */
        public boolean blockPlace = true;

        /** Chặn đập crystal */
        public boolean blockAttack = true;

        /** Chặn tương tác crystal */
        public boolean blockInteract = true;

        /** Bảo vệ kể cả khi kill bằng crystal (indirect kill) */
        public boolean protectOnCrystalKill = true;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                instance = GSON.fromJson(r, ConfigData.class);
            } catch (IOException e) {
                instance = new ConfigData();
            }
        }
        save();
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(instance, w);
        } catch (IOException e) {
            // ignore
        }
    }

    public static ConfigData get() {
        return instance;
    }
}
