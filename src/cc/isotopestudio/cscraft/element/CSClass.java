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

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.*;

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
                    plugin.getLogger().warning("职业 " + classString + " 不存在");
                    continue;
                }
                result.add(classes.get(classString));
            }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CSClass{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
