package cc.isotopestudio.cscraft.players.listener;
/*
 * Created by david on 2017/1/25.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.CScraft;
import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.gui.ClassGUI;
import cc.isotopestudio.cscraft.gui.InfoGUI;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static cc.isotopestudio.cscraft.element.GameItems.*;
import static cc.isotopestudio.cscraft.players.PlayerInfo.playerRoomMap;

public class GUIItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getItem() == null) return;
        if (!playerRoomMap.containsKey(player)) return;
        Room room = playerRoomMap.get(player);
        if (room.getStatus() == RoomStatus.WAITING) {
            event.setCancelled(true);
            if (itemEquals(getExitItem(), event.getItem())) {
                room.exit(player);
                player.sendMessage(S.toPrefixGreen("退出房间"));
            } else if (itemEquals(getTeam1Item(), event.getItem())) {
                // switch to team A
                if (room.getTeamAplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在") + Room.TEAMANAME + S.toRed("了"));
                } else {
                    if (room.getTeamAplayer().size() >= room.getReqPlayerNum() / 2) {
                        player.sendMessage(CScraft.prefix + Room.TEAMANAME + S.toRed("已满员"));
                        return;
                    }
                    room.getTeamBplayer().remove(player);
                    room.getTeamAplayer().add(player);
                    room.getPlayerClassMap().remove(player);
                    player.getEquipment().setHelmet(addPlayerLore(getRedTeamCap(), player));
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + Room.TEAMANAME);
                }
            } else if (itemEquals(getTeam2Item(), event.getItem())) {
                // switch to team B
                if (room.getTeamBplayer().contains(player)) {
                    player.sendMessage(S.toPrefixRed("你已经在") + Room.TEAMBNAME + S.toRed("了"));
                } else {
                    if (room.getTeamBplayer().size() >= room.getReqPlayerNum() / 2) {
                        player.sendMessage(CScraft.prefix + Room.TEAMBNAME + S.toRed("已满员"));
                        return;
                    }
                    room.getTeamAplayer().remove(player);
                    room.getTeamBplayer().add(player);
                    room.getPlayerClassMap().remove(player);
                    player.getEquipment().setHelmet(addPlayerLore(getBlueTeamCap(), player));
                    room.sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toGreen(" 加入") + Room.TEAMBNAME);
                }
            } else if (itemEquals(getClassItem(), event.getItem())) {
                if (!(room instanceof InfectRoom)) {
                    new ClassGUI(room, player, room.getTeamAplayer().contains(player) ? room.getTeamAclass() : room.getTeamBclass()).open(player);
                }
            } else if (itemEquals(getHumanClassItem(), event.getItem())) {
                if (room instanceof InfectRoom)
                    new ClassGUI(room, player, room.getTeamBclass()).open(player);
            }
        } else {
            if (itemEquals(getInfoItem(), event.getItem())) {
                event.setCancelled(true);
                new InfoGUI(room, player).open(player);
            } else if (itemEquals(getAntigenClassItem(), event.getItem())) {
                if (room instanceof InfectRoom)
                    new ClassGUI(room, player, ((InfectRoom) room).getTeamAntigenClass()).open(player);
            } else if (itemEquals(getZombieClassItem(), event.getItem())) {
                if (room instanceof InfectRoom)
                    new ClassGUI(room, player, room.getTeamAclass()).open(player);
            }
        }
    }

}
