package cn.langya.event.impl;

/**
 * 可取消事件的抽象类。
 * 该类实现了 {@link Event} 和 {@link Cancellable} 接口。
 */
public abstract class CancellableEvent implements Event, Cancellable {
	/**
	 * 一个标志指示事件是否已被取消。
	 */
	private boolean cancelled;

	/**
	 * 设置事件的取消状态。
	 *
	 * @param cancelled {@code true} 取消事件, {@code false} 允许事件继续。
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * 检查事件是否已被取消。
	 *
	 * @return {@code true} 如果事件被取消, {@code false} 否则。
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
}

