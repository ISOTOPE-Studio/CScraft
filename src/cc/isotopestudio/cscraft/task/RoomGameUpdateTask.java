package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.Room;
import org.bukkit.scheduler.BukkitRunnable;

public class RoomGameUpdateTask extends BukkitRunnable {
    private int waitCount = 0;

    private static final int waitInterval = 5;
    private static final int startWaitInterval = 10;
    private static final int[] startWaitAnnounce = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Override
    public void run() {
        waitCount++;
        if (waitCount == waitInterval) waitCount = 0;
        for (Room room : Room.rooms.values()) {
            if (room.getStatus() != RoomStatus.PROGRESS) continue;
            room.updateScoreBoardInGame();
            if (room.getTeamAplayer().size() == 0) {
                room.teamBWin();
                continue;
            } else if (room.getTeamBplayer().size() == 0) {
                room.teamAWin();
                continue;
            }
            if (room.getIntervalSec() >= 600) {
                room.timeout();
            }

        }
    }

}
