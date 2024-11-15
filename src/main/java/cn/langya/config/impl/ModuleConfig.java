package cn.langya.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cn.langya.Client;
import cn.langya.config.Config;
import cn.langya.module.Module;
import cn.langya.value.Value;
import cn.langya.value.impl.BooleanValue;
import cn.langya.value.impl.ModeValue;
import cn.langya.value.impl.NumberValue;

/**
 * @author LangYa
 * @since 2024/9/3 18:21
 */
public class ModuleConfig extends Config {

    public ModuleConfig() {
        super("Module");
    }

    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enable", module.isEnable());
            moduleObject.addProperty("key", module.getKeyCode());
            JsonObject valuesObject = getValueJsonObject(module);
            moduleObject.add("values", valuesObject);
            object.add(module.getName(), moduleObject);
        }
        return object;
    }

    private JsonObject getValueJsonObject(Module module) {
        JsonObject valuesObject = new JsonObject();
        for (Value<?> value : module.getValues()) {
            if (value instanceof NumberValue) {
                valuesObject.addProperty(value.getName(), ((NumberValue)value).getValue());
            } else if (value instanceof BooleanValue) {
                valuesObject.addProperty(value.getName(), ((BooleanValue)value).getValue());
            } else if (value instanceof ModeValue) {
                valuesObject.addProperty(value.getName(), ((ModeValue)value).getValue());
            }
        }
        return valuesObject;
    }

    public void loadConfig(JsonObject object) {
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            if (object.has(module.getName())) {
                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();
                if (moduleObject.has("enable")) module.setEnable(moduleObject.get("enable").getAsBoolean());
                if (moduleObject.has("key")) module.setKeyCode(moduleObject.get("key").getAsInt());
                if (!moduleObject.has("values")) continue;
                JsonObject valuesObject = moduleObject.get("values").getAsJsonObject();
                for (Value<?> value : module.getValues()) {
                    if (valuesObject.has(value.getName())) {
                        JsonElement theValue = valuesObject.get(value.getName());
                        if (value instanceof NumberValue) {
                            ((NumberValue)value).setValue(theValue.getAsNumber().floatValue());
                        } else if (value instanceof BooleanValue) {
                            ((BooleanValue)value).setValue(theValue.getAsBoolean());
                        } else if (value instanceof ModeValue) {
                            ((ModeValue)value).setValue(theValue.getAsString());
                        }
                    }
                }
            }
        }
    }
}
