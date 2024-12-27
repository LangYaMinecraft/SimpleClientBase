package cn.langya.module;

import cn.langya.Client;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventKeyInput;
import cn.langya.utils.InitializerUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa
 * @since 2024/11/16 03:46
 */
@Getter
public class ModuleManager {
    private final Map<String,Module> moduleMap;

    public ModuleManager() {
        this.moduleMap = new HashMap<>();

        init();
    }

    public void addModule(Module module) {
        this.moduleMap.put(module.getName(), module);
    }

    public Module getModule(String moduleName) {
        return moduleMap.get(moduleName);
    }

    public Module getModule(Class<?> moduleClazz) {
        return moduleMap.get(moduleClazz.getSimpleName());
    }

    public void init() {
        InitializerUtil.initialize(clazz -> {
            if (!InitializerUtil.check(Module.class,clazz)) return;
            try {
                Module mInstance = (Module) clazz.newInstance();
                addModule(mInstance);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }, this.getClass());

        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        for (Module module : moduleMap.values()) {
            if (module.getKeyCode() == event.getKeyCode()) module.toggle();
        }
    }
}
