/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.cscraft.gui;

import cc.isotopestudio.cscraft.element.RoomStatus;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomGUI extends GUI {

    private Map<Integer, String> slotIDMap = new HashMap<>();

    public RoomGUI(Player player) {
        super(S.toBoldGold("加入游戏") + "[" + player.getName() + "]", 4 * 9, player);
        this.page = 0;
        int pos = 0;
        for (Room room : Room.rooms.values()) {
            if (pos >= size) break;
            if (!room.isReady()) {
                continue;
            }
            slotIDMap.put(pos, room.getName());
            ItemStack item = new ItemStack(Material.WOOL);

            int playerNum = room.getPlayers().size();
            if (playerNum < 2)
                item.setAmount(1);
            else if (playerNum >= 64)
                item.setAmount(64);
            else
                item.setAmount(playerNum);

            switch (room.getStatus()) {
                case WAITING:
                    // change
                    item.setDurability((short) 5);
                    break;
                case PROGRESS:
                    // change
                    item.setDurability((short) 14);
                    break;
            }
            ItemMeta meta = item.getItemMeta();
            String title = room.getMsg("GUI.title");
            if (title != null)
                meta.setDisplayName(title);
            else
                meta.setDisplayName(S.toBoldRed(room.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(S.toGreen("玩家: " + playerNum + " / " + room.getReqPlayerNum()));
            lore.addAll(room.getMsgList("GUI.lore"));
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
                Room room = Room.rooms.get(slotIDMap.get(slot));
                if (room == null) {
                    player.sendMessage(S.toPrefixRed("房间不存在"));
                } else {
                    if (room.getStatus() == RoomStatus.PROGRESS) {
                        return;
                    } else if (room.getPlayers().size() >= room.getReqPlayerNum()) {
                        player.sendMessage(S.toPrefixRed("玩家数量已达最大值"));
                    } else {
                        room.join(player);
                        player.sendMessage(S.toPrefixGreen("传送到大厅"));
                    }
                }
                player.closeInventory();
            }
        }
    }

}
