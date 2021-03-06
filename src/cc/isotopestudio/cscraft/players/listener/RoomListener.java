package cc.isotopestudio.cscraft.players.listener;
/*
 * Created by david on 2017/1/25.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.EffectPlace;
import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.Set;

import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;
import static cc.isotopestudio.cscraft.room.Room.rooms;

public class RoomListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        player.spigot().respawn();
        event.setKeepInventory(true);
        event.setDeathMessage("");
        event.setKeepLevel(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.PROGRESS) {
            room.playerDeath(player.getKiller(), player, player.getKiller() != null ? player.getKiller().getItemInHand() : null);
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
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUpItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        for (Room room : rooms.values()) {
            if (room.getEffectItems().keySet().contains(item)) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                if (!room.getPlayers().contains(player)) {
                    return;
                }
                EffectPlace effectPlace = room.getEffectItems().get(item);
                player.addPotionEffect(effectPlace.getEffect(), false);
                room.getEffectItems().remove(item);
                item.remove();
                effectPlace.respawn();
            }
        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        Set<Player> recipients = event.getRecipients();

        // Player in room
        if (playerRoomMap.containsKey(player)) {
            Room room = playerRoomMap.get(player);
            if (room.getStatus() == RoomStatus.WAITING) {
                return;
            } else {
                event.setCancelled(true);
                if (event.getMessage().contains("&w")) {
                    for (Player recipient : room.getPlayers()) {
                        String msg = event.getMessage().replaceFirst("&w", "");
                        recipient.sendMessage(S.toBoldGold("[全]") + room.getPlayerFullName(player) + ": " + msg);
                    }
                } else {
                    recipients = room.getTeamAplayer().contains(player) ? room.getTeamAplayer() : room.getTeamBplayer();
                    for (Player recipient : recipients) {
                        recipient.sendMessage(room.getPlayerFullName(player) + ": " + event.getMessage());
                    }
                }
            }
        }
        // Player outside
        else {
            for (Room room : Room.rooms.values()) {
                if (room.getStatus() == RoomStatus.PROGRESS)
                    recipients.removeAll(room.getPlayers());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        // Player in room
        if (playerRoomMap.containsKey(player)) {
            Block block = event.getClickedBlock();
            if (block == null || block.getType() == Material.AIR) {
                return;
            }
            Material type = block.getType();
            if (type == Material.WORKBENCH || type == Material.FURNACE || type == Material.CHEST
                    || type == Material.TRAPPED_CHEST) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (playerRoomMap.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockPlaceEvent event) {
        if (playerRoomMap.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

}
