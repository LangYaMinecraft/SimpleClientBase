package cn.langya.utils;

import net.minecraft.client.gui.Gui;

/**
 * @author LangYa
 * @since 2024/11/16 03:59
 *
 * 渲染工具类，提供绘制轮廓的方法。
 */
public class RenderUtil {
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
}
