package cc.isotopestudio.cscraft.players.listener;
/*
 * Created by david on 2017/1/25.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.Room;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        if (!playerRoomMap.containsKey(player)) {
            return;
        }
//        System.out.print("EntityDamageEvent");
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            event.setCancelled(true);
        } else {
            if (room.isUseColorCap() && player.getInventory().getHelmet() != null) {
                ItemStack helmet = player.getInventory().getHelmet();
                helmet.setDurability((short) 0);
                player.getInventory().setHelmet(helmet);
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
                        damager = (Player) ((Projectile) event1.getDamager()).getShooter();
                    }
                }
                if (damager != null) {
                    Set<Player> teamAplayer = new HashSet<>(room.getTeamAplayer());
                    Set<Player> teamBplayer = new HashSet<>(room.getTeamBplayer());
                    if (room instanceof InfectRoom) {
                        teamAplayer.addAll(((InfectRoom) room).getTeamAntigenPlayers());
                    }
                    if ((teamAplayer.contains(player) && teamAplayer.contains(damager)) ||
                            (teamBplayer.contains(player) && teamBplayer.contains(damager))) {
                        event.setCancelled(true);
                        return;
                    }
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
                            item = killer.getItemInHand();
                        }
                    }
                }
                room.playerDeath(killer, player, item);
            }

        }
    }

    @EventHandler
    public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
        System.out.print("WeaponDamageEntityEvent");
    }
}
