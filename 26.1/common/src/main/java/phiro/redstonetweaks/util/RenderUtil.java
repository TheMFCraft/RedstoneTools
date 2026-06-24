package phiro.redstonetweaks.util;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
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
                0,
                ((int) (a * 255) << 24) | ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255),
                false,
                poseStack.last().pose(),
                mc.renderBuffers().bufferSource(),
                net.minecraft.client.gui.Font.DisplayMode.SEE_THROUGH,
                0,
                0xF000F0
        );

        mc.renderBuffers().bufferSource().endBatch();
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        buffer.addVertex((float)(x1 - cx), (float)(y1 - cy), (float)(z1 - cz)).setColor(r, g, b, a);
        buffer.addVertex((float)(x2 - cx), (float)(y2 - cy), (float)(z2 - cz)).setColor(r, g, b, a);

        buffer.build();
    }

    public static void drawBoxHighlight(BlockPos pos, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;

        double x = pos.getX() - cx, y = pos.getY() - cy, z = pos.getZ() - cz;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        double minX = x, minY = y, minZ = z;
        double maxX = x + 1, maxY = y + 1, maxZ = z + 1;

        buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);

        buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);

        buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
        buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);

        buffer.build();
    }

    public static void fillBlockFace(BlockPos pos, Direction dir, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;
        double x = pos.getX() - cx, y = pos.getY() - cy, z = pos.getZ() - cz;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        double minX = x, minY = y, minZ = z;
        double maxX = x + 1, maxY = y + 1, maxZ = z + 1;

        switch (dir) {
            case DOWN:
                buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
                break;
            case UP:
                buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                break;
            case NORTH:
                buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                break;
            case SOUTH:
                buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                break;
            case WEST:
                buffer.addVertex((float)minX, (float)minY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)minX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                break;
            case EAST:
                buffer.addVertex((float)maxX, (float)minY, (float)minZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)minY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)maxZ).setColor(r, g, b, a);
                buffer.addVertex((float)maxX, (float)maxY, (float)minZ).setColor(r, g, b, a);
                break;
        }

        buffer.build();
    }

    public static void fillBlockFaceInset(BlockPos pos, Direction dir, double inset, float r, float g, float b, float a) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        double cx = camPos.x, cy = camPos.y, cz = camPos.z;
        double ox = pos.getX() - cx, oy = pos.getY() - cy, oz = pos.getZ() - cz;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        switch (dir) {
            case DOWN:
                buffer.addVertex((float)(ox + inset), (float)oy, (float)(oz + inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + inset), (float)oy, (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)oy, (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)oy, (float)(oz + inset)).setColor(r, g, b, a);
                break;
            case UP:
                buffer.addVertex((float)(ox + inset), (float)(oy + 1), (float)(oz + inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + 1), (float)(oz + inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + 1), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + inset), (float)(oy + 1), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                break;
            case NORTH:
                buffer.addVertex((float)(ox + inset), (float)(oy + inset), (float)oz).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + inset), (float)oz).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + 1 - inset), (float)oz).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + inset), (float)(oy + 1 - inset), (float)oz).setColor(r, g, b, a);
                break;
            case SOUTH:
                buffer.addVertex((float)(ox + inset), (float)(oy + inset), (float)(oz + 1)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + inset), (float)(oy + 1 - inset), (float)(oz + 1)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + 1 - inset), (float)(oz + 1)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1 - inset), (float)(oy + inset), (float)(oz + 1)).setColor(r, g, b, a);
                break;
            case WEST:
                buffer.addVertex((float)ox, (float)(oy + inset), (float)(oz + inset)).setColor(r, g, b, a);
                buffer.addVertex((float)ox, (float)(oy + inset), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)ox, (float)(oy + 1 - inset), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)ox, (float)(oy + 1 - inset), (float)(oz + inset)).setColor(r, g, b, a);
                break;
            case EAST:
                buffer.addVertex((float)(ox + 1), (float)(oy + inset), (float)(oz + inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1), (float)(oy + inset), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1), (float)(oy + 1 - inset), (float)(oz + 1 - inset)).setColor(r, g, b, a);
                buffer.addVertex((float)(ox + 1), (float)(oy + 1 - inset), (float)(oz + inset)).setColor(r, g, b, a);
                break;
        }

        buffer.build();
    }
}
