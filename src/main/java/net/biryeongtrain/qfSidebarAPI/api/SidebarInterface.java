package net.biryeongtrain.qfSidebarAPI.api;

import net.biryeongtrain.qfSidebarAPI.api.lines.SidebarLine;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

import java.util.List;

public interface SidebarInterface {
    Sidebar.Priority getPriority();
    default int getUpdateRate() {return 1;}
    Component getTitleFor(Player player);
    boolean isDirty();
    List<SidebarLine> getLinesFor(Player player);
    boolean isActive();
    void disconnected(Player player);

    default boolean manualComponentUpdates() {return false;}
}
