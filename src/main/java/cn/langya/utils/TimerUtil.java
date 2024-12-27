package cn.langya.utils;

// 定义一个工具类，用于计时功能
public class TimerUtil {
    private long lastMS;

    // 获取当前时间的毫秒数
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    // 检查指定的毫秒数是否已经到达
    public boolean hasReached(int milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("milliseconds must be non-negative");
        }
        return (getCurrentMS() - lastMS) >= milliseconds;
    }

    // 重置计时器
    public void reset() {
        lastMS = getCurrentMS();
    }

    // 检查指定的毫秒数延迟是否已经到达
    public boolean delay(float milliSec) {
        if (milliSec < 0) {
            throw new IllegalArgumentException("milliSec must be non-negative");
        }
        return (getCurrentMS() - lastMS) >= milliSec;
    }

    // 获取当前时间的毫秒数
    public long getTime() {
        return getCurrentMS();
    }
}
