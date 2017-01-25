package cc.isotopestudio.cscraft.players;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static cc.isotopestudio.cscraft.CScraft.playerData;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class PlayerInfo {

    public static Map<Player, Room> playerRoomMap = new HashMap<>();

    static void setPlayerError(Player player, String error) {
        playerData.set(player.getName() + ".error", error);
        playerData.save();
    }

    static String getPlayerError(Player player) {
        return playerData.getString(player.getName() + ".error");
    }

    public static void teleportOut(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        final Location location = Util.stringToLocation(playerData.getString(player.getName() + ".location"));
        if (location == null) {
            player.sendMessage(S.toPrefixRed("ÄãµÄÊý¾ÝËð»µ!"));
            return;
        }
        teleport(player, location);
        new BukkitRunnable() {
            @Override
            public void run() {
                Util.loadInventory(playerData.getConfigurationSection(player.getName() + ".inventory"), player);
                player.setMaxHealth(20);
            }
        }.runTaskLater(plugin, 10);
    }

    public static void teleport(Player player, Location location) {
        player.teleport(location);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(location);
            }
        }.runTaskLater(plugin, 1);
    }

}
