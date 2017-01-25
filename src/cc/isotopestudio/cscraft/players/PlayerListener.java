package cc.isotopestudio.cscraft.players;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.players.listener.DamageListener;
import cc.isotopestudio.cscraft.players.listener.EntranceListener;
import cc.isotopestudio.cscraft.players.listener.GUIItemListener;
import cc.isotopestudio.cscraft.players.listener.RoomListener;
import org.bukkit.plugin.PluginManager;

import static cc.isotopestudio.cscraft.CScraft.plugin;

public class PlayerListener {

    public static void enableListener() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new DamageListener(), plugin);
        pm.registerEvents(new EntranceListener(), plugin);
        pm.registerEvents(new GUIItemListener(), plugin);
        pm.registerEvents(new RoomListener(), plugin);
    }

}
