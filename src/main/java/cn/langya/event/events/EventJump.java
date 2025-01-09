package cn.langya.event.events;

import cn.langya.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventJump implements Event {
    private float yaw;
}