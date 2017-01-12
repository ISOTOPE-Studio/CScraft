package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.ProtectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import cc.isotopestudio.cscraft.room.TeamRoom;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class RoomLobbyUpdateTask extends BukkitRunnable {
    private int waitCount = 0;

    private static final int waitInterval = 7;
    private static final int startWaitInterval = 15;
    private static final int[] startWaitAnnounce = {15, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Override
    public void run() {
        waitCount++;
        if (waitCount == waitInterval) waitCount = 0;
        for (Room room : Room.rooms.values()) {
            if (room.getStatus() != RoomStatus.WAITING) continue;

            if (room.getPlayers().size() >= room.getMinPlayer()) {
                if ((room instanceof TeamRoom || room instanceof ProtectRoom)
                        && room.getPlayers().size() % 2 != 0) {
                    room.sendAllPlayersMsg(S.toPrefixYellow("玩家数量必须是偶数"));
                    room.setScheduleStart(-1);
                } else if (room.getScheduleStart() < 0) {
                    room.setScheduleStart(new Date().getTime() + startWaitInterval * 1000);
                    room.sendAllPlayersMsg(S.toPrefixYellow("还有 " + startWaitInterval + " 秒开始游戏"));
                } else {
                    int sec = getRemainSec(room.getScheduleStart());
                    for (int num : startWaitAnnounce) {
                        if (num == sec) {
                            room.sendAllPlayersMsg(S.toPrefixYellow("还有 " + sec + " 秒开始游戏"));
                        }
                    }
                }
            } else {
                room.setScheduleStart(-1);
            }
            if (getRemainSec(room.getScheduleStart()) == 0) {
                room.start();
            }
            if (waitCount == 0)
                room.sendAllPlayersMsg(S.toPrefixYellow("你在大厅里，等待其他玩家进入   ") + S.toGreen(
                        room.getPlayers().size() + " / " + room.getMinPlayer() + " / " + room.getMaxPlayer()));
        }

    }

    private int getRemainSec(long time) {
        long now = new Date().getTime();
        return (int) ((time - now) / 1000);
    }
}
