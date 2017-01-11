package cc.isotopestudio.cscraft.listener;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static cc.isotopestudio.cscraft.listener.PlayerInfo.playerRoomMap;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            if (GameItems.getExitItem().equals(event.getItem())) {
                room.exit(player);
                player.sendMessage(S.toPrefixGreen("ÍË³ö·¿¼ä"));
            }
        } else {
        }
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
