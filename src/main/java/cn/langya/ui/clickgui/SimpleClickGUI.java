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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Visual Studio Code 风格的 ClickGUI（含 Value 高亮、Number滑动条、可视化 ColorPicker、模块列表平滑滚动和模块 toggle）
 *
 * @author LangYa466
 * @since 2/15/2025
 */
@Getter
@Setter
public class SimpleClickGUI extends GuiScreen {
    private DragWindow dragWindow;
    // 目标滚动值，由鼠标滚轮设置
    private int scrollOffset = 0;
    // 当前滚动值，用于平滑动画
    private float currentScrollOffset = 0.0f;
    private static final int MAX_SCROLL = 400; // 根据内容调整
    private static final int MIN_SCROLL = 0;

    // 窗口尺寸与样式（VSCode 风格）
    private static final int GUI_WIDTH = 350;
    private static final int GUI_HEIGHT = 450;
    private static final Color BG_COLOR = new Color(30, 30, 30, 230);
    private static final Color ACTIVE_COLOR = new Color(40, 160, 255);
    private static final int TITLE_HEIGHT = 25;
    private static final int PADDING = 10;

    // VS 主题关键字高亮颜色
    private static final Color KEYWORD_COLOR_BOOLEAN = new Color(86,156,214);
    private static final Color KEYWORD_COLOR_NUMBER = new Color(181,206,168);
    private static final Color KEYWORD_COLOR_MODE   = new Color(220,220,170);
    private static final Color KEYWORD_COLOR_COLOR  = new Color(255,184,184);

    // 主模块列表
    private final List<Module> modules;
    // 记录模块列表点击区域
    private List<ModuleBox> moduleBoxes = new ArrayList<>();
    // 记录各 Value 显示的点击区域（包括滑动条区域）
    private List<ValueBox> valueBoxes = new ArrayList<>();

    // NumberValue 滑动编辑状态
    private NumberValue editingNumberValue = null;
    // （可选：如果需要文本输入模式）当前文本输入内容
    private String currentNumberInput = "";

    // 当前激活的 ColorValue，用于显示可视化 ColorPicker
    private ColorValue activeColorValue = null;

    // ColorPicker 参数
    private static final int COLOR_PICKER_WIDTH = 100;
    private static int pickerX, pickerY;

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
        // drawDefaultBackground();

        // 拖拽处理
        if (dragWindow.isDragging()) {
            dragWindow.drag(mouseX, mouseY);
        }
        int startX = dragWindow.getX();
        int startY = dragWindow.getY();

        // 绘制窗口背景与标题栏（VSCode 风格）
        RenderUtil.drawRect(startX, startY, GUI_WIDTH, GUI_HEIGHT, BG_COLOR.getRGB());
        RenderUtil.drawRect(startX, startY, GUI_WIDTH, TITLE_HEIGHT, new Color(45, 45, 45, 255).getRGB());
        RenderUtil.drawString("Simple Client Base", startX + PADDING, startY + 7, Color.WHITE.getRGB());

        int codeY = startY + TITLE_HEIGHT + PADDING;
        // 绘制 package 行
        RenderUtil.drawString("package cn.langya.client;", startX + PADDING, codeY, new Color(86,156,214).getRGB());
        codeY += 20;
        // 空行
        RenderUtil.drawString("", startX + PADDING, codeY, Color.WHITE.getRGB());
        codeY += 20;
        // 已删除 AllModules 显示，直接开始模块列表

