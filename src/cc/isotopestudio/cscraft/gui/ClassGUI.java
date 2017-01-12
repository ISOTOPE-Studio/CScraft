package cc.isotopestudio.cscraft.gui;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassGUI extends GUI {

    private Map<Integer, String> slotIDMap = new HashMap<>();

    public ClassGUI(Room room, Player player) {
        super(S.toBoldGold("—°‘Ò÷∞“µ") + "[" + player.getName() + "]", 2 * 9, player);
        this.page = 0;
        int pos = 0;
        List<CSClass> classes = new ArrayList<>();
        if (room.getTeamAplayer().contains(player)) {
            classes.addAll(room.getTeamAclass());
        } else if (room.getTeamBplayer().contains(player)) {
            classes.addAll(room.getTeamBclass());
        }
        for (CSClass csclass : classes) {
            if (pos >= size) break;
            slotIDMap.put(pos, csclass.getName());
            ItemStack item = new ItemStack(Material.WOOL);
            ItemMeta meta = item.getItemMeta();
            String itemName = csclass.getMsg("GUI.name");
            meta.setDisplayName(itemName == null ? csclass.getName() : itemName);
            List<String> lore = new ArrayList<>();
            lore.addAll(csclass.getMsgList("GUI.lore"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            setOption(pos, item);
            pos++;
        }
    }
}
