package phiro.redstonetweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.*;

public class RedstoneTweaksConfig {
    public static final OverlayConfig CONFIG = new OverlayConfig();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    public static void init() {
        configFile = new File(Minecraft.getInstance().gameDirectory, "config/redstonetweaks.json");
        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                OverlayConfig loaded = GSON.fromJson(reader, OverlayConfig.class);
                if (loaded != null) {
                    CONFIG.overlayEnabled = loaded.overlayEnabled;
                    CONFIG.showSignalStrength = loaded.showSignalStrength;
                    CONFIG.showComponentStatus = loaded.showComponentStatus;
                    CONFIG.showBlockUpdates = loaded.showBlockUpdates;
                    CONFIG.showWireConnections = loaded.showWireConnections;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        if (configFile == null) return;
        configFile.getParentFile().mkdirs();
        try (Writer writer = new FileWriter(configFile)) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
