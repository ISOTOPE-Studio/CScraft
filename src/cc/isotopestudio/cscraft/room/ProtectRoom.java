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

public class ProtectRoom extends Room {

    public ProtectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_protect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);
    }

    @Override
    void loadFromConfig() {
        super.loadFromConfig();

    }


    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return name();
    }

    public static String name() {
        return S.toBoldPurple("����ģʽ");
    }
}
