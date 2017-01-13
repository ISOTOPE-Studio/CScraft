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

import java.util.HashMap;
import java.util.Map;

import static cc.isotopestudio.cscraft.CScraft.playerData;

public class PlayerInfo {

    public static Map<Player, Room> playerRoomMap = new HashMap<>();

    public static Room getRoom(Player player) {
        return Room.rooms.get(playerData.getString(player.getName() + ".room"));
    }

    public static void setPlayerError(Player player, String error) {
        playerData.set(player.getName() + ".error", error);
        playerData.save();
    }

    public static String getPlayerError(Player player) {
        return playerData.getString(player.getName() + ".error");
    }

    public static void teleportOut(Player player) {
        final Location location = Util.stringToLocation(playerData.getString(player.getName() + ".location"));
        if (location == null) {
            player.sendMessage(S.toPrefixRed("ÄãµÄÊý¾ÝËð»µ!"));
            return;
        }
        player.teleport(location);
        Util.loadInventory(playerData.getConfigurationSection(player.getName() + ".inventory"), player);
        playerData.set(player.getName(), null);
        playerData.save();
    }

}
