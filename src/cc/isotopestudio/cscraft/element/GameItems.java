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

    private static ItemStack exitItem = new ItemStack(Material.COMPASS);
    private static ItemStack classItem = new ItemStack(Material.NETHER_STAR);
    private static ItemStack team1Item = new ItemStack(Material.WOOL);
    private static ItemStack team2Item = new ItemStack(Material.WOOL);
    private static ItemStack infoItem = new ItemStack(Material.SKULL_ITEM);


    public static void update() {
        ItemMeta meta = exitItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("�˳� (�Ҽ�)"));
        exitItem.setItemMeta(meta);

        meta = classItem.getItemMeta();
        meta.setDisplayName(S.toBoldGold("ѡ��ְҵ (�Ҽ�)"));
        classItem.setItemMeta(meta);

        team1Item.setDurability((short) 11);
        meta = team1Item.getItemMeta();
        meta.setDisplayName(S.toBoldDarkAqua("�������� (�Ҽ�)"));
        team1Item.setItemMeta(meta);

        team2Item.setDurability((short) 14);
        meta = team2Item.getItemMeta();
        meta.setDisplayName(S.toBoldRed("������ (�Ҽ�)"));
        team2Item.setItemMeta(meta);

        infoItem.setDurability((short) 3);
        meta = infoItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("ս�� (�Ҽ�)"));
        infoItem.setItemMeta(meta);
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
}
