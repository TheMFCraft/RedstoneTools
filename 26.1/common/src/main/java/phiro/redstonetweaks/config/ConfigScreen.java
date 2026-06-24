package phiro.redstonetweaks.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("redstonetweaks.config.title"));
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

        addRenderableWidget(Button.builder(
            toggleLabel(config.overlayEnabled, "redstonetweaks.config.overlayEnabled"),
            btn -> {
                config.overlayEnabled = !config.overlayEnabled;
                btn.setMessage(toggleLabel(config.overlayEnabled, "redstonetweaks.config.overlayEnabled"));
                RedstoneTweaksConfig.save();
            }
        ).bounds(x, y, w, h).build());

        y += 24;
        addRenderableWidget(Button.builder(
            toggleLabel(config.showSignalStrength, "redstonetweaks.config.showSignalStrength"),
            btn -> {
                config.showSignalStrength = !config.showSignalStrength;
                btn.setMessage(toggleLabel(config.showSignalStrength, "redstonetweaks.config.showSignalStrength"));
                RedstoneTweaksConfig.save();
            }
        ).bounds(x, y, w, h).build());

        y += 24;
        addRenderableWidget(Button.builder(
            toggleLabel(config.showComponentStatus, "redstonetweaks.config.showComponentStatus"),
            btn -> {
                config.showComponentStatus = !config.showComponentStatus;
                btn.setMessage(toggleLabel(config.showComponentStatus, "redstonetweaks.config.showComponentStatus"));
                RedstoneTweaksConfig.save();
            }
        ).bounds(x, y, w, h).build());

        y += 24;
        addRenderableWidget(Button.builder(
            toggleLabel(config.showBlockUpdates, "redstonetweaks.config.showBlockUpdates"),
            btn -> {
                config.showBlockUpdates = !config.showBlockUpdates;
                btn.setMessage(toggleLabel(config.showBlockUpdates, "redstonetweaks.config.showBlockUpdates"));
                RedstoneTweaksConfig.save();
            }
        ).bounds(x, y, w, h).build());

        y += 24;
        addRenderableWidget(Button.builder(
            toggleLabel(config.showWireConnections, "redstonetweaks.config.showWireConnections"),
            btn -> {
                config.showWireConnections = !config.showWireConnections;
                btn.setMessage(toggleLabel(config.showWireConnections, "redstonetweaks.config.showWireConnections"));
                RedstoneTweaksConfig.save();
            }
        ).bounds(x, y, w, h).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    private Component toggleLabel(boolean value, String baseKey) {
        String statusKey = value ? "redstonetweaks.config.enabled" : "redstonetweaks.config.disabled";
        return Component.translatable(baseKey).append(": ").append(Component.translatable(statusKey));
    }
}
