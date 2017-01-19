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
    public String getTeamAName() {
        return S.toBoldDarkAqua("��ʬ");
    }

    @Override
    public String getTeamBName() {
        return S.toBoldRed("����");
    }


    @Override
    public String toString() {
        return name();
    }

    public static String name() {
        return S.toBoldDarkGreen("��Ⱦģʽ");
    }
}
