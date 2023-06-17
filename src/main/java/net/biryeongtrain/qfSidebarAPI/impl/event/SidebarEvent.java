package net.biryeongtrain.qfSidebarAPI.impl.event;

import net.biryeongtrain.qfSidebarAPI.impl.SidebarHolderImpl;
import net.biryeongtrain.qfSidebarAPI.qfSidebarAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SidebarEvent {
    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        qfSidebarAPI.SIDEBAR_HOLDERS.put(player.getUniqueId(), new SidebarHolderImpl(player));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

    }
}
