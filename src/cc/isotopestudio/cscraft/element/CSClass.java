package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.*;
import static cc.isotopestudio.cscraft.element.GameItems.*;

public class CSClass {

    public static final Map<String, CSClass> classes = new HashMap<>();

    private ConfigurationSection config;

    private final String name;
    private final Map<Integer, ItemStack> inventory = new HashMap<>();
    private final ItemStack[] equipment = new ItemStack[4];
    private int health;
    private boolean invisible = false;
    private String permission;
    private final List<PotionEffect> effects = new ArrayList<>();

    private static final PotionEffect SATURATION = new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 1);

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
        effects.add(SATURATION);
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
        invisible = config.getBoolean("invisible");
        permission = config.getString("permission");
        health = config.getInt("health");
        for (String line : config.getStringList("effects"))
            effects.add(Util.stringToPotionEffect(line));
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        String name = getMsg("name");
        if (name != null) return name;
        return this.name + ChatColor.RESET;
    }

    public void setClass(Player player) {
        inventory.putAll(Util.saveInventory(player, config, "inventory"));
        classData.save();
    }

    public void equip(Player player) {
        if (equipment[0] != null)
            player.getInventory().setHelmet(addPlayerLore(equipment[0], player));
        if (equipment[1] != null)
            player.getInventory().setChestplate(addPlayerLore(equipment[1], player));
        if (equipment[2] != null)
            player.getInventory().setLeggings(addPlayerLore(equipment[2], player));
        if (equipment[3] != null)
            player.getInventory().setBoots(addPlayerLore(equipment[3], player));
        for (int i : inventory.keySet())
            player.getInventory().setItem(i, addPlayerLore(inventory.get(i), player));
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        config.set("invisible", invisible);
        classData.save();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
        config.set("permission", permission);
        classData.save();
    }

    public int getHealth() {
        if (health == 0) return 20;
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        config.set("health", health);
        classData.save();
    }

    public void addEffect(PotionEffect effect) {
        effects.add(effect);
        List<String> list = new ArrayList<>();
        for (PotionEffect potionEffect : effects) {
            list.add(Util.potionEffectToString(potionEffect));
        }
        config.set("effects", list);
        classData.save();
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public void remove() {
        classes.remove(name);
        classData.set(name, null);
        classData.save();
    }

    public String getMsg(String path) {
        String string = classMsgData.getString(name + "." + path);
        if (string == null) return null;
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> getMsgList(String path) {
        List<String> list = new ArrayList<>();
        for (String line : classMsgData.getStringList(name + "." + path)) {
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return list;
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

    @Override
    public String toString() {
        return "CSClass{" + "config=" + config +
                ", name='" + name + '\'' +
                ", inventory=" + inventory +
                ", equipment=" + (equipment == null ? "null" : Arrays.asList(equipment).toString()) +
                ", health=" + health +
                ", invisible=" + invisible +
                ", permission='" + permission + '\'' +
                ", effects=" + effects +
                '}';
    }
}
