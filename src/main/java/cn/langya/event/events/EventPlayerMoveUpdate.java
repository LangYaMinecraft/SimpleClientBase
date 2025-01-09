package cn.langya.event.events;

import cn.langya.event.impl.CancellableEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventPlayerMoveUpdate extends CancellableEvent {
    private float strafe, forward, friction, yaw, pitch;
}