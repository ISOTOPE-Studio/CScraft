package cc.isotopestudio.cscraft.players.listener;
/*
 * Created by david on 2017/1/25.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.players.PlayerInfo;
import cc.isotopestudio.cscraft.room.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.CScraft.plugin;
import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;

public class EntranceListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String error = PlayerInfo.getPlayerError(player);
        if (error != null)
            switch (error) {
                default:
                    return;
                case "leaveAtLobby":
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerInfo.teleportOut(player);
                        }
                    }.runTaskLater(plugin, 2);
                    break;
                case "leaveAtGame":
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerInfo.teleportOut(player);
                        }
                    }.runTaskLater(plugin, 2);
                    break;
            }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            room.leave(player);
            PlayerInfo.setPlayerError(player, "leaveAtLobby");
        } else {
            room.leave(player);
            PlayerInfo.setPlayerError(player, "leaveAtGame");
        }
    }

}
