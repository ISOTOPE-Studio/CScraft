package cc.isotopestudio.cscraft.listener;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.CScraft;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.gui.ClassGUI;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static cc.isotopestudio.cscraft.listener.PlayerInfo.playerRoomMap;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String error = PlayerInfo.getPlayerError(player);
        if (error != null)
            switch (error) {
                default:
                    return;
                case "leaveAtLobby":
                    PlayerInfo.teleportOut(player);
                    break;
                case "leaveAtGame":
                    PlayerInfo.teleportOut(player);
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

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            event.setCancelled(true);
        } else {

        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {

        } else {
            room.sendAllPlayersMsg(player.getDisplayName() + S.toPrefixYellow(" "));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getItem() == null) return;
        if (!playerRoomMap.containsKey(player)) return;
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            event.setCancelled(true);
            if (GameItems.getExitItem().equals(event.getItem())) {
                room.exit(player);
                player.sendMessage(S.toPrefixGreen("退出房间"));
            } else if (GameItems.getTeam1Item().equals(event.getItem())) {
                if (room.getTeamAplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在蓝队了"));
                } else {
                    room.getTeamBplayer().remove(player);
                    room.getTeamAplayer().add(player);
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + S.toBoldDarkAqua("蓝队"));
                }
            } else if (GameItems.getTeam2Item().equals(event.getItem())) {
                if (room.getTeamBplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在红队了"));
                } else {
                    room.getTeamAplayer().remove(player);
                    room.getTeamBplayer().add(player);
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + S.toBoldRed("红队"));
                }
            } else if (GameItems.getClassItem().equals(event.getItem())) {
                new ClassGUI(room, player).open(player);
            }
        } else {
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (!playerRoomMap.containsKey(player)) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        event.setCancelled(true);
        if (room.getStatus() == RoomStatus.WAITING) {

        } else {
        }

    }


}
