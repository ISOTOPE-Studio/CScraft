package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.ChatColor;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class InfectRoom extends Room {

    public InfectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_infect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return S.toBoldDarkGreen("��Ⱦģʽ");
    }
}