        // 绘制模块列表（模块名称在一行内），不受滚动影响
        moduleBoxes.clear();
        int modulesLineY = codeY;
        RenderUtil.drawString("(", startX + PADDING, modulesLineY, new Color(200,200,200).getRGB());
        int currentX = startX + PADDING + RenderUtil.getStringWidth("(");
        int index = 1;
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            String moduleName = module.getName();
            int color = module.isEnable() ? ACTIVE_COLOR.getRGB() : new Color(180, 180, 180).getRGB();
            RenderUtil.drawString(moduleName, currentX, modulesLineY, color);
            int textWidth = RenderUtil.getStringWidth(moduleName);
            moduleBoxes.add(new ModuleBox(module, currentX, modulesLineY, textWidth, 15));
            currentX += textWidth;
            if (i < modules.size() - index) {
                String comma = ", ";
                RenderUtil.drawString(comma, currentX, modulesLineY, new Color(200,200,200).getRGB());
                currentX += RenderUtil.getStringWidth(comma);
            }
        }
        RenderUtil.drawString(")", currentX, modulesLineY, new Color(200,200,200).getRGB());
        // 模块列表不随滚动改变
        codeY += 30;

        // 平滑滚动：currentScrollOffset 逐渐接近 scrollOffset
        currentScrollOffset += (scrollOffset - currentScrollOffset) * 0.1f;
        int contentOffset = (int) currentScrollOffset;

        // 绘制每个模块的 Value 显示区域，整体受滚动影响
        valueBoxes.clear();
        int contentY = codeY + contentOffset;
        List<Runnable> waitDraw = new ArrayList<>();
        for (Module module : modules) {
            if (module.getValues() != null && !module.getValues().isEmpty()) {
                // 绘制模块设置注释行
                String comment = "// " + module.getName() + " settings";
                RenderUtil.drawString(comment, startX + PADDING, contentY, new Color(87,166,74).getRGB());
                contentY += 20;
                // 在设置注释下增加一个模块 toggle 行
                String toggleText = "isEnable = " + (module.isEnable() ? "true" : "false");
                RenderUtil.drawString(toggleText, startX + PADDING + 20, contentY, KEYWORD_COLOR_BOOLEAN.getRGB());
                valueBoxes.add(new ValueBox(module, null, startX + PADDING + 20, contentY, RenderUtil.getStringWidth(toggleText), 15));
                contentY += 20;

                for (Value<?> value : module.getValues()) {
                    if (value.isHide) continue;
                    // 根据不同 Value 类型绘制
                    if (value instanceof BooleanValue) {
                        String keyPart = value.getName() + " = ";
                        String valPart = ((BooleanValue) value).getValue() ? "true" : "false";
                        RenderUtil.drawString(keyPart, startX + PADDING + 20, contentY, Color.WHITE.getRGB());
                        RenderUtil.drawString(valPart, startX + PADDING + 20 + RenderUtil.getStringWidth(keyPart), contentY, KEYWORD_COLOR_BOOLEAN.getRGB());
                        int boxWidth = RenderUtil.getStringWidth(keyPart + valPart);
                        valueBoxes.add(new ValueBox(module, value, startX + PADDING + 20, contentY, boxWidth, 15));
                        contentY += 20;
                    } else if (value instanceof ModeValue) {
                        String keyPart = value.getName() + " = ";
                        String valPart = ((ModeValue) value).getValue();
                        RenderUtil.drawString(keyPart, startX + PADDING + 20, contentY, Color.WHITE.getRGB());
                        RenderUtil.drawString(valPart, startX + PADDING + 20 + RenderUtil.getStringWidth(keyPart), contentY, KEYWORD_COLOR_MODE.getRGB());
                        int boxWidth = RenderUtil.getStringWidth(keyPart + valPart);
                        valueBoxes.add(new ValueBox(module, value, startX + PADDING + 20, contentY, boxWidth, 15));
                        contentY += 20;
                    } else if (value instanceof NumberValue) {
                        NumberValue numberValue = (NumberValue) value;
                        String keyPart = numberValue.getName() + " = ";
                        RenderUtil.drawString(keyPart, startX + PADDING + 20, contentY, Color.WHITE.getRGB());
                        int keyWidth = RenderUtil.getStringWidth(keyPart);
                        int sliderX = startX + PADDING + 20 + keyWidth;
                        int sliderWidth = 80;
                        double norm = (numberValue.getValue() - numberValue.minValue) / (numberValue.maxValue - numberValue.minValue);
                        // 绘制滑动条背景
                        RenderUtil.drawRect(sliderX, contentY + 3, sliderWidth, 10, new Color(60,60,60,200).getRGB());
                        // 绘制滑动条填充
                        RenderUtil.drawRect(sliderX, contentY + 3, (int)(sliderWidth * norm), 10, KEYWORD_COLOR_NUMBER.getRGB());
                        // 绘制当前数值
                        RenderUtil.drawString(String.format("%.2f", numberValue.getValue()), sliderX + sliderWidth + 5, contentY, KEYWORD_COLOR_NUMBER.getRGB());
                        // 记录滑动区域用于交互
                        valueBoxes.add(new ValueBox(module, value, sliderX, contentY, sliderWidth, 10));
                        contentY += 20;
                    } else if (value instanceof ColorValue) {
                        ColorValue colorValue = (ColorValue) value;
                        String keyPart = value.getName() + " = ";
                        String hex = String.format("#%02X%02X%02X", colorValue.getRed(), colorValue.getGreen(), colorValue.getBlue());
                        RenderUtil.drawString(keyPart, startX + PADDING + 20, contentY, Color.WHITE.getRGB());
                        RenderUtil.drawString(hex, startX + PADDING + 20 + RenderUtil.getStringWidth(keyPart), contentY, KEYWORD_COLOR_COLOR.getRGB());
                        int boxWidth = RenderUtil.getStringWidth(keyPart + hex);
                        valueBoxes.add(new ValueBox(module, value, startX + PADDING + 20, contentY, boxWidth, 15));
                        contentY += 20;

                        // 防止colorpicker被覆盖 我的写法waitDraw有点啥比
                        int finalContentY = contentY;
                        // 直接set会报错 给我脑子干烧了 这个while是gpt写的
                        // IndexOutOfBoundsException很喜欢你这个小朋友哦
                        while (waitDraw.size() <= index) {
                            waitDraw.add(null);
                        }

                        waitDraw.set(index, () -> {
                            if (activeColorValue != null) {
                                pickerX = startX + PADDING + 25 + RenderUtil.getStringWidth(hex);
                                pickerY = finalContentY + 3;
                                int pickerW = 120;
                                int pickerH = activeColorValue.getHasAlpha() ? 60 : 45;
                                RenderUtil.drawRect(pickerX - 15, finalContentY, pickerW, pickerH, new Color(50, 50, 50, 230).getRGB());

                                // Red 滑条
                                RenderUtil.drawString("R", pickerX - 13, finalContentY - 3, Color.WHITE.getRGB());
                                RenderUtil.drawColorSlider(pickerX, finalContentY, COLOR_PICKER_WIDTH, activeColorValue.getRed() / 255.0, 1.0, Color.RED);
                                if (Mouse.isButtonDown(0) && HoveringUtil.isHovering(pickerX, finalContentY + 2, COLOR_PICKER_WIDTH, 5, mouseX, mouseY)) {
                                    // 更新红色值
                                    int newRed = (int) ((mouseX - pickerX) / (float) COLOR_PICKER_WIDTH * 255);
                                    newRed = Math.min(Math.max(newRed, 0), 255);
                                    activeColorValue.setRed(newRed);
                                }

                                // Green 滑条
                                RenderUtil.drawString("G", pickerX - 13, finalContentY + 12, Color.WHITE.getRGB());
                                RenderUtil.drawColorSlider(pickerX, finalContentY + 17, COLOR_PICKER_WIDTH, activeColorValue.getGreen() / 255.0, 1.0, Color.GREEN);
                                if (Mouse.isButtonDown(0) && HoveringUtil.isHovering(pickerX, finalContentY + 17, COLOR_PICKER_WIDTH, 5, mouseX, mouseY)) {
                                    // 更新绿色值
                                    int newGreen = (int) ((mouseX - pickerX) / (float) COLOR_PICKER_WIDTH * 255);
                                    newGreen = Math.min(Math.max(newGreen, 0), 255);
                                    activeColorValue.setGreen(newGreen);
                                }

                                // Blue 滑条
                                RenderUtil.drawString("B", pickerX - 13, finalContentY + 27, Color.WHITE.getRGB());
                                RenderUtil.drawColorSlider(pickerX, finalContentY + 32, COLOR_PICKER_WIDTH, activeColorValue.getBlue() / 255.0, 1.0, Color.BLUE);
                                if (Mouse.isButtonDown(0) && HoveringUtil.isHovering(pickerX, finalContentY + 32, COLOR_PICKER_WIDTH, 5, mouseX, mouseY)) {
                                    // 更新蓝色值
                                    int newBlue = (int) ((mouseX - pickerX) / (float) COLOR_PICKER_WIDTH * 255);
                                    newBlue = Math.min(Math.max(newBlue, 0), 255);
                                    activeColorValue.setBlue(newBlue);
                                }

                                // Alpha 滑条（如果支持）
                                if (activeColorValue.getHasAlpha()) {
                                    RenderUtil.drawString("A", pickerX - 13, finalContentY + 43, Color.WHITE.getRGB());
                                    RenderUtil.drawColorSlider(pickerX, finalContentY + 47, COLOR_PICKER_WIDTH, activeColorValue.getAlpha() / 255.0, 1.0, Color.WHITE);
                                    if (Mouse.isButtonDown(0) && HoveringUtil.isHovering(pickerX, finalContentY + 47, COLOR_PICKER_WIDTH, 5, mouseX, mouseY)) {
                                        // 更新透明度值
                                        int newAlpha = (int) ((mouseX - pickerX) / (float) COLOR_PICKER_WIDTH * 255);
                                        newAlpha = Math.min(Math.max(newAlpha, 0), 255);
                                        activeColorValue.setAlpha(newAlpha);
                                    }
                                }
                            } else {
                                if (waitDraw.get(index) != null) waitDraw.set(index, null);
                            }
                        });
                    }
                }

                contentY += 10;
            }
            if (waitDraw.size() > index && waitDraw.get(index) != null) {
                waitDraw.get(index).run();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // 若存在 ColorPicker，点击外部则关闭
        if (activeColorValue != null) {
            int pickerW = 150;
            int pickerH = activeColorValue.getHasAlpha() ? 60 : 45;
            if (!HoveringUtil.isHovering(pickerX, pickerY, pickerW, pickerH, mouseX, mouseY)) {
                activeColorValue = null;
            } else {
                super.mouseClicked(mouseX, mouseY, mouseButton);
                return;
            }
        }

        // 检测标题栏拖拽
        if (dragWindow.isHoveringHeader(mouseX, mouseY, TITLE_HEIGHT)) {
            dragWindow.startDrag(mouseX, mouseY);
        }
        // 检测模块名称点击（切换模块开关）
        for (ModuleBox box : moduleBoxes) {
            if (HoveringUtil.isHovering(box.x, box.y, box.width, box.height, mouseX, mouseY)) {
                box.module.toggle();
                break;
            }
        }
        // 检测各 Value 点击（交互操作）
        for (ValueBox box : valueBoxes) {
            if (HoveringUtil.isHovering(box.x, box.y, box.width, box.height, mouseX, mouseY)) {
                // 如果是模块 toggle 行（Value 为 null），则切换该模块
                if (box.value == null) {
                    box.module.toggle();
                } else if (box.value instanceof BooleanValue) {
                    ((BooleanValue) box.value).toggle();
                } else if (box.value instanceof ModeValue) {
                    ((ModeValue) box.value).setNextValue();
                } else if (box.value instanceof NumberValue) {
                    // 进入 NumberValue 滑动编辑模式
                    editingNumberValue = (NumberValue) box.value;
                } else if (box.value instanceof ColorValue) {
                    activeColorValue = (ColorValue) box.value;
                }
                break;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        // 结束 NumberValue 滑动编辑模式
        editingNumberValue = null;
        dragWindow.stopDrag();
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // 处理 NumberValue 滑动条拖动
        if (editingNumberValue != null && editingNumberValue instanceof NumberValue) {
            // 遍历 valueBoxes 找到对应的滑动区域
            for (ValueBox box : valueBoxes) {
                if (box.value == editingNumberValue) {
                    double norm = (mouseX - box.x) / (double) box.width;
                    norm = Math.max(0.0, Math.min(1.0, norm));
                    double newValue = editingNumberValue.minValue + norm * (editingNumberValue.maxValue - editingNumberValue.minValue);
                    editingNumberValue.setValue((float) newValue);
                    break;
                }
            }
        }
        // ColorPicker 拖动已在 drawScreen 中处理
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Integer.signum(Mouse.getDWheel());
        if (wheel != 0) {
            scrollOffset -= wheel * 15;
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

    // （可选：如果需要文本输入模式，处理 NumberValue 文本输入）
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (editingNumberValue != null) {
            if (keyCode == Keyboard.KEY_RETURN) { // 回车确认
                try {
                    double newValue = Double.parseDouble(currentNumberInput);
                    editingNumberValue.setValue((float) newValue);
                } catch (NumberFormatException e) {
                    // 输入错误时处理
                }
                editingNumberValue = null;
            } else if (keyCode == Keyboard.KEY_BACK) {
                if (!currentNumberInput.isEmpty()) {
                    currentNumberInput = currentNumberInput.substring(0, currentNumberInput.length() - 1);
                }
            } else {
                currentNumberInput += typedChar;
            }
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    // 内部类：记录模块名称点击区域
    private static class ModuleBox {
        Module module;
        int x, y, width, height;
        public ModuleBox(Module module, int x, int y, int width, int height) {
            this.module = module;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    // 内部类：记录 Value 显示（及滑动条）区域
    private static class ValueBox {
        Module module;
        Value<?> value;
        int x, y, width, height;
        public ValueBox(Module module, Value<?> value, int x, int y, int width, int height) {
            this.module = module;
            this.value = value;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
