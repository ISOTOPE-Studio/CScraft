package cc.isotopestudio.cscraft.players;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.CScraft;
import cc.isotopestudio.cscraft.element.EffectPlace;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.gui.ClassGUI;
import cc.isotopestudio.cscraft.gui.InfoGUI;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.CScraft.plugin;
import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;
import static cc.isotopestudio.cscraft.room.Room.rooms;

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
            if (room.isUseColorCap() && player.getInventory().getHelmet() != null) {
                player.getInventory().getHelmet().setDurability((short) 0);
            }
            EntityDamageByEntityEvent event1 = null;
            if (event instanceof EntityDamageByEntityEvent) {
                event1 = (EntityDamageByEntityEvent) event;
                Player damager = null;
                if (event1.getDamager() instanceof Player) {
                    damager = ((Player) event1.getDamager());
                }
                if (event1.getDamager() instanceof Projectile) {
                    if (((Projectile) event1.getDamager()).getShooter() instanceof Player) {
                        damager = (Player) ((Arrow) event1.getDamager()).getShooter();
                    }
                }
                if (damager != null)
                    if ((room.getTeamAplayer().contains(player) && room.getTeamAplayer().contains(damager)) ||
                            (room.getTeamBplayer().contains(player) && room.getTeamBplayer().contains(damager))) {
                        event.setCancelled(true);
                        return;
                    }
            }
            if (event.getFinalDamage() > player.getHealth()) {
                event.setCancelled(true);
                Player killer = null;
                ItemStack item = null;
                if (event1 != null) {
                    if (event1.getDamager() instanceof Player) {
                        killer = (Player) event1.getDamager();
                        item = killer.getItemInHand();
                    } else if (event1.getDamager() instanceof Projectile) {
                        if (((Projectile) (event1.getDamager())).getShooter() instanceof Player) {
                            killer = ((Player) ((Projectile) (event1.getDamager())).getShooter());
                        }
                    }
                }
                room.playerDeath(killer, player, item);
            }

        }
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
                // switch to team A
                if (room.getTeamAplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在蓝队了"));
                } else {
                    if (room.getTeamAplayer().size() >= room.getReqPlayerNum() / 2) {
                        player.sendMessage(S.toPrefixRed("蓝队已满员"));
                        return;
                    }
                    room.getTeamBplayer().remove(player);
                    room.getTeamAplayer().add(player);
                    room.getPlayerClassMap().remove(player);
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + S.toBoldDarkAqua("蓝队"));
                }
            } else if (GameItems.getTeam2Item().equals(event.getItem())) {
                // switch to team B
                if (room.getTeamBplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在红队了"));
                } else {
                    if (room.getTeamBplayer().size() >= room.getReqPlayerNum() / 2) {
                        player.sendMessage(S.toPrefixRed("红队已满员"));
                        return;
                    }
                    room.getTeamAplayer().remove(player);
                    room.getTeamBplayer().add(player);
                    room.getPlayerClassMap().remove(player);
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + S.toBoldRed("红队"));
                }
            } else if (GameItems.getClassItem().equals(event.getItem())) {
                if (room instanceof InfectRoom) {

                } else
                    new ClassGUI(room, player, room.getTeamAplayer().contains(player) ? room.getTeamAclass() : room.getTeamBclass()).open(player);
            }
        } else {
            if (GameItems.getInfoItem().equals(event.getItem())) {
                event.setCancelled(true);
                new InfoGUI(room, player).open(player);
            }
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
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        final Player player = (Player) event.getEntity();
        if (playerRoomMap.containsKey(player)) {
            event.setCancelled(true);
        }
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

}
