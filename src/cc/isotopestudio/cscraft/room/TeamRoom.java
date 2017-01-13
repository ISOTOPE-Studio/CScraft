package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    public void join(Player player) {
        super.join(player);
        if (teamAplayer.size() > teamBplayer.size()) {
            teamBplayer.add(player);
        } else if (teamAplayer.size() < teamBplayer.size()) {
            teamAplayer.add(player);
        } else {
            if (Math.random() < 0.5)
                teamBplayer.add(player);
            else
                teamAplayer.add(player);
        }

        player.getInventory().setItem(3, GameItems.getTeam1Item());
        player.getInventory().setItem(5, GameItems.getTeam2Item());
    }

    @Override
    public void start() {
        super.start();
        for (Player player : teamAplayer) {
            player.teleport(getTeamALocation());
        }
        for (Player player : teamBplayer) {
            player.teleport(getTeamBLocation());
        }
        sendAllPlayersMsg(S.toPrefixYellow("游戏开始"));
    }

    @Override
    public String toString() {
        return S.toBoldGold("团队模式");
    }
}
