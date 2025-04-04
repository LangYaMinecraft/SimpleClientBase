package cn.langya.utils;

import cn.langya.Wrapper;
import cn.langya.ui.font.FontManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

/**
 * @author LangYa
 * @since 2024/11/16 03:59
 *
 * 渲染工具类，提供绘制轮廓的方法。
 */
public class RenderUtil implements Wrapper {
    /**
     * 绘制一个矩形的轮廓
     *
     * @param x      矩形左上角的x坐标
     * @param y      矩形左上角的y坐标
     * @param width  矩形的宽度
     * @param height 矩形的高度
     * @param color  轮廓的颜色
     */
    public static void drawOutline(int x, int y, int width, int height, int color) {
        Gui gui = new Gui();
        gui.drawHorizontalLine(x, x + width, y, color);
        gui.drawHorizontalLine(x, x + width, y + height, color);

        gui.drawVerticalLine(x, y, y + height, color);
        gui.drawVerticalLine(x + width, y, y + height, color);
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawRect(int x, int y, int width, int height, Color color) {
        drawRect(x, y, width, height, color.getRGB());
    }

    public static void drawRectWithOutline(int x, int y, int width, int height, int color, int outlineColor) {
        Gui.drawRect(x, y, x + width, y + height, color);
        drawOutline(x, y, width, height, outlineColor);
    }

    public static int drawCenterString(String text, int x, int y, int color) {
        return (int) FontManager.vivo(18).drawString(text, (x - getStringWidth(text) / 2), y, color);
    }

    public static int getStringWidth(String text) {
        return FontManager.vivo(18).getStringWidth(text);
    }


    public static int drawString(String text, int x, int y, int color) {
        return drawString(text, x, y, color, false);
    }

    public static int drawString(String text, int x, int y, int color, boolean textShadow) {
        return FontManager.vivo(18).drawString(text, x, y, color, textShadow);
    }

    public static int drawStringWithShadow(String text, int x, int y, int color) {
        return FontManager.vivo(18).drawStringWithShadow(text, x, y, color);
    }

    public static void drawRect2(double left, double top, double right, double bottom, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        double minX = Math.min(left, right);
        double maxX = Math.max(left, right);
        double minY = Math.min(top, bottom);
        double maxY = Math.max(top, bottom);

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(minX, maxY, 0.0D).endVertex();
        worldrenderer.pos(maxX, maxY, 0.0D).endVertex();
        worldrenderer.pos(maxX, minY, 0.0D).endVertex();
        worldrenderer.pos(minX, minY, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawColorSlider(int x, int y, int width, double value, double max, Color color) {
        int barWidth = (int) (width * (value / max));

        // 背景
        drawRect(x, y, width, 5, new Color(50, 50, 50).getRGB());

        // 进度条
        drawRect(x, y, barWidth, 5, color.getRGB());

        // 滑块
        drawRect(x + barWidth - 2, y - 2, 4, 9, Color.WHITE.getRGB());
    }
}
