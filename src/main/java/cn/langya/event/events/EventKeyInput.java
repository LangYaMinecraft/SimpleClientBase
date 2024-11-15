package cn.langya.event.events;

import cn.langya.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LangYa
 * @since 2024/11/16 04:36
 */
@AllArgsConstructor
@Getter
public class EventKeyInput implements Event {
    private final int keyCode;
}
