package net.biryeongtrain.qfSidebarAPI.impl;

import net.biryeongtrain.qfSidebarAPI.api.SidebarInterface;
import net.biryeongtrain.qfSidebarAPI.api.lines.ImmutableSidebarLine;
import net.biryeongtrain.qfSidebarAPI.qfSidebarAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class SidebarHolderImpl implements SidebarHolder{

    public SidebarHolderImpl(Player player) {
        this.player = (ServerPlayer) player;
    }

    private final ServerPlayer player;
    private final Set<SidebarInterface> sidebarApi$sidebars = new HashSet<>();

    private final ImmutableSidebarLine[] sidebarApi$lines = new ImmutableSidebarLine[15];
    private SidebarInterface sidebarApi$currentSidebar = null;
    private Component sidebarApi$title = null;
    private boolean sidebarApi$alreadyHidden = true;
    int sidebarApi$currentTick = 0;
    @Override
    public void sidebarApi$add(SidebarInterface sidebar) {

    }

    @Override
    public void sidebarApi$remove(SidebarInterface sidebar) {

    }

    @Override
    public void sidebarApi$clear() {

    }

    @Override
    public Set<SidebarInterface> sidebarApi$getAll() {
        return null;
    }

    @Override
    public SidebarInterface sidebarApi$getCurrent() {
        return null;
    }

    @Override
    public void sidebarApi$update(SidebarInterface candidate) {

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
                ClientboundSetObjectivePacket packet = new ClientboundSetObjectivePacket(qfSidebarAPI.SCOREBOARD_OBJECTIVE, 0);
                SOUS2CPacketAccessor accessor = (SOUS2CPacketAccessor) packet;
                accessor.setTitle(this.sidebarApi$title);
                this.sendPacket(packet);
            }
            {
                ScoreboardDisplayS2CPacket packet = new ScoreboardDisplayS2CPacket(1, null);
                ScoreboardDisplayS2CPacketAccessor accessor = (ScoreboardDisplayS2CPacketAccessor) packet;
                accessor.setName(SidebarAPIMod.OBJECTIVE_NAME);
                this.sendPacket(packet);
            }

            int x = 0;
            for (SidebarLine line : this.sidebarApi$currentSidebar.getLinesFor((ServerPlayNetworkHandler) (Object) this)) {
                this.sidebarApi$lines[x] = line.immutableCopy((ServerPlayNetworkHandler) (Object) this);
                TeamS2CPacket packet = TeamS2CPacket.updateTeam(SidebarAPIMod.TEAMS.get(x), true);
                ((SerializableTeamAccessor) packet.getTeam().get()).setPrefix(line.getText((ServerPlayNetworkHandler) (Object) this));
                this.sendPacket(packet);

                this.sendPacket(new ScoreboardPlayerUpdateS2CPacket(
                        ServerScoreboard.UpdateMode.CHANGE, SidebarAPIMod.OBJECTIVE_NAME, SidebarAPIMod.FAKE_PLAYER_NAMES.get(x), line.getValue()));
                x++;
            }
        }
    }

    @Override
    public void sidebarApi$updateText() {

    }

    @Override
    public void sidebarApi$updateState(boolean tick) {

    }
}
