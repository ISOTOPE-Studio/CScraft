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

public class TeamRoom extends Room {

    private int goal;

    public TeamRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_team.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);

        goal = config.getInt("goal", 20);
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        config.set("goal", goal);
        config.save();
    }

    @Override
    public boolean isReady() {
        return super.isReady();
    }

    @Override
    public String toString() {
        return S.toBoldGold("团队模式");
    }
}
