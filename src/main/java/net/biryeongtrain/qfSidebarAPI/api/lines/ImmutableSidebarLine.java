package net.biryeongtrain.qfSidebarAPI.api.lines;

import net.biryeongtrain.qfSidebarAPI.api.Sidebar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public record ImmutableSidebarLine(int value, Component component) implements SidebarLine{

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public boolean setValue(int value) {
        return false;
    }

    @Override
    public Component getComponent(Player player) {
        return this.component;
    }

    @Override
    public void setSidebar(@Nullable Sidebar sidebar) {}

    public boolean equals(Object obj, Player player) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof SidebarLine)) return false;
        SidebarLine that = (SidebarLine) obj;
        return this.value == that.getValue() && that.getComponent(player).equals(this.component);
    }
}
