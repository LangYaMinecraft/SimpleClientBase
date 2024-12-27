package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.module.Module;
import cn.langya.utils.ChatUtil;

/**
 * 处理模块切换的命令
 * @author LangYa
 * @since 2024/11/19 22:51
 */
public class ToggleCommand extends Command {
    /**
     * 构造函数，设置命令的名称和用法
     */
    public ToggleCommand() {
        super("t", ".t moduleName");
    }

    /**
     * 执行切换模块的操作
     * @param args 命令行参数
     */
    @Override
    public void run(String[] args) {
        if (args.length != 2) {
            return; // If arguments are not valid, exit early
        }

        String moduleName = args[1];
        Module module = Client.getInstance().getModuleManager().getModule(moduleName);

        if (module == null) {
            ChatUtil.info("Module not found");
            return;
        }

        module.toggle();
        super.run(args);
    }
}
