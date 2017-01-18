package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCsclass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("csclass")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("���ִ�е�����"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("cscraft.admin")) {
                player.sendMessage(S.toPrefixRed("��û��Ȩ��"));
                return true;
            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("�����˵�"));
                player.sendMessage(S.toYellow("/" + label + " set <����> - �������װ������ְҵ��װ��"));
                player.sendMessage(S.toYellow("/" + label + " setinvisible <����> <true|false> - ����"));
                player.sendMessage(S.toYellow("/" + label + " health <����> <����ֵ> - �������ֵ"));
                player.sendMessage(S.toYellow("/" + label + " permission <����> <Ȩ��> - ����Ȩ��"));
                player.sendMessage(S.toYellow("/" + label + " delete <����> - ɾ��ְҵ"));
                player.sendMessage(S.toRed("    ��ɾ�����з����ڵĴ�ְҵ"));
                player.sendMessage(S.toYellow("/" + label + " list - �鿴ְҵ�б�"));
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) {
                    player.sendMessage(S.toPrefixYellow("/" + label + " set <����> - �������װ������ְҵ��װ��"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.setClass(player);
                } else {
                    csclass = new CSClass(args[1]);
                    csclass.setClass(player);
                }
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("setinvisible")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " setinvisible <����> <true|false> - ����"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    switch (args[2]) {
                        case "true":
                            csclass.setInvisible(true);
                            break;
                        case "false":
                            csclass.setInvisible(false);
                            break;
                        default:
                            player.sendMessage(S.toPrefixRed("��������"));
                            return true;
                    }
                    player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                } else {
                    player.sendMessage(S.toPrefixRed("ְҵ������"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("health")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " health <����> <����ֵ> - �������ֵ"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    int num;
                    try {
                        num = Integer.parseInt(args[2]);
                        if (num < 1) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        player.sendMessage(S.toPrefixRed("���ֲ���"));
                        return true;
                    }
                    csclass.setHealth(num);
                    player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                } else {
                    player.sendMessage(S.toPrefixRed("ְҵ������"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("permission")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " permission <����> <Ȩ��> - ����Ȩ��"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.setPermission(args[2]);
                    player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                } else {
                    player.sendMessage(S.toPrefixRed("ְҵ������"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " delete <����> - ɾ��ְҵ"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    for (Room room : Room.rooms.values()) {
                        room.getTeamAclass().remove(csclass);
                        room.saveTeamAclass();
                        room.getTeamBclass().remove(csclass);
                        room.saveTeamBclass();
                    }
                    csclass.remove();
                    player.sendMessage(S.toPrefixGreen("�ɹ�ɾ��"));
                } else {
                    player.sendMessage(S.toPrefixRed("ְҵ������"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("ְҵ�б�"));
                for (CSClass csclass : CSClass.classes.values()) {
                    player.sendMessage(S.toBoldDarkGreen("   - " + csclass.getName()));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("equip")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " equip <����>"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.equip(player);
                    player.sendMessage(S.toPrefixGreen("�ɹ�"));
                } else {
                    player.sendMessage(S.toPrefixRed("ְҵ������"));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
            return true;
        }
        return false;
    }
}