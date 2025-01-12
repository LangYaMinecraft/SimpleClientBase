package cn.langya.ui.font;

import cn.langya.ui.font.impl.UFontRenderer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cubk, LangYa
 * 字体管理器
 */
public class FontManager {
    private static final Map<Integer, UFontRenderer> clientFontMap = new ConcurrentHashMap<>();

    // 获取字体渲染器的方法
    private static UFontRenderer getRenderer(String name, int size, Map<Integer, UFontRenderer> map) {
        return map.computeIfAbsent(size, s -> new UFontRenderer(name, s));
    }

    // 获取汉仪字体的方法
    public static UFontRenderer vivo(int size) {
        return getRenderer("VivoSansSCVF", size, clientFontMap);
    }
}
