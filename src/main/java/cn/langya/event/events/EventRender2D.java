package cn.langya.event.events;

import cn.langya.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author LangYa
 * @since 2024/11/16 03:51
 */
@Getter
@AllArgsConstructor
public class EventRender2D implements Event {
    private final ScaledResolution scaledresolution;
    private final float partialTicks;
}
