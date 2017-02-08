package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GameItems {

    // GUI
    private static ItemStack noPermission = new ItemStack(Material.STAINED_GLASS_PANE);

    // Game
    private static ItemStack exitItem = new ItemStack(Material.COMPASS);
    private static ItemStack classItem = new ItemStack(Material.NETHER_STAR);
    private static ItemStack team1Item = new ItemStack(Material.WOOL);
    private static ItemStack team2Item = new ItemStack(Material.WOOL);
    private static ItemStack infoItem = new ItemStack(Material.SKULL_ITEM);

    // InfectRoom
    private static ItemStack antigenClassItem = new ItemStack(Material.WOOL);
    private static ItemStack zombieClassItem = new ItemStack(Material.WOOL);
    private static ItemStack humanClassItem = new ItemStack(Material.WOOL);

    // cap
    private static ItemStack redTeamCap = new ItemStack(Material.LEATHER_HELMET);
    private static ItemStack blueTeamCap = new ItemStack(Material.LEATHER_HELMET);



    public static void update() {
        noPermission.setDurability((short) 7);
        ItemMeta meta = noPermission.getItemMeta();
        meta.setDisplayName(S.toBoldRed("无权限"));
        noPermission.setItemMeta(meta);

        meta = exitItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("退出 (右键)"));
        exitItem.setItemMeta(meta);

        meta = classItem.getItemMeta();
        meta.setDisplayName(S.toBoldGold("选择职业 (右键)"));
        classItem.setItemMeta(meta);

        team1Item.setDurability((short) 14);
        meta = team1Item.getItemMeta();
        meta.setDisplayName(S.toYellow("加入") + Room.TEAMANAME + " (右键)");
        team1Item.setItemMeta(meta);

        team2Item.setDurability((short) 11);
        meta = team2Item.getItemMeta();
        meta.setDisplayName(S.toYellow("加入") + Room.TEAMBNAME + " (右键)");
        team2Item.setItemMeta(meta);

        infoItem.setDurability((short) 3);
        meta = infoItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("战绩 (右键)"));
        infoItem.setItemMeta(meta);

        antigenClassItem.setDurability((short) 14);
        zombieClassItem.setDurability((short) 5);
        humanClassItem.setDurability((short) 11);

        LeatherArmorMeta lch = (LeatherArmorMeta) redTeamCap.getItemMeta();
        lch.setColor(Color.fromRGB(255, 0, 0));
        redTeamCap.setItemMeta(lch);

        lch = (LeatherArmorMeta) blueTeamCap.getItemMeta();
        lch.setColor(Color.fromRGB(0, 0, 255));
        blueTeamCap.setItemMeta(lch);
    }

    public static ItemStack getExitItem() {
        return exitItem.clone();
    }

    public static ItemStack getClassItem() {
        return classItem.clone();
    }

    public static ItemStack getTeam1Item() {
        return team1Item.clone();
    }

    public static ItemStack getTeam2Item() {
        return team2Item.clone();
    }

    public static ItemStack getInfoItem() {
        return infoItem.clone();
    }

    public static ItemStack getNoPermission() {
        return noPermission.clone();
    }

    public static ItemStack getAntigenClassItem() {
        return antigenClassItem.clone();
    }

    public static ItemStack getZombieClassItem() {
        return zombieClassItem.clone();
    }

    public static ItemStack getHumanClassItem() {
        return humanClassItem.clone();
    }

    public static ItemStack getRedTeamCap() {
        return redTeamCap;
    }

    public static ItemStack getBlueTeamCap() {
        return blueTeamCap;
    }
}
