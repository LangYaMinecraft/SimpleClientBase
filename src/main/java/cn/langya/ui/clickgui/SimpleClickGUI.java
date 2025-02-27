package cn.langya.ui.clickgui;

import cn.langya.Client;
import cn.langya.module.Module;
import cn.langya.utils.HoveringUtil;
import cn.langya.utils.RenderUtil;
import cn.langya.value.Value;
import cn.langya.value.impl.BooleanValue;
import cn.langya.value.impl.ColorValue;
import cn.langya.value.impl.ModeValue;
import cn.langya.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LangYa466
 * @since 2/15/2025
 */
@Getter
@Setter
public class SimpleClickGUI extends GuiScreen {
    private DragWindow dragWindow;
    private int scrollOffset = 0;
    private static final int MAX_SCROLL = 200;
    private static final int MIN_SCROLL = 0;
    private static final int MODULE_HEIGHT = 20;
    private static final int PADDING = 10;
    private static final int GUI_WIDTH = 250;
    private static final int GUI_HEIGHT = 300;
    private static final Color BG_COLOR = new Color(20, 20, 20, 180);
    private static final Color ACTIVE_COLOR = new Color(40, 160, 255);

    private final List<Module> modules;
    // 为 null 表示显示主模块列表，不为 null 则显示对应模块的设置界面
    private Module selectedModule = null;

