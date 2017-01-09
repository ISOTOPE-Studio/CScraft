package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.classData;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class CSClass {

    public static final Map<String, CSClass> classes = new HashMap<>();

    private ConfigurationSection config;

    private final String name;
    private final Map<Integer, ItemStack> inventory = new HashMap<>();
    private final ItemStack[] equipment = new ItemStack[4];
    private boolean invisible = false;

    public CSClass(String name) {
        this.name = name;
        config = classData.getConfigurationSection(name);
        if (config == null) {
            classData.set(name + ".created", new Date().getTime());
            classData.save();
            config = classData.getConfigurationSection(name);
        } else {
            loadFromConfig();
        }
        classes.put(name, this);
    }

    private void loadFromConfig() {
        equipment[0] = config.getItemStack("inventory.equipment.helmet");
        equipment[1] = config.getItemStack("inventory.equipment.chestplate");
        equipment[2] = config.getItemStack("inventory.equipment.leggings");
        equipment[3] = config.getItemStack("inventory.equipment.boots");
        ConfigurationSection itemSection = config.getConfigurationSection("inventory.item");
        if (itemSection != null) {
            for (String key : itemSection.getKeys(false)) {
                inventory.put(Integer.parseInt(key), itemSection.getItemStack(key));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setClass(Player player) {
        config.set("inventory", null);
        PlayerInventory inv = player.getInventory();
        equipment[0] = inv.getHelmet();
        equipment[1] = inv.getChestplate();
        equipment[2] = inv.getLeggings();
        equipment[3] = inv.getBoots();
        if (equipment[0] != null)
            config.set("inventory.equipment.helmet", equipment[0]);
        if (equipment[1] != null)
            config.set("inventory.equipment.chestplate", equipment[1]);
        if (equipment[2] != null)
            config.set("inventory.equipment.leggings", equipment[2]);
        if (equipment[3] != null)
            config.set("inventory.equipment.boots", equipment[3]);
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                ItemStack item = contents[i];
                if (item.getType() != Material.AIR) {
                    item = item.clone();
                    inventory.put(i, item);
                    config.set("inventory.item." + i, item);
                }
            }
        }
        classData.save();
    }

    public void equip(Player player) {
        player.getInventory().clear();
        if (equipment[0] != null)
            player.getInventory().setHelmet(equipment[0]);
        if (equipment[1] != null)
            player.getInventory().setChestplate(equipment[1]);
        if (equipment[2] != null)
            player.getInventory().setLeggings(equipment[2]);
        if (equipment[3] != null)
            player.getInventory().setBoots(equipment[3]);
        for (int i : inventory.keySet())
            player.getInventory().setItem(i, inventory.get(i));
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        config.set("invisible", invisible);
        classData.save();
    }

    public void remove() {
        classes.remove(name);
        classData.set(name, null);
        classData.save();
    }

    public static CSClass getClassByName(String name) {
        if (classes.containsKey(name))
            return classes.get(name);
        return null;
    }

    public static Set<CSClass> parseSet(Collection<String> stringSet) {
        Set<CSClass> result = new HashSet<>();
        if (stringSet != null)
            for (String classString : stringSet) {
                if (!classes.containsKey(classString)) {
                    plugin.getLogger().warning("ְҵ " + classString + " ������");
                    continue;
                }
                result.add(classes.get(classString));
            }
        return result;
    }
}