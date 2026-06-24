#!/bin/bash
set -e
ROOT="/home/max/Documents/Development/PhiroStudios/RedstoneTweaks"

create_architectury_common() {
    local dir="$1"
    mkdir -p "$dir/common/src/main/resources"
    cat > "$dir/common/src/main/resources/architectury.common.json" << 'EOF'
{
  "accessWidener": "redstonetweaks.accesswidener"
}
EOF
}

create_fabric_modjson() {
    local dir="$1"
    local mc_ver="$2"
    mkdir -p "$dir/fabric/src/main/resources"
    cat > "$dir/fabric/src/main/resources/fabric.mod.json" << JSONEOF
{
  "schemaVersion": 1,
  "id": "redstonetweaks",
  "version": "\${mod_version}",
  "name": "Redstone Tweaks",
  "description": "Client-side redstone overlays showing signal strength, component status, block updates, and connection lines.",
  "authors": ["PhiroStudios"],
  "contact": {},
  "license": "MIT",
  "icon": "assets/redstonetweaks/icon.png",
  "environment": "client",
  "entrypoints": {
    "client": [
      "phiro.redstonetweaks.fabric.RedstoneTweaksFabric"
    ],
    "modmenu": [
      "phiro.redstonetweaks.fabric.ModMenuIntegration"
    ]
  },
  "depends": {
    "fabric": "*",
    "minecraft": ">=${mc_ver}",
    "architectury": ">=2.0"
  },
  "custom": {
    "modmenu": {
      "client": true
    }
  }
}
JSONEOF
}

create_neoforge_modstoml() {
    local dir="$1"
    local mc_ver="$2"
    mkdir -p "$dir/neoforge/src/main/resources"
    cat > "$dir/neoforge/src/main/resources/META-INF/neoforge.mods.toml" << 'TOML'
[mods]
  [[mods]]
    modId = "redstonetweaks"
    version = "${mod_version}"
    displayName = "Redstone Tweaks"
    description = "Client-side redstone overlays showing signal strength, component status, block updates, and connection lines."
    authors = ["PhiroStudios"]
    license = "MIT"
    clientSideOnly = true

[[dependencies.redstonetweaks]]
    modId = "minecraft"
    type = "required"
    versionRange = ">=TOMLEOF
echo "${mc_ver}" >> "$dir/neoforge/src/main/resources/META-INF/neoforge.mods.toml"
    cat >> "$dir/neoforge/src/main/resources/META-INF/neoforge.mods.toml" << 'TOML2'
    ordering = "NONE"
    side = "CLIENT"

[[dependencies.redstonetweaks]]
    modId = "architectury"
    type = "required"
    versionRange = ">=2.0"
    ordering = "NONE"
    side = "CLIENT"
TOML2
}

create_entry_points() {
    local dir="$1"
    local package_path="$dir/fabric/src/main/java/phiro/redstonetweaks/fabric"
    mkdir -p "$package_path"
    
    cat > "$package_path/RedstoneTweaksFabric.java" << 'JAVA'
package phiro.redstonetweaks.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import phiro.redstonetweaks.overlay.OverlayConfig;
import phiro.redstonetweaks.overlay.RedstoneOverlayHandler;

public class RedstoneTweaksFabric implements ClientModInitializer {
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstonetweaks.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstonetweaks"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                OverlayConfig.toggleAll();
            }
        });

        WorldRenderEvents.LAST.register(context -> {
            if (client != null && client.player != null && client.world != null && !OverlayConfig.isEmpty()) {
                RedstoneOverlayHandler.INSTANCE.render(context.matrixStack(), context.camera(), client);
            }
        });
    }
}
JAVA

    # ModMenu integration
    cat > "$package_path/ModMenuIntegration.java" << 'JAVA'
package phiro.redstonetweaks.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import phiro.redstonetweaks.config.ConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent);
    }
}
JAVA

    # NeoForge entry point
    if [ -d "$dir/neoforge" ]; then
        local np_path="$dir/neoforge/src/main/java/phiro/redstonetweaks/neoforge"
        mkdir -p "$np_path"
        cat > "$np_path/RedstoneTweaksNeoForge.java" << 'JAVA'
package phiro.redstonetweaks.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyMapping;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import phiro.redstonetweaks.overlay.OverlayConfig;
import phiro.redstonetweaks.overlay.RedstoneOverlayHandler;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;

@Mod(value = "redstonetweaks", dist = Dist.CLIENT)
public class RedstoneTweaksNeoForge {
    private static KeyMapping toggleKey;
    private final Minecraft client = Minecraft.getInstance();

    public RedstoneTweaksNeoForge(IEventBus modBus) {
        modBus.addListener(this::onKeyMappingRegister);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onRenderLevel);
    }

    private void onKeyMappingRegister(RegisterKeyMappingsEvent event) {
        toggleKey = new KeyMapping(
            "key.redstonetweaks.toggle",
            KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstonetweaks"
        );
        event.register(toggleKey);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        while (toggleKey.consumeClick()) {
            OverlayConfig.toggleAll();
        }
    }

    private void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL
            && client.player != null && client.level != null && !OverlayConfig.isEmpty()) {
            RedstoneOverlayHandler.INSTANCE.render(
                event.getPoseStack(), event.getCamera(), client
            );
        }
    }
}
JAVA
    fi
}

# Create for all versions
echo '{"accessWidener":"redstonetweaks.accesswidener"}' > "$ROOT/1.18.2/common/src/main/resources/architectury.common.json"
echo '{"accessWidener":"redstonetweaks.accesswidener"}' > "$ROOT/1.19.4/common/src/main/resources/architectury.common.json"
echo '{"accessWidener":"redstonetweaks.accesswidener"}' > "$ROOT/1.20.6/common/src/main/resources/architectury.common.json"
echo '{"accessWidener":"redstonetweaks.accesswidener"}' > "$ROOT/1.21.5/common/src/main/resources/architectury.common.json"
echo '{"accessWidener":"redstonetweaks.accesswidener"}' > "$ROOT/26.1/common/src/main/resources/architectury.common.json"

# Fabric mod.json for each version
create_fabric_modjson "$ROOT/1.18.2" "1.18"
create_fabric_modjson "$ROOT/1.19.4" "1.19"
create_fabric_modjson "$ROOT/1.20.6" "1.20"
create_fabric_modjson "$ROOT/1.21.5" "1.21"
create_fabric_modjson "$ROOT/26.1" "26.1"

# NeoForge mods.toml for 1.20+
create_neoforge_modstoml "$ROOT/1.20.6" "1.20"
create_neoforge_modstoml "$ROOT/1.21.5" "1.21"
create_neoforge_modstoml "$ROOT/26.1" "26.1"

# Entry points for each version
create_entry_points "$ROOT/1.18.2" 
create_entry_points "$ROOT/1.19.4"
create_entry_points "$ROOT/1.20.6"
create_entry_points "$ROOT/1.21.5"
create_entry_points "$ROOT/26.1"

echo "All metadata and entry points generated!"
