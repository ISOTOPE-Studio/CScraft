package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 1/11/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        team1Item.setDurability((short) 11);
        meta = team1Item.getItemMeta();
        meta.setDisplayName(S.toBoldDarkAqua("加入蓝队 (右键)"));
        team1Item.setItemMeta(meta);

        team2Item.setDurability((short) 14);
        meta = team2Item.getItemMeta();
        meta.setDisplayName(S.toBoldRed("加入红队 (右键)"));
        team2Item.setItemMeta(meta);

        infoItem.setDurability((short) 3);
        meta = infoItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("战绩 (右键)"));
        infoItem.setItemMeta(meta);

        antigenClassItem.setDurability((short) 14);
        zombieClassItem.setDurability((short) 5);
        humanClassItem.setDurability((short) 11);
    }

    public static ItemStack getExitItem() {
        return exitItem.clone();
    }

    public static ItemStack getClassItem() {
        return classItem;
    }

    public static ItemStack getTeam1Item() {
        return team1Item;
    }

    public static ItemStack getTeam2Item() {
        return team2Item;
    }

    public static ItemStack getInfoItem() {
        return infoItem;
    }

    public static ItemStack getNoPermission() {
        return noPermission;
    }

    public static ItemStack getAntigenClassItem() {
        return antigenClassItem;
    }

    public static ItemStack getZombieClassItem() {
        return zombieClassItem;
    }

    public static ItemStack getHumanClassItem() {
        return humanClassItem;
    }
}
