package net.biryeongtrain.qfSidebarAPI.impl.utils;

import net.biryeongtrain.qfSidebarAPI.api.ScrollableSidebar;
import net.biryeongtrain.qfSidebarAPI.api.Sidebar;
import net.biryeongtrain.qfSidebarAPI.api.SidebarInterface;
import net.biryeongtrain.qfSidebarAPI.api.lines.LineBuilder;
import net.biryeongtrain.qfSidebarAPI.impl.holder.SidebarHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class SidebarUtils {

    public static Sidebar create(Component component, Sidebar.Priority priority, Consumer<LineBuilder> lineBuilderConsumer) {
        var sidebar = new Sidebar(component, priority);
        sidebar.set(lineBuilderConsumer);
        return sidebar;
    }

    public static ScrollableSidebar createScrolling(Component component, Sidebar.Priority priority, int speed, Consumer<LineBuilder> lineBuilderConsumer) {
        var sidebar = new ScrollableSidebar(component, priority, speed);
        sidebar.set(lineBuilderConsumer);
        return sidebar;
    }

    public static void updateTexts(Player player, SidebarInterface sidebar) {
        if (isVisible(player, sidebar)) {
            SidebarHolder.of(player).sidebarApi$updateText();
        }
    }

    public static boolean isVisible(Player player, SidebarInterface sidebar) {
        return SidebarHolder.of(player).sidebarApi$getCurrent() == sidebar;
    }

    public static void updatePriorities(Player player, SidebarInterface sidebar) {
        SidebarHolder.of(player).sidebarApi$update(sidebar);
    }

    public static void requestStateUpdate(Player player) {
        SidebarHolder.of(player).sidebarApi$updateState(false);
    }

    public static void addSidebar(Player player, SidebarInterface sidebar) {
        if (!SidebarHolder.of(player).sidebarApi$getAll().contains(sidebar)) {
            SidebarHolder.of(player).sidebarApi$add(sidebar);
        }
    }

    public static void removeSidebar(Player player, SidebarInterface sidebar) {
        if (SidebarHolder.of(player).sidebarApi$getAll().contains(sidebar)) {
            SidebarHolder.of(player).sidebarApi$remove(sidebar);
        }
    }
}
