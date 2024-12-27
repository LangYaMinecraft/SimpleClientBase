package cn.langya.utils;

// 该类提供了一些与悬停相关的实用工具方法
public class HoveringUtil {
    // 检查鼠标是否悬停在指定的矩形区域内
    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}

