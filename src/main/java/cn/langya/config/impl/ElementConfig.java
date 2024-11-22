package cn.langya.config.impl;

import com.google.gson.JsonObject;
import cn.langya.Client;
import cn.langya.Wrapper;
import cn.langya.config.Config;
import cn.langya.ui.Element;

/**
 * @author LangYa
 * @since 2024/9/3 18:21
 */
public class ElementConfig extends Config implements Wrapper {

    public ElementConfig() {
        super("Element");
    }

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

    public void loadConfig(final JsonObject object) {
        for (Element hud : Client.getInstance().getElementManager().getElementMap().values()) {
            if (object.has(hud.getName())) {
                final JsonObject hudObject = object.get(hud.getName()).getAsJsonObject();
                hud.setXY(hudObject.get("x").getAsInt(),hudObject.get("y").getAsInt());
            }
        }
    }
}
