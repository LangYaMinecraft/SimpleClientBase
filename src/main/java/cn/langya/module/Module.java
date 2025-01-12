package cn.langya.module;

import cn.langya.Client;
import cn.langya.Wrapper;
import cn.langya.utils.ChatUtil;
import cn.langya.value.Value;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa
 * @since 2024/11/16 03:40
 * 模块类，表示一个可启用的模块。包含模块的基本信息和操作方法。
 */
@Getter
@Setter
public class Module implements Wrapper {
    private final String name;
    private final Category category;
    private String suffix = "";
    private String description = "";
    private boolean enable;
    private List<Value<?>> values;
    private int keyCode = 114514;

    /**
     * 构造函数，初始化模块的名称和类别。
     * @param category 模块的类别
     */
    public Module(Category category) {
        this.name = this.getClass().getSimpleName();
        this.category = category;
        this.values = new ArrayList<>();
    }

    /**
     * 设置模块的启用状态，并根据状态执行相应的操作。
     * @param enable 模块是否启用
     */
    public void setEnable(boolean enable) {
        // 赋值需要.this
        this.enable = enable;
        // 获取值不需要.this
        if (enable) {
            onEnable();
            ChatUtil.info(this.name + EnumChatFormatting.GREEN + " Enabled.");
        } else {
            onDisable();
            ChatUtil.info(this.name + EnumChatFormatting.RED + " Disable.");
        }

        Client.getInstance().getEventManager().registerModule(enable,this);

        if (mc.thePlayer != null) mc.theWorld.playSound(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "random.click", 0.5F, enable ? 0.6F : 0.5F, false);
    }

    /**
     * 启用模块时调用的方法。
     */
    public void onEnable() { }

    /**
     * 禁用模块时调用的方法。
     */
    public void onDisable() { }

    /**
     * 切换模块的启用状态。
     */
    public void toggle() {
        setEnable(!isEnable());
    }
}
