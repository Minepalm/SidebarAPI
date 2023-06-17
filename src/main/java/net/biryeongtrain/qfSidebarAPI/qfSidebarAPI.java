package net.biryeongtrain.qfSidebarAPI;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.biryeongtrain.qfSidebarAPI.impl.SidebarHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class qfSidebarAPI extends JavaPlugin {
    public static final List<PlayerTeam> TEAMS = new ArrayList<>();
    public static final List<String> FAKE_PLAYERS = new ArrayList<>();
    public static final Scoreboard SCOREBOARD = new Scoreboard();
    public static final String OBJECTIVE_NAME = "■SidebarApiObj";
    public static final String TEAM_NAME = "■SidebarApi-";
    public static final Objective SCOREBOARD_OBJECTIVE = new Objective(SCOREBOARD, OBJECTIVE_NAME, ObjectiveCriteria.DUMMY, Component.literal("엄"), ObjectiveCriteria.RenderType.INTEGER);
    public static final Object2ObjectMap<UUID, SidebarHolder> SIDEBAR_HOLDERS = new Object2ObjectOpenHashMap<>();

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
