package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class RoomLobbyUpdateTask extends BukkitRunnable {
    private int waitCount = 0;

    private static final int waitInterval = 5;
    private static final int startWaitInterval = 30;
    private static final int[] startWaitAnnounce = {30, 15, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Override
    public void run() {
        waitCount++;
        if (waitCount == waitInterval) waitCount = 0;
        for (Room room : Room.rooms.values()) {
            if (room.getStatus() != RoomStatus.WAITING) continue;

            room.updateScoreBoardAtLobby();

            // Full Player
            if (room.getPlayers().size() >= room.getReqPlayerNum()) {

                // Selecting class not finished
                if (room.getPlayerClassMap().size() < room.getPlayers().size()) {
                    for (Player player : room.getPlayers()) {
                        if (room.getPlayerClassMap().containsKey(player)) continue;
                        player.sendMessage(S.toPrefixRed("请选择职业"));
                    }
                }

                // Start counting
                if (room.getScheduleStart() < 0) {
                    room.setScheduleStart(new Date().getTime() + startWaitInterval * 1000);
                    room.sendAllPlayersMsg(S.toPrefixYellow("还有 " + startWaitInterval + " 秒开始游戏"));
                }
                int sec = getRemainSec(room.getScheduleStart());
                for (int num : startWaitAnnounce) {
                    if (num == sec) {
                        room.sendAllPlayersMsg(S.toPrefixYellow("还有 " + sec + " 秒开始游戏"));
                    }
                }

                // Start
                if (room.getScheduleStart() > 0 && getRemainSec(room.getScheduleStart()) <= 0) {
                    room.prestart();
                }
            }

            // Waiting
            else {
                room.setScheduleStart(-1);
                if (waitCount == 0) {
                    room.sendAllPlayersMsg(S.toPrefixYellow("你在大厅里，等待其他玩家进入   ") +
                            S.toGreen(room.getPlayers().size() + " / " + room.getReqPlayerNum()));
                    for (Player player : room.getPlayers()) {
                        if (room.getPlayerClassMap().containsKey(player)) continue;
                        player.sendMessage(S.toPrefixRed("请选择职业"));
                    }
                }
            }
        }

    }

    private int getRemainSec(long time) {
        long now = new Date().getTime();
        return (int) ((time - now) / 1000);
    }
}
