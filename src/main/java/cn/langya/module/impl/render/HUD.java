package cn.langya.module.impl.render;

import cn.langya.Client;
import cn.langya.ui.Element;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventRender2D;
import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.ui.font.FontManager;
import cn.langya.ui.font.impl.UFontRenderer;
import cn.langya.value.impl.ColorValue;
import cn.langya.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author LangYa
 * @since 2024/11/16 03:50
 * HUD类用于在游戏中渲染用户界面元素
 */
public class HUD extends Module {

    public HUD() {
        super(Category.Render);
    }

    private final NumberValue spacingValue = new NumberValue("Spacing",2,5,0,1); // 设置模块之间的间距
    private final ColorValue colorValue = new ColorValue("ModuleListTextColor",-1);
    private final Element element = Client.getInstance().getElementManager().createElement(getName()); // 创建显示元素

    /**
     * 在2D渲染事件中绘制HUD信息
     * @param event 触发的2D渲染事件
     */
    @EventTarget
    public void onRender2D(EventRender2D event) {
        String displayText = String.format("%s | %sFPS",Client.name, Minecraft.getDebugFPS()); // 获取并格式化显示文本
        UFontRenderer fr = FontManager.vivo(18); // 获取字体渲染器
        float width = fr.getStringWidth(displayText); // 计算文本宽度
        float height = fr.FONT_HEIGHT; // 获取文本高度
        element.setWH(width,height); // 设置元素的宽高
        fr.drawStringWithShadow(displayText, (int) element.getX(), (int) element.getY(), -1); // 绘制主信息文本

        int index = 0;
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) { // 遍历所有模块
            if (!module.isEnable()) continue; // 跳过未启用的模块
            fr.drawStringWithShadow(module.getSuffix().isEmpty() ? module.getName() : String.format("%s %s%s", module.getName(), EnumChatFormatting.GRAY, module.getSuffix()),10,25 + (index * fr.FONT_HEIGHT + spacingValue.getValue().intValue()),colorValue.getColor()); // 绘制已启用模块信息
            index++;
        }
    }
}
