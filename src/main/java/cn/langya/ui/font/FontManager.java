package cn.langya.ui.font;

import cn.langya.ui.font.impl.UFontRenderer;

import java.util.HashMap;

public class FontManager {
    private static final HashMap<Integer, UFontRenderer> hanYiMap = new HashMap<>();

    private static UFontRenderer getRenderer(String name, int size, HashMap<Integer, UFontRenderer> map) {
        if (map.containsKey(size)) return map.get(size);
        UFontRenderer newRenderer = new UFontRenderer(name, size);
        map.put(size, newRenderer);
        return newRenderer;
    }

    public static UFontRenderer hanYi(int size) {
        return getRenderer("HYQiHeiX1-65S", size, hanYiMap);
    }
}