    public SimpleClickGUI() {
        this.modules = Client.getInstance().getModuleManager().getModuleMap().values().stream()
                .sorted((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()))
                .collect(Collectors.toList());
        // 初始位置设置为 (100,100)，后续 initGui() 会居中
        this.dragWindow = new DragWindow(100, 100, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void initGui() {
        // 居中窗口
        this.dragWindow.setX((this.width - GUI_WIDTH) / 2);
        this.dragWindow.setY((this.height - GUI_HEIGHT) / 2);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // 若正在拖拽，则更新窗口位置
        if (dragWindow.isDragging()) {
            dragWindow.drag(mouseX, mouseY);
        }

        int startX = dragWindow.getX();
        int startY = dragWindow.getY();

        if (selectedModule == null) {
            // 绘制主模块列表界面
            RenderUtil.drawRect(startX, startY, GUI_WIDTH, GUI_HEIGHT, BG_COLOR.getRGB());
            // 绘制顶部作为拖拽区域的标题栏
            RenderUtil.drawRect(startX, startY, GUI_WIDTH, 20, new Color(50, 50, 50, 200).getRGB());
            RenderUtil.drawString("ClickGUI", startX + 10, startY + 5, Color.WHITE.getRGB());

            int y = startY + 20 + PADDING;
            int x = startX + PADDING;
            for (Module module : modules) {
                int displayY = y - scrollOffset;
                if (displayY >= startY + 20 && displayY <= startY + GUI_HEIGHT - MODULE_HEIGHT) {
                    Color rectColor = module.isEnable() ? ACTIVE_COLOR : new Color(60, 60, 60, 180);
                    RenderUtil.drawRect(x, displayY, GUI_WIDTH - PADDING * 2, MODULE_HEIGHT, rectColor.getRGB());
                    RenderUtil.drawString(module.getName(), x + 8, displayY + 6, Color.WHITE.getRGB());
                    if (!module.getValues().isEmpty()) {
                        RenderUtil.drawString("Settings", x + GUI_WIDTH - PADDING * 2 - 60, displayY + 6, Color.LIGHT_GRAY.getRGB());
                    }
                }
                y += MODULE_HEIGHT + 5;
            }
        } else {
            // 绘制模块设置界面
            int winW = GUI_WIDTH;
            int winH = GUI_HEIGHT;
            RenderUtil.drawRect(startX, startY, winW, winH, new Color(30, 30, 30, 200).getRGB());
            RenderUtil.drawRect(startX, startY, winW, winH, new Color(80, 80, 80, 255).getRGB());
            // 绘制标题及退出按钮
            RenderUtil.drawStringWithShadow(selectedModule.getName(), startX + 10, startY + 10, -1);
            RenderUtil.drawStringWithShadow("Exit", startX + winW - 40, startY + 10, Color.RED.getRGB());
            
            int x = startX + 10;
            int y = startY + 30;
            for (Value<?> value : selectedModule.getValues()) {
                if (value.isHide) continue;

                if (value instanceof BooleanValue) {
                    RenderUtil.drawString(value.getName(), x, y, -1);
                    RenderUtil.drawRect(x + 100, y - 2, 40, 12, ((BooleanValue) value).getValue() ? Color.GREEN.getRGB() : Color.RED.getRGB());
                    y += 20;
                } else if (value instanceof ModeValue) {
                    RenderUtil.drawString(value.getName() + ": ", x, y, -1);
                    RenderUtil.drawString(((ModeValue) value).getValue(), x + 100, y, Color.CYAN.getRGB());
                    y += 20;
                } else if (value instanceof NumberValue) {
                    NumberValue numberValue = (NumberValue) value;
                    double min = numberValue.minValue;
                    double max = numberValue.maxValue;
                    double current = numberValue.getValue();
                    double normalizedValue = (current - min) / (max - min);
                    RenderUtil.drawString(value.getName(), x, y, -1);
                    RenderUtil.drawColorSlider(x + 100, y + 3, 80, normalizedValue, 1.0, Color.BLUE);
                    RenderUtil.drawString(String.format("%.2f", current), x + 190, y, -1);
                    y += 25;
                } else if (value instanceof ColorValue) {
                    ColorValue colorValue = (ColorValue) value;
                    RenderUtil.drawString(value.getName(), x, y, -1);
                    RenderUtil.drawColorSlider(x + 50, y + 15, 100, colorValue.getRed() / 255.0, 1.0, Color.RED);
                    RenderUtil.drawColorSlider(x + 50, y + 30, 100, colorValue.getGreen() / 255.0, 1.0, Color.GREEN);
                    RenderUtil.drawColorSlider(x + 50, y + 45, 100, colorValue.getBlue() / 255.0, 1.0, Color.BLUE);
                    if (colorValue.getHasAlpha()) {
                        RenderUtil.drawColorSlider(x + 50, y + 60, 100, colorValue.getAlpha() / 255.0, 1.0, Color.WHITE);
                    }
                    RenderUtil.drawRect(x + 160, y + 15, 40, 40, colorValue.getColor());
                    y += 80;
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int startX = dragWindow.getX();
        int startY = dragWindow.getY();

        // 检测是否在窗口标题区域（高度 20）点击，若是则开始拖拽
        if (dragWindow.isHoveringHeader(mouseX, mouseY, 20)) {
            dragWindow.startDrag(mouseX, mouseY);
        }

        if (selectedModule == null) {
            // 主界面：处理模块点击和设置按钮
            int y = startY + 20 + PADDING;
            int x = startX + PADDING;
            for (Module module : modules) {
                int displayY = y - scrollOffset;
                if (displayY >= startY + 20 && displayY <= startY + GUI_HEIGHT - MODULE_HEIGHT) {
                    if (HoveringUtil.isHovering(x, displayY, GUI_WIDTH - PADDING * 2, MODULE_HEIGHT, mouseX, mouseY)) {
                        // 若点击在设置按钮区域则进入设置视图
                        if (!module.getValues().isEmpty() &&
                                HoveringUtil.isHovering(x + GUI_WIDTH - PADDING * 2 - 60, displayY, 50, MODULE_HEIGHT, mouseX, mouseY)) {
                            selectedModule = module;
                            break;
                        } else {
                            module.toggle();
                        }
                    }
                }
                y += MODULE_HEIGHT + 5;
            }
        } else {
            // 设置界面：先检测退出按钮
            if (HoveringUtil.isHovering(startX + GUI_WIDTH - 40, startY + 10, 30, 10, mouseX, mouseY)) {
                selectedModule = null;
                return;
            }
            int x = startX + 10;
            int y = startY + 30;
            for (Value<?> value : selectedModule.getValues()) {
                if (value.isHide) continue;
                if (value instanceof BooleanValue) {
                    if (HoveringUtil.isHovering(x + 100, y - 2, 40, 12, mouseX, mouseY)) {
                        ((BooleanValue) value).toggle();
                    }
                    y += 20;
                } else if (value instanceof ModeValue) {
                    if (HoveringUtil.isHovering(x + 100, y, 80, 10, mouseX, mouseY)) {
                        ((ModeValue) value).setNextValue();
                    }
                    y += 20;
                } else if (value instanceof NumberValue) {
                    NumberValue numberValue = (NumberValue) value;
                    if (HoveringUtil.isHovering(x + 100, y + 3, 80, 5, mouseX, mouseY)) {
                        double min = numberValue.minValue;
                        double max = numberValue.maxValue;
                        double normalizedValue = (mouseX - (x + 100)) / 80.0;
                        double newValue = min + normalizedValue * (max - min);
                        numberValue.setValue((float) Math.min(max, Math.max(min, newValue)));
                    }
                    y += 25;
                } else if (value instanceof ColorValue) {
                    ColorValue colorValue = (ColorValue) value;
                    if (HoveringUtil.isHovering(x + 50, y + 15, 100, 5, mouseX, mouseY)) {
                        colorValue.setRed((mouseX - x - 50) * 255 / 100);
                    }
                    if (HoveringUtil.isHovering(x + 50, y + 30, 100, 5, mouseX, mouseY)) {
                        colorValue.setGreen((mouseX - x - 50) * 255 / 100);
                    }
                    if (HoveringUtil.isHovering(x + 50, y + 45, 100, 5, mouseX, mouseY)) {
                        colorValue.setBlue((mouseX - x - 50) * 255 / 100);
                    }
                    if (colorValue.getHasAlpha() && HoveringUtil.isHovering(x + 50, y + 60, 100, 5, mouseX, mouseY)) {
                        colorValue.setAlpha((mouseX - x - 50) * 255 / 100);
                    }
                    y += 80;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragWindow.stopDrag();
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Integer.signum(Mouse.getDWheel());
        if (wheel != 0 && selectedModule == null) {
            scrollOffset -= wheel * (MODULE_HEIGHT + 5);
            scrollOffset = Math.max(MIN_SCROLL, Math.min(scrollOffset, MAX_SCROLL));
        }
    }

    @Override
    public void onGuiClosed() {
        Client.getInstance().getConfigManager().saveConfig("Module.json");
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
