package cc.isotopestudio.cscraft.util;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Util {
    public static String locationToString(Location loc) {
        if (loc == null) return "";
        return loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
    }

    public static Location stringToLocation(String string) {
        if (string == null) return null;
        String[] s = string.split(" ");
        if (s.length != 4) return null;
        World world = Bukkit.getWorld(s[0]);
        int x = Integer.parseInt(s[1]);
        int y = Integer.parseInt(s[2]);
        int z = Integer.parseInt(s[3]);
        return new Location(world, x + .5, y, z + .5);
    }

    public static String potionEffectToString(PotionEffect effect) {
        return effect.getType().getName() + " " + effect.getAmplifier() + " " + effect.getDuration();
    }

    public static PotionEffect stringToPotionEffect(String string) {
        if (string == null) return null;
        String[] s = string.split(" ");
        if (s.length != 3) return null;
        PotionEffectType type = PotionEffectType.getByName(s[0]);
        int level = Integer.parseInt(s[1]);
        int time = Integer.parseInt(s[2]);
        return new PotionEffect(type, time, level);
    }

    public static Material getMaterialByName(String string) {
        if (string == null) return null;
        Material material = Material.getMaterial(string);
        if (material != null) return material;
        int id;
        try {
            id = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
        material = Material.getMaterial(id);
        if (material != null) return material;
        return null;
    }

    public static Map<Integer, ItemStack> saveInventory(Player player, ConfigurationSection config, String path) {
        Map<Integer, ItemStack> inventory = new HashMap<>();
        config.set(path, null);
        PlayerInventory inv = player.getInventory();
        ItemStack[] equipment = new ItemStack[4];
        equipment[0] = inv.getHelmet();
        equipment[1] = inv.getChestplate();
        equipment[2] = inv.getLeggings();
        equipment[3] = inv.getBoots();
        if (equipment[0] != null)
            config.set(path + ".equipment.helmet", equipment[0]);
        if (equipment[1] != null)
            config.set(path + ".equipment.chestplate", equipment[1]);
        if (equipment[2] != null)
            config.set(path + ".equipment.leggings", equipment[2]);
        if (equipment[3] != null)
            config.set(path + ".equipment.boots", equipment[3]);
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                ItemStack item = contents[i];
                if (item.getType() != Material.AIR) {
                    item = item.clone();
                    inventory.put(i, item);
                    config.set(path + ".item." + i, item);
                }
            }
        }
        return inventory;
    }

    public static void loadInventory(ConfigurationSection config, Player player) {
        player.getInventory().clear();
        if (config == null) return;
        ItemStack[] equipment = new ItemStack[4];
        equipment[0] = config.getItemStack("equipment.helmet");
        equipment[1] = config.getItemStack("equipment.chestplate");
        equipment[2] = config.getItemStack("equipment.leggings");
        equipment[3] = config.getItemStack("equipment.boots");
        Map<Integer, ItemStack> inventory = new HashMap<>();
        ConfigurationSection itemSection = config.getConfigurationSection("item");
        if (itemSection != null) {
            for (String key : itemSection.getKeys(false)) {
                inventory.put(Integer.parseInt(key), itemSection.getItemStack(key));
            }
        }
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

    public static Set<String> playerToStringSet(Collection<Player> players) {
        Set<String> set = new HashSet<>();
        for (Player player : players) {
            set.add(player.getName());
        }
        return set;
    }
}
