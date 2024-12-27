package net.minecraft.client.gui;

import cn.langya.Client;
import cn.langya.ui.Element;
import cn.langya.utils.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.*;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * @author Mojang, LangYa466
 * 聊天窗口类，继承自GuiScreen
 */
public class GuiChat extends GuiScreen {
    private String historyBuffer = ""; // 历史记录缓冲区
    private int sentHistoryCursor = -1; // 发送历史记录光标
    private boolean playerNamesFound, waitingOnAutocomplete; // 玩家名称发现与自动完成功能状态
    private int autocompleteIndex; // 自动完成索引
    private final List<String> foundPlayerNames = Lists.newArrayList(); // 已找到的玩家名称列表
    protected GuiTextField inputField; // 输入框
    private String defaultInputFieldText = ""; // 默认输入框文本

    public GuiChat() {} // 无参构造函数

    public GuiChat(String defaultText) {
        // 带默认文本的构造函数
        this.defaultInputFieldText = defaultText;
    }

    public void initGui() {
        // 初始化GUI
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
    }

    public void onGuiClosed() {
        // GUI关闭时的处理
        Client.getInstance().getConfigManager().saveConfig("Element");
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    public void updateScreen() {
        // 更新屏幕
        this.inputField.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        // 处理键盘输入
        this.waitingOnAutocomplete = false;

        if (keyCode == 15) {
            autocompletePlayerNames();
        } else if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        } else if (keyCode == 28 || keyCode == 156) {
            String s = this.inputField.getText().trim();
            if (!s.isEmpty()) {
                sendChatMessage(s);
            }
            this.mc.displayGuiScreen(null);
        } else if (keyCode == 200) {
            getSentHistory(-1);
        } else if (keyCode == 208) {
            getSentHistory(1);
        } else {
            this.inputField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    public void handleMouseInput() throws IOException {
        // 处理鼠标输入
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            i = (i > 1) ? 1 : Math.max(i, -1);
            if (!isShiftKeyDown()) i *= 7;
            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // 处理鼠标点击事件
        if (mouseButton == 0) {
            for (Element element : Client.getInstance().getElementManager().getElementMap().values()) {
                element.setHovering(element.checkHover(mouseX, mouseY));
            }

            IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
            if (handleComponentClick(ichatcomponent)) return;
        }
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        // 处理鼠标释放事件
        for (Element element : Client.getInstance().getElementManager().getElementMap().values()) {
            element.setHovering(false);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void setText(String newChatText, boolean shouldOverwrite) {
        // 设置文本
        if (shouldOverwrite) {
            this.inputField.setText(newChatText);
        } else {
            this.inputField.writeText(newChatText);
        }
    }

    public void autocompletePlayerNames() {
        // 自动完成玩家名称
        if (this.playerNamesFound) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        } else {
            int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String s = this.inputField.getText().substring(i).toLowerCase();
            String s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            sendAutocompleteRequest(s1, s);

            if (this.foundPlayerNames.isEmpty()) return;

            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }

        if (this.foundPlayerNames.size() > 1) {
            StringBuilder stringbuilder = new StringBuilder();
            for (String s2 : this.foundPlayerNames) {
                if (stringbuilder.length() > 0) stringbuilder.append(", ");
                stringbuilder.append(s2);
            }
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }

        this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
        // 发送自动完成请求
        if (!p_146405_1_.isEmpty()) {
            BlockPos blockpos = null;
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = this.mc.objectMouseOver.getBlockPos();
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }

    public void getSentHistory(int msgPos) {
        // 获取发送历史
        int i = this.sentHistoryCursor + msgPos;
        int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);

        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            } else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }
                this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 绘制屏幕
        for (Element element : Client.getInstance().getElementManager().getElementMap().values()) {
            if (element.isHovering()) {
                if (Client.getInstance().getModuleManager().getModuleMap().get(element.getName()).isEnable()) {
                    RenderUtil.drawOutline((int) element.getX() - 4, (int) element.getY() - 4, (int) element.getWidth() + 8, (int) element.getHeight() + 8, -1);
                    element.setXY(mouseX - (element.getWidth() / 2), mouseY - (element.getHeight() / 2));
                }
            }
        }

        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void onAutocompleteResponse(String[] p_146406_1_) {
        // 处理自动完成响应
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();

            for (String s : p_146406_1_) {
                if (!s.isEmpty()) {
                    this.foundPlayerNames.add(s);
                }
            }

            String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            String s2 = StringUtils.getCommonPrefix(p_146406_1_);

            if (!s2.isEmpty() && !s1.equalsIgnoreCase(s2)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s2);
            } else if (!this.foundPlayerNames.isEmpty()) {
                this.playerNamesFound = true;
                autocompletePlayerNames();
            }
        }
    }

    public boolean doesGuiPauseGame() {
        // 检查GUI是否暂停游戏
        return false;
    }
}
