package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.ProtectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.TeamRoom;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import static cc.isotopestudio.cscraft.CScraft.classData;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class UpdateConfig extends BukkitRunnable {

    @Override
    public void run() {
        // Update config

        // Update class
        CSClass.classes.clear();
        for (String className : classData.getKeys(false)) {
            new CSClass(className);
        }

        // Update room
        Room.rooms.clear();
        File roomFolder = new File(plugin.getDataFolder() + "/rooms/");
        if (roomFolder.exists()) {
            File[] files = roomFolder.listFiles();
            if (files != null)
                for (File roomFile : files) {
//                    String roomFileName = roomFile.getName().substring(roomFile.getName().indexOf(dir));
                    String[] roomFileString = roomFile.getName().split("\\.");
                    switch (roomFileString[0]) {
                        case "InfectRoom":
                            new InfectRoom(roomFileString[1]);
                            break;
                        case "ProtectRoom":
                            new ProtectRoom(roomFileString[1]);
                            break;
                        case "TeamRoom":
                            new TeamRoom(roomFileString[1]);
                            break;
                        default:
                            plugin.getLogger().warning("´íÎó: parsing room data folder");
                            break;
                    }
                }
        }
    }
}
