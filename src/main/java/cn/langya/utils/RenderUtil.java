package cn.langya.utils;

import net.minecraft.client.gui.Gui;

/**
 * @author LangYa
 * @since 2024/11/16 03:59
 */
public class RenderUtil {
    public static void drawOutline(int x, int y, int width, int height, int color) {
        Gui gui = new Gui();
        gui.drawHorizontalLine(x, x + width, y, color);
        gui.drawHorizontalLine(x, x + width, y + height, color);

        gui.drawVerticalLine(x, y, y + height, color);
        gui.drawVerticalLine(x + width, y, y + height, color);
    }
}
