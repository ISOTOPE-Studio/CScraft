package cc.isotopestudio.cscraft.task;
/*
 * Created by david on 2017/3/9.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.room.Room.rooms;

public class ParticleTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Room room : rooms.values()) {
            if (room instanceof InfectRoom) {
                InfectRoom infectRoom = (InfectRoom) room;
                if (room.getStatus() == RoomStatus.PROGRESS) {
                    for (Player player : room.getTeamAplayer()) {
                        ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.EMERALD_BLOCK, (byte) 0),
                                (float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3, 0, 3, player.getLocation(), 100);
                    }
                    for (Player player : infectRoom.getTeamAntigenPlayers()) {
                        ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.EMERALD_BLOCK, (byte) 0),
                                (float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3, 0, 3, player.getLocation(), 100);
                    }
                }
            }
        }
    }
}
