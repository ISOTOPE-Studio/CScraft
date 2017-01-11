package cc.isotopestudio.cscraft.listener;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static cc.isotopestudio.cscraft.CScraft.playerData;

public class PlayerInfo {

    public static Map<Player, Room> playerRoomMap = new HashMap<>();

    public static Room getRoom(Player player) {
        return Room.rooms.get(playerData.getString(player.getName() + ".room"));
    }

}
