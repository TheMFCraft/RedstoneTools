package phiro.redstonetweaks.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class RenderUtil {

    public static void drawText3D(String text, double x, double y, double z, float scale, float r, float g, float b, float a) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        double dx = x - camPos.x;
        double dy = y - camPos.y;
        double dz = z - camPos.z;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance > 32.0) return;

        float textScale = scale * 0.025f;

        PoseStack poseStack = new PoseStack();
        poseStack.translate(dx, dy, dz);
        poseStack.scale(-textScale, -textScale, textScale);

        mc.font.drawInBatch(
                text,
                -mc.font.width(text) / 2f,
                0f,
                ((int) (a * 255) << 24) | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255),
                false,
                poseStack.last().pose(),
                mc.renderBuffers().bufferSource(),
                true,
                0,
                0xF000F0
        );

        mc.renderBuffers().bufferSource().endBatch();
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;

        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(2.0f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        buffer.vertex(x1 - cx, y1 - cy, z1 - cz).color(r, g, b, a).endVertex();
        buffer.vertex(x2 - cx, y2 - cy, z2 - cz).color(r, g, b, a).endVertex();

        tesselator.end();
    }

    public static void drawBoxHighlight(BlockPos pos, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;

        double x = pos.getX() - cx, y = pos.getY() - cy, z = pos.getZ() - cz;

        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(2.0f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        double minX = x, minY = y, minZ = z;
        double maxX = x + 1, maxY = y + 1, maxZ = z + 1;

        buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();

        buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();

        buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();

        tesselator.end();
    }

    public static void fillBlockFace(BlockPos pos, Direction dir, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;
        double x = pos.getX() - cx, y = pos.getY() - cy, z = pos.getZ() - cz;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        double minX = x, minY = y, minZ = z;
        double maxX = x + 1, maxY = y + 1, maxZ = z + 1;

        switch (dir) {
            case DOWN:
                buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
                break;
            case UP:
                buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
                break;
            case NORTH:
                buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
                break;
            case SOUTH:
                buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
                break;
            case WEST:
                buffer.vertex(minX, minY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, maxY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, maxY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(minX, minY, maxZ).color(r, g, b, a).endVertex();
                break;
            case EAST:
                buffer.vertex(maxX, minY, minZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, minY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
                buffer.vertex(maxX, maxY, minZ).color(r, g, b, a).endVertex();
                break;
        }

        tesselator.end();
        RenderSystem.enableCull();
    }

    public static void fillBlockFaceInset(BlockPos pos, Direction dir, double inset, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;
        double ox = pos.getX() - cx, oy = pos.getY() - cy, oz = pos.getZ() - cz;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        switch (dir) {
            case DOWN -> {
                buffer.vertex(ox + inset, oy, oz + inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + inset, oy, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy, oz + inset).color(r, g, b, a).endVertex();
            }
            case UP -> {
                buffer.vertex(ox + inset, oy + 1, oz + inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + 1, oz + inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + 1, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + inset, oy + 1, oz + 1 - inset).color(r, g, b, a).endVertex();
            }
            case NORTH -> {
                buffer.vertex(ox + inset, oy + inset, oz).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + inset, oz).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + 1 - inset, oz).color(r, g, b, a).endVertex();
                buffer.vertex(ox + inset, oy + 1 - inset, oz).color(r, g, b, a).endVertex();
            }
            case SOUTH -> {
                buffer.vertex(ox + inset, oy + inset, oz + 1).color(r, g, b, a).endVertex();
                buffer.vertex(ox + inset, oy + 1 - inset, oz + 1).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + 1 - inset, oz + 1).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1 - inset, oy + inset, oz + 1).color(r, g, b, a).endVertex();
            }
            case WEST -> {
                buffer.vertex(ox, oy + inset, oz + inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox, oy + inset, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox, oy + 1 - inset, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox, oy + 1 - inset, oz + inset).color(r, g, b, a).endVertex();
            }
            case EAST -> {
                buffer.vertex(ox + 1, oy + inset, oz + inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1, oy + inset, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1, oy + 1 - inset, oz + 1 - inset).color(r, g, b, a).endVertex();
                buffer.vertex(ox + 1, oy + 1 - inset, oz + inset).color(r, g, b, a).endVertex();
            }
        }

        tesselator.end();
        RenderSystem.enableCull();
    }
}
