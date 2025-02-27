package cn.langya.ui.notification;

import cn.langya.utils.TimerUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 通知类，表示一条消息通知
 */
@Getter
@Setter
public class Notification {
    private final String message;
    private final NotificationType type;
    // 通知显示的持续时间（单位：毫秒）
    private final int displayDuration;
    // 使用 TimerUtil 来跟踪通知的显示时长
    private final TimerUtil timer;

    /**
     * 构造通知
     * @param message 提示信息
     * @param type 通知类型
     * @param displayDuration 显示时长（毫秒）
     */
    public Notification(String message, NotificationType type, int displayDuration) {
        this.message = message;
        this.type = type;
        this.displayDuration = displayDuration;
        this.timer = new TimerUtil();
        this.timer.reset();
    }
    /**
     * 判断通知是否已过期
     * @return true 表示通知已显示超过规定时长，可从列表中移除
     */
    public boolean isExpired() {
        return timer.hasReached(displayDuration);
    }
}
