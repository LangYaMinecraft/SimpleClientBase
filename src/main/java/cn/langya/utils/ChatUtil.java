package cn.langya.utils;

import cn.langya.Client;
import cn.langya.Wrapper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtil implements Wrapper {
    private static final String PRIMARY_COLOR = EnumChatFormatting.BLUE.toString();
    private static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String PREFIX = PRIMARY_COLOR + "[" + SECONDARY_COLOR + Client.name + PRIMARY_COLOR + "] ";

    public static void log(String message) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void info(String s) {
        log(PREFIX + s);
    }
}
