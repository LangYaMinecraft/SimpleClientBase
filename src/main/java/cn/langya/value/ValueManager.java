package cn.langya.value;

import cn.langya.Client;
import cn.langya.Logger;
import cn.langya.module.Module;

import java.lang.reflect.Field;

/**
 * @author LangYa
 * @since 2024/9/1 20:08
 */
public class ValueManager {

    public ValueManager() {
        Client.getInstance()
                .getModuleManager()
                .getModuleMap()
                .values()
                .forEach(this::registerValues);
    }

    /**
     * Registers values for the given module by inspecting its fields.
     *
     * @param module the module to register values for
     */
    private void registerValues(Module module) {
        Field[] fields = module.getClass().getDeclaredFields();

        for (Field field : fields) {
            registerValue(module, field);
        }
    }

    /**
     * Attempts to register a single value field for a module.
     *
     * @param module the module the field belongs to
     * @param field  the field to register
     */
    private void registerValue(Module module, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(module);

            if (value instanceof Value) {
                module.getValues().add((Value<?>) value);
            }
        } catch (IllegalAccessException e) {
            Logger.error("Error registering value for module {}: ", module.getName(), e);
        }
    }
}
