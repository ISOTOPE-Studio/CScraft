package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.room.Room.rooms;

public class CheckPlayerLocation extends BukkitRunnable {

    @Override
    public void run() {
        for (Room room : rooms.values()) {
            if (room.getStatus() != RoomStatus.PROGRESS) continue;
            for (Player player : room.getPlayers()) {
                switch (isOutsideOfRegion(player.getLocation(), room.getPos1(), room.getPos2())) {
                    case (0): break;
                    case (1):
                        break;
                    case (2):
                        break;
                    case (3):
                        break;
                    case (-1):
                        break;
                    case (-2):
                        break;
                    case (-3):
                        break;
                }
            }
        }
    }

    /**
     * @return 0: in region<br />
     * 1: out of X<br />
     * 2: out of Y<br />
     * 3: out of Z<br />
     * positive: out of pos1; negative: out of pos2
     */
    private static int isOutsideOfRegion(Location player, Location pos1, Location pos2) {
        if (player.getBlockX() < pos1.getBlockX()) {
            return -1;
        }
        if (player.getBlockY() < pos1.getBlockY()) {
            return -2;
        }
        if (player.getBlockZ() < pos1.getBlockZ()) {
            return -3;
        }
        if (player.getBlockX() > pos2.getBlockX()) {
            return 1;
        }
        if (player.getBlockY() > pos2.getBlockY()) {
            return 2;
        }
        if (player.getBlockZ() > pos2.getBlockZ()) {
            return 3;
        }
        return 0;
    }
}
