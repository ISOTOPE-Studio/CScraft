package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.room.*;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class CommandCsroom implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("csroom")) {
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
                player.sendMessage(S.toYellow("/" + label + " create <����> <team|infect|protect> - ����<�Ŷ�|��Ⱦ|����>"));
                player.sendMessage(S.toYellow("/" + label + " lobby <����> - �ȴ�����"));
                player.sendMessage(S.toYellow("/" + label + " pos1 <����> - ���õ�ǰλ��Ϊ�߽�"));
                player.sendMessage(S.toYellow("/" + label + " pos2 <����> - ���õ�ǰλ��Ϊ�߽�"));
                player.sendMessage(S.toYellow("/" + label + " teamA <����> - ���õ�ǰλ��ΪA�ӳ�����"));
                player.sendMessage(S.toYellow("/" + label + " teamB <����> - ���õ�ǰλ��ΪB�ӳ�����"));
                player.sendMessage(S.toYellow("/" + label + " addAclass <����> <ְҵ����> - ���A��ְҵ"));
                player.sendMessage(S.toYellow("/" + label + " addBclass <����> <ְҵ����> - ���B��ְҵ"));
                player.sendMessage(S.toYellow("/" + label + " removeAclass <����> <ְҵ����> - �h��A��ְҵ"));
                player.sendMessage(S.toYellow("/" + label + " removeBclass <����> <ְҵ����> - �h��A��ְҵ"));
                player.sendMessage(S.toYellow("/" + label + " min <����> <�����������>"));
                player.sendMessage(S.toYellow("/" + label + " max <����> <����������>"));
                player.sendMessage(S.toYellow("/" + label + " effect <Ư����ƷID> <���ҩˮ> <ҩˮ�ȼ�> <ʱ��> <��ȴ> - ҩˮ"));
                player.sendMessage(S.toYellow("/" + label + " reward <����> �鿴���� (�������ļ�����ӽ���)"));
                player.sendMessage(S.toYellow("/" + label + " remove <����> - ɾ��һ������"));
                player.sendMessage(S.toYellow("/" + label + " info <����> - �鿴��Ϣ"));
                player.sendMessage(S.toYellow("/" + label + " list - �鿴�����б�"));
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " create <����> <team|infect|protect> - ����<�Ŷ�|��Ⱦ|����>"));
                    return true;
                }
                if (Room.rooms.containsKey(args[1])) {
                    player.sendMessage(S.toPrefixRed(args[1] + "�Ѿ�����"));
                    return true;
                }
                switch (args[2]) {
                    case "team":
                        new TeamRoom(args[1]);
                        break;
                    case "infect":
                        new InfectRoom(args[1]);
                        break;
                    case "protect":
                        new ProtectRoom(args[1]);
                        break;
                    default:
                        player.sendMessage(S.toPrefixRed("��������"));
                        return true;
                }
                player.sendMessage(S.toPrefixGreen("�����ɹ�"));
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("�����б�"));
                for (Room room : Room.rooms.values()) {
                    player.sendMessage("   " + S.toBoldDarkGreen(room.getName()) + " - " + S.toAqua(room.toString()));
                }
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
                return true;
            }

            if (!Room.rooms.containsKey(args[1])) {
                player.sendMessage(S.toPrefixRed(args[1] + "������"));
                return true;
            }
            Room room = Room.rooms.get(args[1]);
            if (args[0].equalsIgnoreCase("lobby")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " lobby <����> - �ȴ�����"));
                    return true;
                }
                room.setLobby(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("pos1")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " pos1 <����> - ���õ�ǰλ��Ϊ�߽�"));
                    return true;
                }
                room.setPos1(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " pos2 <����> - ���õ�ǰλ��Ϊ�߽�"));
                    return true;
                }
                room.setPos2(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("teamA")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " teamA <����> - ���õ�ǰλ��ΪA�ӳ�����"));
                    return true;
                }
                room.setTeamALocation(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("teamB")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " teamB <����> - ���õ�ǰλ��ΪB�ӳ�����"));
                    return true;
                }
                room.setTeamBLocation(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addAclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " addAclass <����> <ְҵ����> - ���A��ְҵ"));
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (room.getTeamAclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "�Ѿ����б�����"));
                    return true;
                }
                room.getTeamAclass().add(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("�ɹ����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addBclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " addBclass <����> <ְҵ����> - ���B��ְҵ"));
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (room.getTeamBclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "�Ѿ����б�����"));
                    return true;
                }
                room.getTeamBclass().add(csclass);
                room.saveTeamBclass();
                player.sendMessage(S.toPrefixGreen("�ɹ����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeAclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " removeAclass <����> <ְҵ����> - �h��A��ְҵ"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!room.getTeamAclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                room.getTeamAclass().remove(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("�ɹ��h��"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeBclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " removeBclass <����> <ְҵ����> - �h��B��ְҵ"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!room.getTeamBclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                room.getTeamBclass().remove(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("�ɹ��h��"));
                return true;
            }
            if (args[0].equalsIgnoreCase("min")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " min <����> <�����������>"));
                    return true;
                }
                int min;
                try {
                    min = Integer.parseInt(args[2]);
                    if (min < 2) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                if (min > room.getMaxPlayer()) {
                    player.sendMessage(S.toPrefixRed("��С����������ܴ�������������"));
                    return true;
                }
                room.setMinPlayer(min);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("max")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " max <����> <����������>"));
                    return true;
                }
                int max;
                try {
                    max = Integer.parseInt(args[2]);
                    if (max < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                if (max < room.getMinPlayer()) {
                    player.sendMessage(S.toPrefixRed("��������������С����С�������"));
                    return true;
                }
                room.setMaxPlayer(max);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("effect")) {
                if (args.length < 6) {
                    player.sendMessage(S.toYellow("/" + label + " effect <����> <Ư����ƷID> <���ҩˮ> <ҩˮ�ȼ�> <ʱ��> <��ȴ> - ҩˮ"));
                    return true;
                }
                boolean failed = false;
                Material material = Util.getMaterialByName(args[2]);
                if (material == null) {
                    player.sendMessage(S.toPrefixRed("��ƷID/���Ʋ�����"));
                    failed = true;
                }
                PotionEffectType type = PotionEffectType.getByName(args[3]);
                if (type == null) {
                    player.sendMessage(S.toPrefixRed("ҩˮ���Ʋ�����"));
                    failed = true;
                }
                int level = 0;
                try {
                    level = Integer.parseInt(args[4]);
                    if (level < 0 || level > 255) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("ҩˮ�ȼ����ֲ���"));
                    failed = true;
                }
                int time = 0;
                try {
                    time = Integer.parseInt(args[5]);
                    if (time < 0 || time > 255) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("ʱ�����ֲ���"));
                    failed = true;
                }
                int cd = -1;
                if (args.length > 6)
                    try {
                        cd = Integer.parseInt(args[6]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(S.toPrefixRed("��ȴʱ�����ֲ���"));
                        failed = true;
                    }
                if (failed) {
                    return true;
                }
                room.addEffectPlace(player.getLocation(), material, new PotionEffect(type, time, level), cd);
                player.sendMessage(S.toPrefixGreen("�ɹ����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " info <����> - �鿴��Ϣ"));
                    return true;
                }
                player.sendMessage(S.toPrefixAqua(" INFO - ") + room.toString() + " " + room.getName());
                player.sendMessage(S.toBoldDarkAqua("    ����: ") +
                        (room.getLobby() != null ? S.toGreen("������") : S.toRed("δ����")));
                player.sendMessage(S.toBoldDarkAqua("    POS1: ") +
                        (room.getPos1() != null ? S.toGreen("������") : S.toRed("δ����")));
                player.sendMessage(S.toBoldDarkAqua("    POS2: ") +
                        (room.getPos2() != null ? S.toGreen("������") : S.toRed("δ����")));
                player.sendMessage(S.toBoldDarkAqua("    ��A������: ") +
                        (room.getTeamALocation() != null ? S.toGreen("������") : S.toRed("δ����")));
                player.sendMessage(S.toBoldDarkAqua("    ��B������: ") +
                        (room.getTeamBLocation() != null ? S.toGreen("������") : S.toRed("δ����")));
                player.sendMessage(S.toBoldDarkAqua("    ��С/�����: ") +
                        S.toGreen(room.getMinPlayer() + " / " + room.getMaxPlayer()));
                Set<String> set = new HashSet<>();
                for (CSClass csclass : room.getTeamAclass()) {
                    set.add(csclass.getName());
                }
                player.sendMessage(S.toBoldDarkAqua("    ��Aְҵ: ") +
                        S.toGreen(set.toString()));
                set = new HashSet<>();
                for (CSClass csclass : room.getTeamBclass()) {
                    set.add(csclass.getName());
                }
                player.sendMessage(S.toBoldDarkAqua("    ��Bְҵ: ") +
                        S.toGreen(set.toString()));
                if (room instanceof TeamRoom) {
                    TeamRoom teamRoom = (TeamRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    Ŀ������: ") +
                            S.toGreen("" + teamRoom.getGoal()));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " remove <����> - ɾ��һ������"));
                    return true;
                }
                if (room.getStatus() == RoomStatus.WAITING) {
                    room.remove();
                    player.sendMessage(S.toPrefixGreen("�ɹ�ɾ��"));
                } else {
                    player.sendMessage(S.toPrefixRed("�˷�����Ϸ��"));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
            return true;
        }
        return false;
    }
}