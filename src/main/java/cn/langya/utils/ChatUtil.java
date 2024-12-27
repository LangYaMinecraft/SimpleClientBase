package cn.langya.utils;

import cn.langya.Client;
import cn.langya.Wrapper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author LangYa466
 * 类提供聊天相关的工具方法
 */
public class ChatUtil implements Wrapper {
    private static final String PREFIX = EnumChatFormatting.BLUE + "["
            + EnumChatFormatting.GRAY + Client.name
            + EnumChatFormatting.BLUE + "] ";

    // 记录聊天信息的方法
    public static void log(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    // 以特定前缀记录信息的方法
    public static void info(String message) {
        log(PREFIX + message);
    }
}
