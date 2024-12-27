package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.module.Module;
import cn.langya.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * @author LangYa
 * @since 2024/11/19 22:37
 * 绑定命令类，用于将特定模块绑定到指定的按键
 */
public class BindCommand extends Command {
    public BindCommand() {
        super("bind", ".bind moduleName bindKeyName");
    }

    @Override
    public void run(String[] args) {
        // 运行绑定命令，接收模块名称和绑定的按键名称作为参数
        if (args.length != 3) {
            return; // If arguments are not valid, exit early
        }

        String moduleName = args[1];
        String bindKeyCodeName = args[2];
        int keyCode = Keyboard.getKeyIndex(bindKeyCodeName.toUpperCase());

        if (keyCode == 0) {
            ChatUtil.log("Cannot find key named " + bindKeyCodeName);
            return;
        }

        Module module = Client.getInstance().getModuleManager().getModule(moduleName);
        if (module == null) {
            ChatUtil.info("Module not found");
            return;
        }

        module.setKeyCode(keyCode);
        ChatUtil.info(String.format("%s key has been bound to %s", moduleName, bindKeyCodeName));

        super.run(args);
    }
}
