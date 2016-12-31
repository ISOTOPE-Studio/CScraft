package cc.isotopestudio.cscraft.data;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CSClass {

    public static final Map<String, CSClass> classes = new HashMap<>();

    private final String name;
    private final Map<Integer, ItemStack> inventory = new HashMap<>();
    private final ItemStack[] equipment = new ItemStack[4];

    public CSClass(String name) {
        this.name = name;
    }

    public void setClass(Player player) {

    }

    public void equip(Player player) {

    }
}
