package net.biryeongtrain.qfSidebarAPI.impl;

import net.biryeongtrain.qfSidebarAPI.api.SidebarInterface;
import net.biryeongtrain.qfSidebarAPI.api.lines.ImmutableSidebarLine;
import net.biryeongtrain.qfSidebarAPI.api.lines.SidebarLine;
import net.biryeongtrain.qfSidebarAPI.qfSidebarAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SidebarHolderImpl implements SidebarHolder{

    public SidebarHolderImpl(Player player) {
        this.player = ((CraftPlayer)player).getHandle();
    }

    private final ServerPlayer player;
    public final Set<SidebarInterface> sidebarApi$sidebars = new HashSet<>();

    private final ImmutableSidebarLine[] sidebarApi$lines = new ImmutableSidebarLine[15];
    private SidebarInterface sidebarApi$currentSidebar = null;
    private Component sidebarApi$title = null;
    private boolean sidebarApi$alreadyHidden = true;
    int sidebarApi$currentTick = 0;
    @Override
    public void sidebarApi$add(SidebarInterface sidebar) {
        this.sidebarApi$sidebars.add(sidebar);
        this.sidebarApi$update(sidebar);
    }

    @Override
    public void sidebarApi$remove(SidebarInterface sidebar) {
        this.sidebarApi$sidebars.remove(sidebar);
        if (sidebar == this.sidebarApi$currentSidebar) {
            SidebarInterface newSidebar = null;
            for (var sidebar1 : this.sidebarApi$sidebars) {
                if (newSidebar == null || newSidebar.getPriority().isLowerThan(sidebar1.getPriority())) {
                    newSidebar = sidebar1;
                }
            }
            this.sidebarApi$currentSidebar = newSidebar;
        }
    }

    @Override
    public void sidebarApi$clear() {
        this.sidebarApi$sidebars.clear();
        this.sidebarApi$currentSidebar = null;
    }

    @Override
    public Set<SidebarInterface> sidebarApi$getAll() {
        return Collections.unmodifiableSet(this.sidebarApi$sidebars);
    }

    @Override
    public SidebarInterface sidebarApi$getCurrent() {
        return this.sidebarApi$currentSidebar;
    }

    @Override
    public void sidebarApi$update(SidebarInterface candidate) {
        if (this.sidebarApi$currentSidebar != null) {
            if (this.sidebarApi$currentSidebar.getPriority().isLowerThan(candidate.getPriority())) {
                this.sidebarApi$currentSidebar = candidate;
            }
        } else {
            this.sidebarApi$currentSidebar = candidate;
        }
    }

    @Override
    public void sidebarApi$removeEmpty() {
        if (!this.sidebarApi$alreadyHidden) {
            this.sidebarApi$alreadyHidden = true;

            {
                ClientboundSetDisplayObjectivePacket packet = new ClientboundSetDisplayObjectivePacket(1, null);
                this.player.connection.send(packet);
            }
            {
                ClientboundSetObjectivePacket packet = new ClientboundSetObjectivePacket(qfSidebarAPI.SCOREBOARD_OBJECTIVE, 1);
                this.player.connection.send(packet);
            }

            this.sidebarApi$title = null;
            for (int index = 0; index < this.sidebarApi$lines.length; index++) {
                if (this.sidebarApi$lines[index] != null) {
                    this.player.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(qfSidebarAPI.TEAMS.get(index)));
                    this.sidebarApi$lines[index] = null;
                }
            }
        }
    }

    @Override
    public void sidebarApi$setupInitial() {
        if (this.sidebarApi$alreadyHidden) {
            this.sidebarApi$alreadyHidden = false;
            this.sidebarApi$title = this.sidebarApi$currentSidebar.getTitleFor(player.connection.getCraftPlayer());
            this.sidebarApi$currentTick = 0;

            {
                ClientboundSetObjectivePacket packet = new ClientboundSetObjectivePacket(qfSidebarAPI.getScoreboardObjective(this.sidebarApi$title), 0);
                this.player.connection.send(packet);
            }
            {
                ClientboundSetDisplayObjectivePacket packet = new ClientboundSetDisplayObjectivePacket(1, qfSidebarAPI.SCOREBOARD_OBJECTIVE);
                this.player.connection.send(packet);
            }

            int x = 0;
            for (SidebarLine line : this.sidebarApi$currentSidebar.getLinesFor((player.getBukkitEntity()))) {
                this.sidebarApi$lines[x] = line.immutableCopy(player.getBukkitEntity());
                PlayerTeam team =qfSidebarAPI.TEAMS.get(x);
                team.setPlayerPrefix(qfSidebarAPI.convertAsMCComponent(line.getComponent(player.getBukkitEntity())));
                ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);

                this.player.connection.send(packet);

                this.player.connection.send(new ClientboundSetScorePacket(
                        ServerScoreboard.Method.CHANGE, qfSidebarAPI.OBJECTIVE_NAME, qfSidebarAPI.FAKE_PLAYERS.get(x), line.getValue()));
                x++;
            }
        }
    }

    @Override
    public void sidebarApi$updateText() {
        Component sidebarTitle = this.sidebarApi$currentSidebar.getTitleFor(player.getBukkitEntity());
        if (!sidebarTitle.equals(this.sidebarApi$title)) {
            this.sidebarApi$title = sidebarTitle;
            ClientboundSetObjectivePacket packet = new ClientboundSetObjectivePacket(qfSidebarAPI.getScoreboardObjective(sidebarTitle), 2);
            this.player.connection.send(packet);
        }

        int index = 0;

        for (SidebarLine line : this.sidebarApi$currentSidebar.getLinesFor(player.getBukkitEntity())) {
            if (this.sidebarApi$lines[index] == null || !this.sidebarApi$lines[index].equals(line, player.getBukkitEntity())) {
                PlayerTeam team =qfSidebarAPI.TEAMS.get(index);
                team.setPlayerPrefix(qfSidebarAPI.convertAsMCComponent(line.getComponent(player.getBukkitEntity())));
                ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team,this.sidebarApi$lines[index] == null);
                this.player.connection.send(packet);

                this.player.connection.send(new ClientboundSetScorePacket(
                        ServerScoreboard.Method.CHANGE, qfSidebarAPI.OBJECTIVE_NAME, qfSidebarAPI.FAKE_PLAYERS.get(index), line.getValue()));

                this.sidebarApi$lines[index] = line.immutableCopy(player.getBukkitEntity());
            }
            index++;
        }

        for (; index < this.sidebarApi$lines.length; index++) {
            if (this.sidebarApi$lines[index] != null) {
                this.player.connection.send(new ClientboundSetScorePacket(
                        ServerScoreboard.Method.REMOVE, qfSidebarAPI.OBJECTIVE_NAME, qfSidebarAPI.FAKE_PLAYERS.get(index), 0));
                player.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(qfSidebarAPI.TEAMS.get(index)));
            }

            this.sidebarApi$lines[index] = null;
        }
    }

    @Override
    public void sidebarApi$updateState(boolean tick) {
        if (this.sidebarApi$currentSidebar == null) {
            this.sidebarApi$removeEmpty();
            return;
        }

        if (this.sidebarApi$alreadyHidden) {
            this.sidebarApi$setupInitial();
        } else if (tick && !this.sidebarApi$currentSidebar.manualComponentUpdates()) {
            if (this.sidebarApi$currentTick % this.sidebarApi$currentSidebar.getUpdateRate() != 0) {
                this.sidebarApi$currentTick++;
                return;
            }

            this.sidebarApi$updateText();

            this.sidebarApi$currentTick++;
        }
    }
}
