package net.biryeongtrain.qfSidebarAPI.api.lines;

import net.biryeongtrain.qfSidebarAPI.api.Sidebar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SimpleSidebarLine extends AbstractSidebarLine {

    private Component component;

    public SimpleSidebarLine(int value, Component component) {
        this.value = value;
        this.component = component;
    }

    public SimpleSidebarLine(int value, Component text, Sidebar sidebar) {
        this(value, text);
        this.sidebar = sidebar;
    }

    public Component getComponent() {return this.component;}

    public Component getComponent(Player player){
        return this.getComponent();
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
