package cn.langya;

import net.minecraft.client.Minecraft;

/**
 * @author LangYa
 * @since 2024/11/16 03:46
 *
 * Wrapper接口，提供对Minecraft实例的包装。
 */
public interface Wrapper {
    Minecraft mc = Minecraft.getMinecraft(); // Minecraft实例的静态引用
}

