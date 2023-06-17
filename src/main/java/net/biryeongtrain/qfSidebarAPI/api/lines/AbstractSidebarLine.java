package net.biryeongtrain.qfSidebarAPI.api.lines;

import net.biryeongtrain.qfSidebarAPI.api.Sidebar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class AbstractSidebarLine implements SidebarLine{
    protected int value;
    protected Sidebar sidebar;

    public int getValue() {
        return this.value;
    }

    public boolean setValue(int value) {
        this.value = value;
        if (this.sidebar != null) {
            this.sidebar.markDirty();
        }
        return true;
    }

    public abstract Component getComponent();

    public Component getComponent(Player player) {
        return this.getComponent();
    }

    public void setSidebar(Sidebar sidebar) {
        this.sidebar = sidebar;
    }
}
