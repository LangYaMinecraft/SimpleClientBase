package cn.langya.ui.clickgui;
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//         佛祖保佑       永无BUG     永不修改                  //
////////////////////////////////////////////////////////////////////

import cn.langya.Client;
import cn.langya.module.Module;
import cn.langya.utils.HoveringUtil;
import cn.langya.utils.RenderUtil;
import cn.langya.value.Value;
import cn.langya.value.impl.BooleanValue;
import cn.langya.value.impl.ColorValue;
import cn.langya.value.impl.ModeValue;
import cn.langya.value.impl.NumberValue;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/9/25 18:19
 */
public class ValueScreen extends GuiScreen {

    private final Module module;
    public ValueScreen(Module module) {
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int y = 10;
        int x = 0;
        for (Value<?> value : module.getValues()) {
            String valueName;
            RenderUtil.drawStringWithShadow("Exit", 50+RenderUtil.getStringWidth(module.getName()), 0, -1);
            RenderUtil.drawStringWithShadow(module.getName(),1,0,-1);
            valueName = value.getName();
            if (value.isHide) return;
            if (value instanceof BooleanValue) {
                RenderUtil.drawString(valueName, x, y, -1);
                RenderUtil.drawRectWithOutline(x + RenderUtil.getStringWidth(valueName) + 5,y + 3,5,10,((BooleanValue)value).getValue() ? Color.GREEN.getRGB() : Color.RED.getRGB(),-1);
                y += 15;
            }
            if (value instanceof ModeValue) {
                RenderUtil.drawString(valueName + ": ", x, y, -1);
                RenderUtil.drawString(((ModeValue)value).getValue(), x + RenderUtil.getStringWidth(valueName) + 5,y, -1);
                y += 15;
            }
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue) value;
                double min = numberValue.minValue;
                double max = numberValue.maxValue;
                double current = numberValue.getValue();
                double normalizedValue = (current - min) / (max - min);

                RenderUtil.drawString(valueName, x, y, -1);
                RenderUtil.drawRectWithOutline(x + RenderUtil.getStringWidth(valueName) + 5, y + 3, 100, 5, Color.GRAY.getRGB(), -1);

                // 进度
                RenderUtil.drawRect(x + RenderUtil.getStringWidth(valueName) + 5, y + 3, (int) (normalizedValue * 100), 5, Color.BLUE.getRGB());
                RenderUtil.drawString(String.valueOf(current), x + RenderUtil.getStringWidth(valueName) + 110, y, -1);
                y += 20;
            }
            if (value instanceof ColorValue) {
                ColorValue colorValue = (ColorValue) value;

                RenderUtil.drawString(valueName, x, y, -1);

                // 红色
                RenderUtil.drawString("R", x + 5, y + 15, Color.RED.getRGB());
                RenderUtil.drawRectWithOutline(x + 20, y + 18, 100, 5, Color.GRAY.getRGB(), -1);
                RenderUtil.drawRect(x + 20, y + 18, (int) (colorValue.getRed() / 255.0 * 100), 5, Color.RED.getRGB());

                // 绿色
                RenderUtil.drawString("G", x + 5, y + 30, Color.GREEN.getRGB());
                RenderUtil.drawRectWithOutline(x + 20, y + 33, 100, 5, Color.GRAY.getRGB(), -1);
                RenderUtil.drawRect(x + 20, y + 33, (int) (colorValue.getGreen() / 255.0 * 100), 5, Color.GREEN.getRGB());

                // 蓝色
                RenderUtil.drawString("B", x + 5, y + 45, Color.BLUE.getRGB());
                RenderUtil.drawRectWithOutline(x + 20, y + 48, 100, 5, Color.GRAY.getRGB(), -1);
                RenderUtil.drawRect(x + 20, y + 48, (int) (colorValue.getBlue() / 255.0 * 100), 5, Color.BLUE.getRGB());

                if (colorValue.getHasAlpha()) {
                    // 透明度
                    RenderUtil.drawString("A", x + 5, y + 60, -1);
                    RenderUtil.drawRectWithOutline(x + 20, y + 63, 100, 5, Color.GRAY.getRGB(), -1);
                    RenderUtil.drawRect(x + 20, y + 63, (int) (colorValue.getAlpha() / 255.0 * 100), 5, Color.WHITE.getRGB());
                }

                // 颜色预览
                RenderUtil.drawRectWithOutline(x + 130, y + 18, 50, 50, colorValue.getColor(), -1);
                y += 90;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int y = 10;
        int x = 0;
        String valueName;
        String Exit;
        valueName = module.getName();
        Exit = "Exit";
        if (HoveringUtil.isHovering(50+RenderUtil.getStringWidth(valueName),0,RenderUtil.getStringWidth(Exit),mc.fontRendererObj.FONT_HEIGHT,mouseX,mouseY)) mc.displayGuiScreen(new SimpleClickGUI());
        for (Value<?> value : module.getValues()) {
            if (value.isHide) return;
            if (value instanceof BooleanValue) {
                if (HoveringUtil.isHovering(x + RenderUtil.getStringWidth(valueName), y + 3, 20, 10, mouseX, mouseY)) {
                    ((BooleanValue) value).toggle();
                }
                y += 15;
            }
            if (value instanceof ModeValue) {
                if (HoveringUtil.isHovering(x + RenderUtil.getStringWidth(valueName + ": " + value.getValue()) + 5,y,20,10,mouseX,mouseY)) {
                    ((ModeValue)value).setNextValue();
                }
                y += 15;
            }
            if (value instanceof NumberValue) {
                NumberValue numberValue = (NumberValue) value;

                if (HoveringUtil.isHovering(x + RenderUtil.getStringWidth(valueName) + 5, y + 3, 100, 5, mouseX, mouseY)) {
                    float min = numberValue.minValue;
                    float max = numberValue.maxValue;

                    // 世界最强狼牙神的算法
                    float normalizedValue = (mouseX - (x + RenderUtil.getStringWidth(valueName) + 5)) / 100F;
                    float newValue = min + normalizedValue * (max - min);

                    // 防止傻逼弄负数
                    newValue = Math.min(max, Math.max(min, newValue));

                    numberValue.setValue(newValue);
                }
                y += 20;
            }
            if (value instanceof ColorValue) {
                ColorValue colorValue = (ColorValue) value;

                // 红色
                int min = Math.min(255, Math.max(0, (mouseX - (x + 20)) * 255 / 100));

                if (HoveringUtil.isHovering(x + 20, y + 18, 100, 5, mouseX, mouseY)) {
                    colorValue.setRed(min);
                }

                // 绿色
                if (HoveringUtil.isHovering(x + 20, y + 33, 100, 5, mouseX, mouseY)) {
                    colorValue.setGreen(min);
                }

                // 蓝色
                if (HoveringUtil.isHovering(x + 20, y + 48, 100, 5, mouseX, mouseY)) {
                    colorValue.setBlue(min);
                }

                if (((ColorValue) value).getHasAlpha()) {
                    // 透明度
                    if (HoveringUtil.isHovering(x + 20, y + 63, 100, 5, mouseX, mouseY)) {
                        colorValue.setAlpha(min);
                    }
                }
                y += 90;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Client.getInstance().getConfigManager().saveConfig("Module.json");
        super.onGuiClosed();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}