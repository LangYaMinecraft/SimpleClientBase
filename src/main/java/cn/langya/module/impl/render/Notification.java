package cn.langya.module.impl.render;

import cn.langya.Client;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventRender2D;
import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.ui.font.FontManager;
import cn.langya.ui.notification.NotificationType;
import cn.langya.utils.RenderUtil;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.util.List;

/**
 * @author LangYa466
 * @since 2/27/2025
 */
public class Notification extends Module {
    /**
     * 构造函数，初始化模块的名称和类别。
     */
    public Notification() {
        super(Category.Render);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        List<cn.langya.ui.notification.Notification> notifications = Client.getInstance().getNotificationManager().getActiveNotifications();
        if (notifications.isEmpty()) return;

        FontRenderer fontRenderer = FontManager.vivo(20);
        int screenWidth = event.getScaledresolution().getScaledWidth();
        int screenHeight = event.getScaledresolution().getScaledHeight();
        int yOffset = screenHeight - 10; // 从底部开始显示通知

        for (cn.langya.ui.notification.Notification notification : notifications) {
            String message = notification.getMessage();
            NotificationType type = notification.getType();
            int width = fontRenderer.getStringWidth(message) + 20;
            int height = 20;

            // 计算颜色
            Color bgColor;
            switch (type) {
                case SUCCESS: bgColor = new Color(0, 200, 0, 150); break;
                case WARNING: bgColor = new Color(255, 165, 0, 150); break;
                case ERROR:   bgColor = new Color(200, 0, 0, 150); break;
                default:      bgColor = new Color(0, 0, 0, 150); break;
            }

            // 背景
            RenderUtil.drawRect2(screenWidth - width - 5, yOffset - height, screenWidth - 5, yOffset, bgColor.getRGB());

            // 绘制文本
            fontRenderer.drawStringWithShadow(message, screenWidth - width, yOffset - height + 6, Color.WHITE.getRGB());

            // 递增偏移量
            yOffset -= (height + 5);
        }
    }
}
