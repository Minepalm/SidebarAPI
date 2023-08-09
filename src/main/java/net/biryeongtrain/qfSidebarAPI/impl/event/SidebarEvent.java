package net.biryeongtrain.qfSidebarAPI.impl.event;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.biryeongtrain.qfSidebarAPI.api.SidebarInterface;
import net.biryeongtrain.qfSidebarAPI.api.SidebarStorage;
import net.biryeongtrain.qfSidebarAPI.impl.holder.SidebarHolderImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public class SidebarEvent implements Listener {
    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SidebarStorage.SIDEBAR_HOLDERS.put(player, new SidebarHolderImpl(player));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        var instance = SidebarStorage.SIDEBAR_HOLDERS.get(player);
        for (SidebarInterface sidebar : new HashSet<>(instance.sidebarApi$sidebars)) {
            sidebar.disconnected(player);
        }
        SidebarStorage.SIDEBAR_HOLDERS.remove(player);
    }

    @EventHandler
    public void onPlayerTick(ServerTickEndEvent event) {
        SidebarStorage.SIDEBAR_HOLDERS.forEach((player, sidebar) -> sidebar.sidebarApi$updateState(true));
    }
}
