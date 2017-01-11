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


    public static void update() {
        ItemMeta meta = exitItem.getItemMeta();
        meta.setDisplayName(S.toBoldRed("ÍË³ö (ÓÒ¼ü)"));
        exitItem.setItemMeta(meta);
    }

    public static ItemStack getExitItem() {
        return exitItem.clone();
    }
}
