package net.biryeongtrain.qfSidebarAPI.api;

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.biryeongtrain.qfSidebarAPI.api.lines.SidebarLine;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class ScrollableSidebar extends Sidebar {
    protected Object2LongMap<Player> position = new Object2LongArrayMap<>();
    protected int scrollTickNumber;

    public ScrollableSidebar(Priority priority, int scrollTickNumber) {
        super(priority);
        this.scrollTickNumber = scrollTickNumber;
    }

    public ScrollableSidebar(Component title, Priority priority, int scrollTickNumber) {
        super(title, priority);
        this.scrollTickNumber = scrollTickNumber;
    }

    public int getTicksPerLine() {
        return this.scrollTickNumber;
    }

    public void setTicksPerLine(int scrollTickNumber) {
        this.scrollTickNumber = scrollTickNumber;
    }

    @Override
    public List<SidebarLine> getLinesFor(Player player) {
        this.sortIfDirty();
        long pos = this.position.getLong(player);
        pos++;
        int index = (int) pos / scrollTickNumber;

        if (index + 14 > this.elements.size()) {
            pos = 0;
            index = 0;
        }

        this.position.put(player, pos);

        return this.elements.subList(index, Math.min(index + 14, this.elements.size()));
    }

    @Override
    public void removePlayer(Player player) {
        super.removePlayer(player);
        this.position.removeLong(player);
    }
}
