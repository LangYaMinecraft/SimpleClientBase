package cn.langya.ui.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知管理器，管理所有通知的添加、清理和获取操作
 */
public class NotificationManager {
    // 存储当前的通知列表
    private final List<Notification> notifications = new ArrayList<>();

    /**
     * 添加新的通知
     * @param notification 要添加的通知
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void addNotification(String message, NotificationType type) {
        notifications.add(new Notification(message,type,1500));
    }

    /**
     * 获取当前未过期的通知列表
     * 在返回前，会自动清理掉已经过期的通知
     * @return 未过期通知的列表
     */
    public List<Notification> getActiveNotifications() {
        // 移除过期的通知
        notifications.removeIf(Notification::isExpired);
        return new ArrayList<>(notifications);
    }

    /**
     * 清空所有通知
     */
    public void clearNotifications() {
        notifications.clear();
    }
}
