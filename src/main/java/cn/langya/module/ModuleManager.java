package cn.langya.module;

import cn.langya.Client;
import cn.langya.Logger;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventKeyInput;
import cn.langya.utils.InitializerUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all modules and their interactions.
 *
 * @author LangYa
 * @since 2024/11/16 03:46
 */
@Getter
public class ModuleManager {
    private final Map<String, Module> moduleMap = new HashMap<>();

    public ModuleManager() {
        init();
    }

    public void addModule(Module module) {
        moduleMap.put(module.getName(), module);
    }

    public Module getModule(String moduleName) {
        return moduleMap.get(moduleName);
    }

    public Module getModule(Class<?> moduleClass) {
        return moduleMap.get(moduleClass.getSimpleName());
    }

    private void init() {
        InitializerUtil.initialize(clazz -> {
            if (InitializerUtil.check(Module.class, clazz)) {
                try {
                    addModule((Module) clazz.getDeclaredConstructor().newInstance());
                } catch (ReflectiveOperationException e) {
                    Logger.error("Registration module {} failed",e);
                }
            }
        }, getClass());

        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        moduleMap.values().stream()
                .filter(module -> module.getKeyCode() == event.getKeyCode())
                .forEach(Module::toggle);
    }
}
