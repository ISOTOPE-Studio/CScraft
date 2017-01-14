package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class ProtectRoom extends Room {

    private Location entityA;
    private Location entityB;

    public ProtectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_protect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);

        entityA = Util.stringToLocation(config.getString("entityA"));
        entityB = Util.stringToLocation(config.getString("entityB"));
    }

    public Location getEntityA() {
        return entityA;
    }

    public void setEntityA(Location entityA) {
        this.entityA = entityA;
    }

    public Location getEntityB() {
        return entityB;
    }

    public void setEntityB(Location entityB) {
        this.entityB = entityB;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String infoString() {
        return super.infoString() +
                "\nentityA=" + Util.locationToString(entityA) +
                "\nentityB=" + Util.locationToString(entityB);
    }

    public static String name() {
        return S.toBoldPurple(" ÿŒ¿ƒ£ Ω");
    }
}
