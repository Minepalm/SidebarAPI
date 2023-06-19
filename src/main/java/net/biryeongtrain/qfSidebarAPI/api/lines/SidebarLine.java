package net.biryeongtrain.qfSidebarAPI.api.lines;

import net.biryeongtrain.qfSidebarAPI.api.Sidebar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface SidebarLine {
    /**
     * Returns score of this line (used for ordering)
     * Highest number is always on top
     */
    int getValue();

    /**
     * Changes value of sidebar line. Used by {@code LineBuilder}
     */
    boolean setValue(int value);

    /**
     * Gets text for selected player. Uses
     *
     * @param player Player
     * @return Component displayed for player
     */
    Component getComponent(Player player);

    /**
     * Creates a static, immutable copy of this SidebarLine, that's used for comparing
     */
    default ImmutableSidebarLine immutableCopy(Player player) {
        return new ImmutableSidebarLine(this.getValue(), this.getComponent(player).asComponent());
    }

    /**
     * Sets active sidebar. Every SidebarLine should only have one sidebar!
     */
    void setSidebar(@Nullable Sidebar sidebar);

    /**
     * Quick way to create SidebarLine instance with more advanced text building
     *
     * @param value Scoreboard value
     * @param textBuilder Function creating component
     * @return SidebarLine
     */
    static SidebarLine create(int value, Function<@Nullable Player, Component> textBuilder) {
        return new SuppliedSidebarLine(value, textBuilder);
    }

    /**
     * Quick way to create SidebarLine instance with more advanced text building
     *
     * @param value Scoreboard value
     * @param text Text
     * @return SidebarLine
     */
    static SidebarLine create(int value, Component text) {
        return new SimpleSidebarLine(value, text);
    }

    /**
     * Quick way to create SidebarLine instance with more advanced text building
     *
     * @param value Scoreboard value
     * @return SidebarLine
     */
    static SidebarLine createEmpty(int value) {
        return new AbstractSidebarLine() {
            @Override
            public Component getComponent() {
                return Component.empty();
            }
        };
    }
}
