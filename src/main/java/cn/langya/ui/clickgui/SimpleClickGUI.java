package cn.langya.ui.clickgui;

import cn.langya.Client;
import cn.langya.module.Module;
import cn.langya.ui.font.FontManager;
import cn.langya.utils.HoveringUtil;
import cn.langya.utils.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/9/25 18:48
 * 简单点击图形用户界面类
 */
public class SimpleClickGUI extends GuiScreen {
    private int scrollOffset = 2;
    private static final int MAX_SCROLL = 200;
    private static final int MIN_SCROLL = 0;
    private int addWidth,delWidth;

    /**
     * GUI关闭时保存配置
     */
    @Override
    public void onGuiClosed() {
        Client.getInstance().getConfigManager().saveConfig("Module.json");
        super.onGuiClosed();
    }

    /**
     * 绘制屏幕内容
     * @param mouseX 鼠标的X坐标
     * @param mouseY 鼠标的Y坐标
     * @param partialTicks 渲染的部分刻度
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int y = 0;
        int x = 1;

        RenderUtil.drawRect(width - 11,1,6,MAX_SCROLL + 11,Color.gray);
        RenderUtil.drawRect(width - 10,scrollOffset,5,10,Color.white);
        Color bgColor = new Color(0,0,0,80);
        addWidth = RenderUtil.drawText("+",width - 30,1,true, bgColor.getRGB(),-1,true);
        delWidth = RenderUtil.drawText("-",width - 30,30,true, bgColor.getRGB(),-1,true);

        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            y += 15;

            int displayY = y - scrollOffset;

            // 可见
            if (displayY >= 0 && displayY <= height - 15) {
                String nameText;
                String description;
                String settingsText;
                nameText = module.getName();
                description = module.getDescription();
                settingsText = "Settings";
                RenderUtil.drawRectWithOutline(0, displayY - 1, RenderUtil.getStringWidth(nameText + description) + 8, FontManager.vivo(18).getHeight() + 3, new Color(0, 0, 0, 80).getRGB(), -1);
                RenderUtil.drawString(nameText, x, displayY, -1);
                RenderUtil.drawString(description, x + RenderUtil.getStringWidth(nameText) + 5, displayY, -1);
                RenderUtil.drawRectWithOutline(x + RenderUtil.getStringWidth(nameText + description) + 10, displayY + 2, 5, 5, module.isEnable() ? Color.GREEN.getRGB() : Color.RED.getRGB(), -1);

                if (!module.getValues().isEmpty())
                    RenderUtil.drawStringWithShadow(settingsText, x + RenderUtil.getStringWidth(nameText + description) + 30, displayY, -1);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * 处理鼠标点击事件
     * @param mouseX 鼠标的X坐标
     * @param mouseY 鼠标的Y坐标
     * @param mouseButton 鼠标按钮索引
     * @throws IOException 处理输入时可能抛出异常
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int y = 0;
        int x = 1;

        for (Module module : Client.getInstance().getModuleManager().getModuleMap().values()) {
            y += 15;

            int displayY = y - scrollOffset;

            if (displayY >= 0 && displayY <= height - 15) {
                String nameText;
                String description;
                String settingsText;
                nameText = module.getName();
                description = module.getDescription();
                settingsText = "Settings";
                if (HoveringUtil.isHovering(x + RenderUtil.getStringWidth(nameText + description) + 10, displayY, 5, 7, mouseX, mouseY))
                    module.toggle();

                if (!module.getValues().isEmpty() && HoveringUtil.isHovering(x + RenderUtil.getStringWidth(nameText + description) + 30, displayY, RenderUtil.getStringWidth(settingsText) + 4, mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY))
                    mc.displayGuiScreen(new ValueScreen(module));
            }
        }

        if (HoveringUtil.isHovering(width - 30,1,addWidth,addWidth,mouseX,mouseY)) {
            int after = scrollOffset + 15;
            if (after < MAX_SCROLL) scrollOffset = after;
        }

        if (HoveringUtil.isHovering(width - 30,30,delWidth,delWidth,mouseX,mouseY)) {
            int after = scrollOffset - 15;
            if (after > MIN_SCROLL) scrollOffset = after;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * 处理鼠标输入事件
     * @throws IOException 处理输入时可能抛出异常
     */
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int wheel = Integer.signum(Mouse.getDWheel());
        if (wheel != 0) {
            scrollOffset -= wheel * 15;

            // 限制滚动范围
            scrollOffset = Math.max(MIN_SCROLL, Math.min(scrollOffset, MAX_SCROLL));
        }
    }

    /**
     * 判断GUI是否暂停游戏
     * @return 返回false表示不暂停游戏
     */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
