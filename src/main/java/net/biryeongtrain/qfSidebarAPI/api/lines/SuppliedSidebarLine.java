package net.biryeongtrain.qfSidebarAPI.api.lines;


import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SuppliedSidebarLine extends AbstractSidebarLine {
    private final Function<Player, Component> func;

    public SuppliedSidebarLine(int value, Function<@Nullable Player, Component> func) {
        this.value = value;
        this.func = func;
    }

    public Component getComponent() {
        return this.func.apply(null);
    }

    public Component getComponent(Player player) {
        return this.func.apply(player);
    }
}
