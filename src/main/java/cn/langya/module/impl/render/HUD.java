package cn.langya.module.impl.render;

import cn.langya.Client;
import cn.langya.ui.Element;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventRender2D;
import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.ui.font.FontManager;
import cn.langya.ui.font.impl.UFontRenderer;
import cn.langya.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author LangYa
 * @since 2024/11/16 03:50
 */
public class HUD extends Module {

    public HUD() {
        super(Category.Render);
    }

    private final NumberValue spacingValue = new NumberValue("Spacing",2,5,0,1);
    private final Element element = Client.getInstance().getElementManager().createElement("HUD",getName());

    @EventTarget
    public void onRender2D(EventRender2D event) {
        String displayText = String.format("%s | %sFPS",Client.name, Minecraft.getDebugFPS());
        UFontRenderer fr = FontManager.hanYi(18);
        float width = fr.getStringWidth(displayText);
        float height = fr.FONT_HEIGHT;
        element.setWH(width,height);
        fr.drawStringWithShadow(displayText, (int) element.getX(), (int) element.getY(), -1);

        int index = 0;
        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            if (!module.isEnable()) continue;
            fr.drawStringWithShadow(module.getSuffix().isEmpty() ? module.getName() : String.format("%s %s%s", module.getName(), EnumChatFormatting.GRAY, module.getSuffix()),10,25 + (index * fr.FONT_HEIGHT + spacingValue.getValue().intValue()),-1);
            index++;
        }
    }
}
