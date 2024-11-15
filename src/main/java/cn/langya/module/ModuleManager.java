package cn.langya.module;

import cn.langya.Client;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventKeyInput;
import cn.langya.module.impl.combat.*;
import cn.langya.module.impl.move.*;
import cn.langya.module.impl.render.*;
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
        this.moduleMap.put(module.getName(),module);
    }

    public void init() {
        addModule(new HUD());
        addModule(new Sprint());
        addModule(new KillAura());

        Client.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        for (Module module : moduleMap.values()) {
            if (module.getKeyCode() == event.getKeyCode()) module.toggle();
        }
    }
}
