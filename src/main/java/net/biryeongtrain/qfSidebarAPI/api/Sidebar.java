package net.biryeongtrain.qfSidebarAPI.api;

import lombok.Getter;
import lombok.Setter;
import net.biryeongtrain.qfSidebarAPI.api.lines.LineBuilder;
import net.biryeongtrain.qfSidebarAPI.api.lines.SidebarLine;
import net.biryeongtrain.qfSidebarAPI.api.lines.SimpleSidebarLine;
import net.biryeongtrain.qfSidebarAPI.impl.utils.SidebarUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Sidebar implements SidebarInterface {

    protected List<SidebarLine> elements = new ArrayList<>();
    protected Set<Player> players = new HashSet<>();
    protected Priority priority;
    @Getter
    @Setter
    protected Component title;
    protected boolean isDirty = false;
    protected int updateRate = 1;

    protected boolean isActive = false;

    public Sidebar(Priority priority) {
        this.priority = priority;
        this.title = Component.empty();
    }

    public Sidebar(Component title, Priority priority) {
        this.priority = priority;
        this.title = title;
    }

    @Override
    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        if (this.isActive) {
            for (Player player : this.players) {
                SidebarUtils.updatePriorities(player, this);
            }
        }
    }
    public int getUpdateRate() {
        return this.updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = Math.max(updateRate, 1);
    }
    @Override
    public Component getTitleFor(Player player) {
        return this.getTitle();
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    public void markDirty() {
        this.isDirty = true;
    }

    protected void sortIfDirty() {
        if (this.isDirty) {
            this.isDirty = false;
            this.elements.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        }
    }

    public void setLine(int value, Component component) {
        for (SidebarLine line : this.elements) {
            if (line.getValue() == value) {
                this.elements.set(this.elements.indexOf(line), new SimpleSidebarLine(value, component, this));
                return;
            }
        }

        this.elements.add(new SimpleSidebarLine(value, component, this));
        this.isDirty = true;
    }

    public void setLine(SidebarLine line) {
        for (SidebarLine cLine : this.elements) {
            if (line.getValue() == cLine.getValue()) {
                line.setSidebar(this);
                this.elements.set(this.elements.indexOf(cLine), line);
                return;
            }
        }

        this.elements.add(line);
        this.isDirty = true;
    }

    public void addLines(SidebarLine... lines) {
        for (SidebarLine line : lines) {
            line.setSidebar(this);
            this.elements.add(line);
        }

        this.isDirty = true;
    }

    public void addLines(Component... texts) {
        if (this.elements.isEmpty()) {
            int lastLine = texts.length;
            for (Component t : texts) {
                this.elements.add(new SimpleSidebarLine(--lastLine, t, this));
            }
        } else {
            this.sortIfDirty();
            int lastLine = this.elements.get(this.elements.size() - 1).getValue();
            for (Component t : texts) {
                this.elements.add(new SimpleSidebarLine(--lastLine, t, this));
            }
        }
    }

    public void removeLine(SidebarLine line) {
        this.elements.remove(line);
        line.setSidebar(null);
    }

    public void removeLine(int value) {
        for (SidebarLine line : new ArrayList<>(this.elements)) {
            if (line.getValue() == value) {
                this.elements.remove(line);
                line.setSidebar(null);
            }
        }
    }

    @Nullable
    public SidebarLine getLine(int value) {
        for (SidebarLine line : this.elements) {
            if (line.getValue() == value) {
                return line;
            }
        }

        return null;
    }

    public void replaceLines(Component... texts) {
        this.clearLines();
        this.addLines(texts);
    }

    public void replaceLines(SidebarLine... lines) {
        this.clearLines();
        this.addLines(lines);
    }

    public void replaceLines(LineBuilder builder) {
        this.replaceLines(builder.getLines().toArray(new SidebarLine[0]));
    }

    public void clearLines() {
        for (SidebarLine line : this.elements) {
            line.setSidebar(null);
        }

        this.elements.clear();
    }

    public void set(Consumer<LineBuilder> consumer) {
        LineBuilder builder = new LineBuilder();
        consumer.accept(builder);
        this.replaceLines(builder);
    }



    @Override
    public List<SidebarLine> getLinesFor(Player player) {
        this.sortIfDirty();

        return this.elements.subList(0, Math.min(14, this.elements.size()));
    }

    public void show() {
        if (!isActive) {
            this.isActive = true;
            for (Player player : this.players) {
                SidebarUtils.addSidebar(player, this);
            }
        }
    }

    public void hide() {
        if (this.isActive) {
            this.isActive = false;
            for (Player player : this.players) {
                SidebarUtils.removeSidebar(player, this);
            }
        }
    }

    public void addPlayer(Player player) {
        if (this.players.add(player)) {
            if (isActive) {
                SidebarUtils.addSidebar(player, this);
            }
        }
    }

    public void removePlayer(Player player) {
        if (this.players.remove(player)) {
            if (isActive) {
                if (player.isOnline()) {
                    SidebarUtils.removeSidebar(player, this);
                }
            }
        }
    }

    public Set<Player> getPlayerHandlerSet() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void disconnected(Player player) {

    }

    public enum Priority {
        LOWEST(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        OVERRIDE(4);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public boolean isLowerThan(Priority priority) {
            return this.value <= priority.value;
        }
    }
}
