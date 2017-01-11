package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.RoomStatus;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class RoomLobbyUpdateTask extends BukkitRunnable {
    private int waitCount = 0;

    private static final int waitInterval = 7;
    private static final int startWaitInterval = 30;
    private static final int[] startWaitAnnounce = {60, 30, 15, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Override
    public void run() {
        waitCount++;
        if (waitCount == waitInterval) waitCount = 0;
        for (Room room : Room.rooms.values()) {
            if (room.getStatus() != RoomStatus.WAITING) continue;

            if (room.getPlayers().size() >= room.getMinPlayer()) {
                if (room.getScheduleStart() < 0) {
                    room.setScheduleStart(new Date().getTime() + startWaitInterval * 1000);
                    room.sendAllPlayersMsg(S.toPrefixYellow("���� " + startWaitInterval + " �뿪ʼ��Ϸ"));
                } else {
                    int sec = getRemainSec(room.getScheduleStart());
                    for (int num : startWaitAnnounce) {
                        if (num == sec) {
                            room.sendAllPlayersMsg(S.toPrefixYellow("���� " + sec + " �뿪ʼ��Ϸ"));
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
                room.sendAllPlayersMsg(S.toPrefixYellow("���ڴ�����ȴ�������ҽ���   ") + S.toGreen(
                        room.getPlayers().size() + " / " + room.getMinPlayer() + " / " + room.getMaxPlayer()));
        }

    }

    private int getRemainSec(long time) {
        long now = new Date().getTime();
        return (int) ((time - now) / 1000);
    }
}