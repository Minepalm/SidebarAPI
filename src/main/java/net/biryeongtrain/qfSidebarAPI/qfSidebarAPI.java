package net.biryeongtrain.qfSidebarAPI;

import net.biryeongtrain.qfSidebarAPI.impl.event.SidebarEvent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class qfSidebarAPI extends JavaPlugin {
    public static final List<PlayerTeam> TEAMS = new ArrayList<>();
    public static final List<String> FAKE_PLAYERS = new ArrayList<>();
    public static final Scoreboard SCOREBOARD = new Scoreboard();
    public static final String OBJECTIVE_NAME = "■SidebarApiObj";
    public static final String TEAM_NAME = "■SidebarApi-";
    public static final Objective SCOREBOARD_OBJECTIVE = new Objective(SCOREBOARD, OBJECTIVE_NAME, ObjectiveCriteria.DUMMY, Component.literal("엄"), ObjectiveCriteria.RenderType.INTEGER);

    @Override
    public void onEnable() {
        // Plugin startup logic
        for (int x = 0; x < 15; x++) {
            String fakePlayerName = String.format("§%s§r", Integer.toHexString(x));
            PlayerTeam team = new PlayerTeam(SCOREBOARD, TEAM_NAME + x);
            SCOREBOARD.addPlayerToTeam(fakePlayerName, team);

            TEAMS.add(team);
            FAKE_PLAYERS.add(fakePlayerName);
        }
        getServer().getPluginManager().registerEvents(new SidebarEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Objective getScoreboardObjective(net.kyori.adventure.text.Component component) {
        Component mcComponent = convertAsMCComponent(component);
        return new Objective(SCOREBOARD, OBJECTIVE_NAME, ObjectiveCriteria.DUMMY, mcComponent, ObjectiveCriteria.RenderType.INTEGER);
    }

    public static Component convertAsMCComponent(net.kyori.adventure.text.Component component) {
        Component mcComponent = null;
        if (component instanceof TextComponent textComponent) {
            mcComponent = Component.literal(textComponent.content());
        }  else if (component instanceof TranslatableComponent translatableComponent) {
            mcComponent = Component.translatable(translatableComponent.key());
        }
        return mcComponent;
    }
}
