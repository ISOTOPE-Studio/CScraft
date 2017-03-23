package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.players.PlayerInfo;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;
import static cc.isotopestudio.cscraft.element.GameItems.*;

public class TeamRoom extends Room {

    private int goal;
    private int teamADeath = 0;
    private int teamBDeath = 0;

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
        if (getTeamAplayer().size() > getTeamBplayer().size()) {
            getTeamBplayer().add(player);
        } else if (getTeamAplayer().size() < getTeamBplayer().size()) {
            getTeamAplayer().add(player);
        } else {
            if (Math.random() < 0.5) {
                getTeamBplayer().add(player);
                player.getEquipment().setHelmet(addPlayerLore(getBlueTeamCap(), player));
            } else {
                getTeamAplayer().add(player);
                player.getEquipment().setHelmet(addPlayerLore(getRedTeamCap(), player));
            }
        }

        player.getInventory().setItem(3, addPlayerLore(getTeam1Item(), player));
        player.getInventory().setItem(5, addPlayerLore(getTeam2Item(), player));
    }

    @Override
    public void start() {
        super.start();
        for (Player player : getTeamAplayer()) {
            PlayerInfo.teleport(player, getTeamALocation());
            new InvincibleListener(this, player, 5);
        }
        for (Player player : getTeamBplayer()) {
            PlayerInfo.teleport(player, getTeamBLocation());
            new InvincibleListener(this, player, 5);
        }
        teamADeath = 0;
        teamBDeath = 0;
    }

    @Override
    public void playerDeath(Player killer, Player player, ItemStack item) {
        super.playerDeath(killer, player, item);
        new InvincibleListener(this, player, 5);
        if (getTeamAplayer().contains(player))
            PlayerInfo.teleport(player, getTeamALocation());
        else
            PlayerInfo.teleport(player, getTeamBLocation());
        if (getTeamAplayer().contains(player)) {
            teamADeath++;
        } else {
            teamBDeath++;
        }
        if (teamADeath >= goal) {
            teamBWin();
        } else if (teamBDeath >= goal) {
            teamAWin();
        }
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String infoString() {
        return super.infoString() +
                "\ngoal=" + goal;
    }

    public static String name() {
        return S.toBoldGold("团队模式");
    }
}
