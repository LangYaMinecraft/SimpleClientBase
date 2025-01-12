package cn.langya.utils;

import cn.langya.Wrapper;
import cn.langya.ui.font.FontManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.GameSettings;

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
     * @param x 矩形左上角的x坐标
     * @param y 矩形左上角的y坐标
     * @param width 矩形的宽度
     * @param height 矩形的高度
     * @param color 轮廓的颜色
     */
    public static void drawOutline(int x, int y, int width, int height, int color) {
        Gui gui = new Gui();
        gui.drawHorizontalLine(x, x + width, y, color);
        gui.drawHorizontalLine(x, x + width, y + height, color);

        gui.drawVerticalLine(x, y, y + height, color);
        gui.drawVerticalLine(x + width, y, y + height, color);
    }

    public static void drawRect(int x,int y,int width,int height,int color) {
        Gui.drawRect(x,y,x + width,y + height,color);
    }

    public static void drawRect(int x, int y, int width, int height, Color color) {
        drawRect(x,y,width,height,color.getRGB());
    }

    public static void drawRectWithOutline(int x,int y,int width,int height,int color,int outlineColor) {
        Gui.drawRect(x,y,x + width,y + height,color);
        drawOutline(x,y,width,height,outlineColor);
    }

    public static int drawCenterString(String text, int x, int y, int color) {
        return (int) FontManager.vivo(18).drawString(text,(x - getStringWidth(text) / 2),y,color);
    }

    public static int getStringWidth(String text) {
        return FontManager.vivo(18).getStringWidth(text);
    }


    public static int drawString(String text, int x, int y, int color) {
        return drawString(text,x,y,color,false);
    }
    public static int drawString(String text, int x, int y, int color,boolean textShadow) {
        return FontManager.vivo(18).drawString(text,x,y,color,textShadow);
    }
    public static int drawStringWithShadow(String text, int x, int y, int color) {
        return FontManager.vivo(18).drawStringWithShadow(text,x,y,color);
    }

    public static int drawText(String text, int x, int y, boolean bg, int bgColor,int textColor,boolean textShadow) {
        int width = FontManager.vivo(18).getStringWidth(text);
        int height = FontManager.vivo(18).getHeight();
        int width1 = width + 6;
        if (bg) RenderUtil.drawRect(x - 2, y - 2, width1,height + 4,bgColor);
        FontManager.vivo(18).drawString(text, x + 1, y - 1, textColor, textShadow);
        return width1;
    }
}
