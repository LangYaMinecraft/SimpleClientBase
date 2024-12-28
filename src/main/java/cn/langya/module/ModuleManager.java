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
 * 管理所有模块及其交互.
 *
 * @author LangYa
 * @since 2024/11/16 03:46
 */
@Getter
public class ModuleManager {
    private final Map<String, Module> moduleMap = new HashMap<>();

    /**
     * 构造函数，初始化模块管理器.
     */
    public ModuleManager() {
        init();
    }

    /**
     * 添加模块到模块地图.
     *
     * @param module 要添加的模块
     */
    public void addModule(Module module) {
        moduleMap.put(module.getName(), module);
    }

    /**
     * 根据模块名获取模块.
     *
     * @param moduleName 模块名称
     * @return 返回对应的模块
     */
    public Module getModule(String moduleName) {
        return moduleMap.get(moduleName);
    }

    /**
     * 根据模块类获取模块.
     *
     * @param moduleClass 模块类
     * @return 返回对应的模块
     */
    public Module getModule(Class<?> moduleClass) {
        return moduleMap.get(moduleClass.getSimpleName());
    }

    /**
     * 初始化模块管理器，注册模块并设置事件监听.
     */
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

    /**
     * 处理键盘输入事件，切换相应模块状态.
     *
     * @param event 键盘输入事件
     */
    @EventTarget
    public void onKeyInput(EventKeyInput event) {
        moduleMap.values().stream()
                .filter(module -> module.getKeyCode() == event.getKeyCode())
                .forEach(Module::toggle);
    }
}

