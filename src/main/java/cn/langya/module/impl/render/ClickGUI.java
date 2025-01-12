package cn.langya.module.impl.render;

import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.ui.clickgui.SimpleClickGUI;
import org.lwjgl.input.Keyboard;

/**
 * @author LangYa466
 * @since 2025/1/12
 */
public class ClickGUI extends Module {
    /**
     * 构造函数，初始化模块的名称和类别。
     */
    public ClickGUI() {
        super(Category.Render);
        setKeyCode(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new SimpleClickGUI());
        setEnable(false);
        super.onEnable();
    }
}
