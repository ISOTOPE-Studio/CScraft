package cc.isotopestudio.cscraft.gui;
/*
 * Created by Mars Tan on 1/5/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InfoGUI extends GUI {

    private static final ItemStack redGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
    private static final ItemStack blueGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
    private static final ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);

    private Room room;

    public InfoGUI(Room room, Player player) {
        super(S.toBoldGold("’Ωº®") + "[" + player.getName() + "]", 6 * 9, player);
        this.room = room;
        List<Player> teamA = new ArrayList<>(room.getTeamAplayer());
        List<Player> teamB = new ArrayList<>(room.getTeamBplayer());
        for (int pos = 0; pos < 54; pos++) {
            switch (pos % 9) {
                case (0):
                    setOption(pos, redGlass);
                    continue;
                case (4):
                    setOption(pos, blackGlass);
                    continue;
                case (8):
                    setOption(pos, blueGlass);
                    continue;
            }
            if (pos % 9 < 4) {
                if (teamA.size() > 0) {
                    setOption(pos, getItem(teamA.remove(0)));
                }
            } else {
                if (teamB.size() > 0) {
                    setOption(pos, getItem(teamB.remove(0)));
                }
            }
        }
    }

    private ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        item.setDurability((short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(player.getDisplayName());
        List<String> lore = new ArrayList<>();
        if (room.getTeamAplayer().contains(player))
            lore.add(S.toBoldGold("∂”ŒÈ: " + room.getTeamAName()));
        else
            lore.add(S.toBoldGold("∂”ŒÈ: " + room.getTeamBName()));
        lore.add(S.toBoldGold("ª˜…±: ") + S.toBoldGreen("" + room.getPlayerKillsMap().get(player)));
        lore.add(S.toBoldGold("À¿Õˆ: ") + S.toBoldRed("" + room.getPlayerDeathMap().get(player)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
