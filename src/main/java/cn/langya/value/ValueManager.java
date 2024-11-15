package cn.langya.value;

import cn.langya.Client;
import cn.langya.module.Module;

import java.lang.reflect.Field;

/**
 * @author LangYa
 * @since 2024/9/1 20:08
 */
public class ValueManager {

    public ValueManager() {
        init();
    }

    public void init() {
        // 就static forEach moment
        Client.getInstance().getModuleManager().getModuleMap().values().forEach(ValueManager::addValues);
    }

    private static void addValues(Module module) {
        for (Field field : module.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                final Object obj = field.get(module);
                if (obj instanceof Value) module.getValues().add((Value<?>) obj);
            } catch (IllegalAccessException e) {
                System.out.printf("%s register value error : %s%n",module.getName(),e.getMessage());
            }
        }
    }
}
