package net.biryeongtrain.qfSidebarAPI.api;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.biryeongtrain.qfSidebarAPI.impl.SidebarHolderImpl;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class SidebarStorage {
    public static final Object2ObjectMap<Player, SidebarHolderImpl> SIDEBAR_HOLDERS = new Object2ObjectOpenHashMap<>();

    public static Sidebar getCurrentSidebar(Player player) {
        SidebarHolderImpl sidebarHolder = SIDEBAR_HOLDERS.get(player);
        return (Sidebar) sidebarHolder.sidebarApi$getCurrent();
    }

    public static void closeCurrentSidebar(Player player) {
        SidebarHolderImpl sidebarHolder = SIDEBAR_HOLDERS.get(player);
        sidebarHolder.sidebarApi$remove(sidebarHolder.sidebarApi$getCurrent());
    }

    public static void closeSidebar(Player player, SidebarInterface sidebar) {
        SidebarHolderImpl sidebarHolder = SIDEBAR_HOLDERS.get(player);
        sidebarHolder.sidebarApi$remove(sidebar);
    }
}
