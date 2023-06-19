package net.biryeongtrain.qfSidebarAPI.impl.holder;

import net.biryeongtrain.qfSidebarAPI.api.SidebarInterface;
import net.biryeongtrain.qfSidebarAPI.api.SidebarStorage;
import org.bukkit.entity.Player;

import java.util.Set;

@SuppressWarnings("unused")
public interface SidebarHolder {
    void sidebarApi$add(SidebarInterface sidebar);
    void sidebarApi$remove(SidebarInterface sidebar);
    void sidebarApi$clear();
    Set<SidebarInterface> sidebarApi$getAll();
    SidebarInterface sidebarApi$getCurrent();
    void sidebarApi$update(SidebarInterface candidate);
    void sidebarApi$removeEmpty();
    void sidebarApi$setupInitial();
    void sidebarApi$updateText();
    void sidebarApi$updateState(boolean tick);

    static SidebarHolder of(Player player) {
        return SidebarStorage.SIDEBAR_HOLDERS.get(player);
    }
}
