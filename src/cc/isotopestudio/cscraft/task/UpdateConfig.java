package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.data.CSClass;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.CScraft.classData;

public class UpdateConfig extends BukkitRunnable {

    @Override
    public void run() {
        // Update config

        // Update class
        for (String className : classData.getKeys(false)) {
            CSClass.classes.put(className, new CSClass(className));
        }

        // Update room
    }
}
