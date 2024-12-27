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

import java.util.stream.Collectors;

/**
 * @author LangYa
 * @since 2024/9/3 18:21
 * 模块配置类，负责模块的保存和加载配置。
 */
public class ModuleConfig extends Config {

    /**
     * 构造函数，初始化模块配置。
     */
    public ModuleConfig() {
        super("Module");
    }

    /**
     * 保存当前所有模块的配置，并以JsonObject格式返回。
     * @return 包含所有模块配置的JsonObject
     */
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        Client.getInstance().getModuleManager().getModuleMap().values().forEach(module -> {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enable", module.isEnable());
            moduleObject.addProperty("key", module.getKeyCode());
            moduleObject.add("values", getValueJsonObject(module));
            object.add(module.getName(), moduleObject);
        });
        return object;
    }

    /**
     * 将模块的值转换为JsonObject格式。
     * @param module 需要转换的模块
     * @return 包含模块值的JsonObject
     */
    private JsonObject getValueJsonObject(Module module) {
        JsonObject valuesObject = new JsonObject();
        module.getValues().forEach(value -> {
            if (value instanceof NumberValue) {
                valuesObject.addProperty(value.getName(), ((NumberValue) value).getValue());
            } else if (value instanceof BooleanValue) {
                valuesObject.addProperty(value.getName(), ((BooleanValue) value).getValue());
            } else if (value instanceof ModeValue) {
                valuesObject.addProperty(value.getName(), ((ModeValue) value).getValue());
            }
        });
        return valuesObject;
    }

    /**
     * 从JsonObject中加载模块配置。
     * @param object 包含模块配置的JsonObject
     */
    public void loadConfig(JsonObject object) {
        Client.getInstance().getModuleManager().getModuleMap().values().forEach(module -> {
            if (object.has(module.getName())) {
                JsonObject moduleObject = object.getAsJsonObject(module.getName());
                moduleObject.entrySet().forEach(entry -> {
                    switch (entry.getKey()) {
                        case "enable":
                            module.setEnable(entry.getValue().getAsBoolean());
                            break;
                        case "key":
                            module.setKeyCode(entry.getValue().getAsInt());
                            break;
                        case "values":
                            loadValues(module, entry.getValue().getAsJsonObject());
                            break;
                    }
                });
            }
        });
    }

    /**
     * 加载模块的值配置。
     * @param module 需要加载值的模块
     * @param valuesObject 包含值的JsonObject
     */
    private void loadValues(Module module, JsonObject valuesObject) {
        module.getValues().forEach(value -> {
            JsonElement theValue = valuesObject.get(value.getName());
            if (theValue != null) {
                if (value instanceof NumberValue) {
                    ((NumberValue) value).setValue(theValue.getAsNumber().floatValue());
                } else if (value instanceof BooleanValue) {
                    ((BooleanValue) value).setValue(theValue.getAsBoolean());
                } else if (value instanceof ModeValue) {
                    ((ModeValue) value).setValue(theValue.getAsString());
                }
            }
        });
    }
}
