package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.module.Module;
import cn.langya.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * @author LangYa
 * @since 2024/11/19 22:37
 */
public class BindCommand extends Command {
    public BindCommand() {
        super("bind");
    }

    @Override
    public void run(String[] args) {
        if (!(args.length == 3)) return;

        boolean nullModule = true;
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            String moduleName = args[1];
            String bindKeyCodeName = args[2];
            if (module.getName().equalsIgnoreCase(moduleName)) {
                int keyCode = Keyboard.getKeyIndex(bindKeyCodeName.toUpperCase());
                if (keyCode == 0) {
                    ChatUtil.log("Cannot find key named " + bindKeyCodeName);
                    return;
                }
                nullModule = false;
                module.setKeyCode(keyCode);
                ChatUtil.info(String.format("%s key has been bound to %s",moduleName,bindKeyCodeName));
            }
        }

        if (nullModule) ChatUtil.info("Module not found");
        super.run(args);
    }
}
