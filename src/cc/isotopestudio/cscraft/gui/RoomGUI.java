/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.cscraft.gui;

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RoomGUI extends GUI {

    public static Set<String> keys;

    private List<String> warps;
    private Map<Integer, String> slotIDMap;
    private List<String> favorites;

    public RoomGUI(Player player) {
        super(S.toBoldGold("加入游戏") + "[" + player.getName() + "]", 4 * 9, player);
        this.page = 0;
        int pos = 0;
        for (Room room : Room.rooms.values()) {
            if (pos >= size) break;
            ItemStack item = new ItemStack(Material.WOOL);
            switch (room.getStatus()) {
                case WAITING:
                    // change
                    item.setDurability((short) 5);
                    break;
                case PROGRESS:
                    // change
                    item.setDurability((short) 5);
                    break;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(S.toBoldRed(room.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(room.toString());
            lore.add(room.getPlayers().size()+"玩家");
            meta.setLore(lore);
            item.setItemMeta(meta);
            setOption(pos,item);
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

                player.closeInventory();
            }
        }
    }

}
