package cc.isotopestudio.cscraft.gui;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.CScraft;
import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ClassGUI extends GUI {

    private Map<Integer, String> slotIDMap = new HashMap<>();
    private Room room;

    public ClassGUI(Room room, Player player, Collection<CSClass> classes) {
        super(S.toBoldGold("选择职业") + "[" + player.getName() + "]", 2 * 9, player);
        this.room = room;
        this.page = 0;
        int pos = 0;
        for (CSClass csclass : classes) {
            if (pos >= size) break;
            if (csclass.getPermission() != null && !player.hasPermission(csclass.getPermission())) {
                setOption(pos++, GameItems.getNoPermission());
                continue;
            }
            slotIDMap.put(pos, csclass.getName());
            Material material = Util.getMaterialByName(csclass.getMsg("GUI.item"));
            ItemStack item;
            item = material != null ? new ItemStack(material) : new ItemStack(Material.WOOL);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot < 0 || slot >= size) {
                return;
            }

            if (optionIcons[slot] != null) {
                CSClass csclass = CSClass.classes.get(slotIDMap.get(slot));
                if (csclass == null) {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                } else {
                    room.sendAllPlayersMsg(CScraft.prefix + room.getPlayerFullName(player) + S.toYellow(" 选择职业 ")
                            + csclass.getDisplayName());
                    room.getPlayerClassMap().put(player, csclass);
                }
                player.closeInventory();
            }
        }
    }
}
