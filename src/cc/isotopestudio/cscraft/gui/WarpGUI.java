/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.cscraft.gui;

import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class WarpGUI extends GUI {

    public static Set<String> keys;

    private List<String> warps;
    private Map<Integer, String> slotIDMap;
    private List<String> favorites;

    public WarpGUI(Player player, int page) {
        super(S.toBoldGold("地标列表") + "[" + player.getName() + "]", 4 * 9, player);
        this.page = page;
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
