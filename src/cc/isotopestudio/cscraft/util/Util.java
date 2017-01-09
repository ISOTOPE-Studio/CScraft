package cc.isotopestudio.cscraft.util;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Util {
    public static String locationToString(Location loc) {
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
        return new Location(world, x, y, z);
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
}
