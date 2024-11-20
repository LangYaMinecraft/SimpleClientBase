package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.module.Module;
import cn.langya.utils.ChatUtil;

/**
 * @author LangYa
 * @since 2024/11/19 22:51
 */
public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("t");
    }

    @Override
    public void run(String[] args) {
        if (!(args.length == 2)) return;

        boolean nullModule = true;
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            String moduleName = args[1];
            if (module.getName().equalsIgnoreCase(moduleName)) {
                nullModule = false;
                module.toggle();
            }
        }

        if (nullModule) ChatUtil.info("Module not found");
        super.run(args);
    }
}
