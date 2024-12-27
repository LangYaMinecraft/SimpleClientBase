package cn.langya.config.impl;

import com.google.gson.JsonObject;
import cn.langya.Client;
import cn.langya.Wrapper;
import cn.langya.config.Config;
import cn.langya.ui.Element;

/**
 * @author LangYa
 * @since 2024/9/3 18:21
 * ElementConfig 类用于管理元素的配置信息，负责保存和加载元素的位置信息。
 */
public class ElementConfig extends Config implements Wrapper {

    public ElementConfig() {
        super("Element");
    }

    /**
     * 保存当前所有元素的位置信息到 JSON 对象中。
     * @return 包含元素位置信息的 JsonObject
     */
    public JsonObject saveConfig() {
        final JsonObject object = new JsonObject();
        for (Element hud : Client.getInstance().getElementManager().getElementMap().values()) {
            final JsonObject hudObject = new JsonObject();
            hudObject.addProperty("x", hud.getX());
            hudObject.addProperty("y", hud.getY());
            object.add(hud.getName(), hudObject);
        }
        return object;
    }

    /**
     * 从 JSON 对象中加载元素的位置信息。
     * @param object 包含元素位置信息的 JsonObject
     */
    public void loadConfig(final JsonObject object) {
        for (Element hud : Client.getInstance().getElementManager().getElementMap().values()) {
            if (object.has(hud.getName())) {
                final JsonObject hudObject = object.get(hud.getName()).getAsJsonObject();
                hud.setXY(hudObject.get("x").getAsInt(),hudObject.get("y").getAsInt());
            }
        }
    }
}
