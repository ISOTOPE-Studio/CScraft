package cc.isotopestudio.cscraft.task;
/*
 * Created by Mars Tan on 3/22/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.Room;
import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.cscraft.CScraft.plugin;
import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;

public class InvisibleArmorTask extends BukkitRunnable {
    @Override
    public void run() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, ConnectionSide.SERVER_SIDE,
                        Packets.Server.ENTITY_EQUIPMENT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (!playerRoomMap.containsKey(event.getPlayer())) {
                            return;
                        }
                        Player receiver = event.getPlayer();
                        Room room = playerRoomMap.get(receiver);
                        if(room.getStatus() == RoomStatus.WAITING) return;
                        PacketContainer packet = event.getPacket();
                        ItemStack stack = packet.getItemModifier().read(0);
                        if (stack == null) return;
                        if (!stack.hasItemMeta() || !stack.getItemMeta().hasLore()) {
                            return;
                        }
                        System.out.println(receiver.getName());
                        Player player = null;
                        for (String s : stack.getItemMeta().getLore()) {
                            s = ChatColor.stripColor(s);
                            if (s.startsWith("[PLAYER] ")) {
                                player = Bukkit.getPlayerExact(s.replace("[PLAYER] ", ""));
                            }
                        }
                        if (player == null) return;
                        System.out.println(" " + player.getName());
                        if (room.getPlayerClassMap().get(player).isInvisible()) {
                            System.out.println(" " + stack);
                            event.setPacket(packet = packet.deepClone());
                            stack = packet.getItemModifier().read(0);
                            stack.setType(Material.GHAST_TEAR);
                        }
                    }
                });
    }
}
