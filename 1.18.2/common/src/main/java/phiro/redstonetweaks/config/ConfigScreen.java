package phiro.redstonetweaks.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(new TranslatableComponent("redstonetweaks.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        OverlayConfig config = RedstoneTweaksConfig.CONFIG;

        int w = 200;
        int h = 20;
        int x = (this.width - w) / 2;
        int y = this.height / 2 - 60;

        addRenderableWidget(new Button(x, y, w, h,
            toggleLabel(config.overlayEnabled, "redstonetweaks.config.overlayEnabled"),
            btn -> {
                config.overlayEnabled = !config.overlayEnabled;
                btn.setMessage(toggleLabel(config.overlayEnabled, "redstonetweaks.config.overlayEnabled"));
                RedstoneTweaksConfig.save();
            }
        ));

        y += 24;
        addRenderableWidget(new Button(x, y, w, h,
            toggleLabel(config.showSignalStrength, "redstonetweaks.config.showSignalStrength"),
            btn -> {
                config.showSignalStrength = !config.showSignalStrength;
                btn.setMessage(toggleLabel(config.showSignalStrength, "redstonetweaks.config.showSignalStrength"));
                RedstoneTweaksConfig.save();
            }
        ));

        y += 24;
        addRenderableWidget(new Button(x, y, w, h,
            toggleLabel(config.showComponentStatus, "redstonetweaks.config.showComponentStatus"),
            btn -> {
                config.showComponentStatus = !config.showComponentStatus;
                btn.setMessage(toggleLabel(config.showComponentStatus, "redstonetweaks.config.showComponentStatus"));
                RedstoneTweaksConfig.save();
            }
        ));

        y += 24;
        addRenderableWidget(new Button(x, y, w, h,
            toggleLabel(config.showBlockUpdates, "redstonetweaks.config.showBlockUpdates"),
            btn -> {
                config.showBlockUpdates = !config.showBlockUpdates;
                btn.setMessage(toggleLabel(config.showBlockUpdates, "redstonetweaks.config.showBlockUpdates"));
                RedstoneTweaksConfig.save();
            }
        ));

        y += 24;
        addRenderableWidget(new Button(x, y, w, h,
            toggleLabel(config.showWireConnections, "redstonetweaks.config.showWireConnections"),
            btn -> {
                config.showWireConnections = !config.showWireConnections;
                btn.setMessage(toggleLabel(config.showWireConnections, "redstonetweaks.config.showWireConnections"));
                RedstoneTweaksConfig.save();
            }
        ));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    private Component toggleLabel(boolean value, String baseKey) {
        String statusKey = value ? "redstonetweaks.config.enabled" : "redstonetweaks.config.disabled";
        return new TranslatableComponent(baseKey).append(": ").append(new TranslatableComponent(statusKey));
    }
}
