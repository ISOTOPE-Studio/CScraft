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

import java.util.HashSet;
import java.util.Set;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class InfectRoom extends Room {

    // Team Antigen: Antigen
    // Team A: Zombie + Antigen
    // Team B: Human

    // Settings
    private Set<CSClass> teamAntigenClass = new HashSet<>();

    // In-game
    private Set<Player> teamAntigenPlayer = new HashSet<>();

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
    public void join(Player player) {
        super.join(player);

        player.getInventory().setItem(3, GameItems.getAntigenClassItem());
        player.getInventory().setItem(4, GameItems.getZombieClassItem());
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
