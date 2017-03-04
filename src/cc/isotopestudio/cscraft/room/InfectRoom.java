package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class InfectRoom extends Room {

    // Team Antigen: Antigen
    // Team A: Zombie + Antigen
    // Team B: Human

    // Settings
    // teamAclass for zombie
    // teamBclass for Human
    private CSClass teamAntigenDefaultClass;
    private CSClass teamZombieDefaultClass;
    private Set<CSClass> teamAntigenClass = new HashSet<>();

    // In-game
    private Set<Player> teamAntigenPlayer = new HashSet<>();

    public InfectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_infect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);
        teamAntigenDefaultClass = CSClass.getClassByName(config.getString("teamAntigenDefaultClass"));
        teamZombieDefaultClass = CSClass.getClassByName(config.getString("teamZombieDefaultClass"));
        teamAntigenClass.clear();
        teamAntigenClass.addAll(CSClass.parseSet(config.getStringList("teamAntigenClass")));
    }

    public CSClass getTeamAntigenDefaultClass() {
        return teamAntigenDefaultClass;
    }


    public void setTeamAntigenDefaultClass(CSClass teamAntigenDefaultClass) {
        this.teamAntigenDefaultClass = teamAntigenDefaultClass;
        config.set("teamAntigenDefaultClass", teamAntigenDefaultClass.getName());
    }

    public CSClass getTeamZombieDefaultClass() {
        return teamZombieDefaultClass;
    }

    public void setTeamZombieDefaultClass(CSClass teamZombieDefaultClass) {
        this.teamZombieDefaultClass = teamZombieDefaultClass;
        config.set("teamZombieDefaultClass", teamZombieDefaultClass.getName());
    }

    public Set<CSClass> getTeamAntigenClass() {
        return teamAntigenClass;
    }

    public void saveTeamAntigenClass() {
        List<String> list = new ArrayList<>();
        for (CSClass csclass : teamAntigenClass) {
            list.add(csclass.getName());
        }
        config.set("teamAntigenClass", list);
        config.save();
    }


    public Set<Player> getTeamAntigenPlayers() {
        return teamAntigenPlayer;
    }

    @Override
    public boolean isReady() {
        return super.isReady() && teamAntigenDefaultClass != null && teamZombieDefaultClass != null
                && teamAntigenClass.size() > 0;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        player.getInventory().setItem(5, GameItems.getHumanClassItem());
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public String getPlayerTeamName(Player player) {
        if (getTeamAplayer().contains(player)) {
            return teamAntigenPlayer.contains(player) ? S.toBoldDarkAqua("母体") : S.toBoldRed("僵尸");
        } else
            return S.toBoldRed("人类");
    }

    @Override
    public String toString() {
        return name();
    }

    public static String name() {
        return S.toBoldDarkGreen("感染模式");
    }
}
