package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.ProtectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.TeamRoom;
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
                sendHelpPage1(label, sender);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length < 2) {
                    sendHelpPage1(label, sender);
                    return true;
                }
                int page;
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendHelpPage1(label, sender);
                    return true;
                }
                if (page == 2) {
                    sendHelpPage2(label, sender);
                    return true;
                } else if (page == 3) {
                    sendHelpPage3(label, sender);
                    return true;
                } else {
                    sendHelpPage1(label, sender);
                    return true;
                }
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
            if (args[0].equalsIgnoreCase("num")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " min <����> <�����������>"));
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 2) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                if (num % 2 != 0) {
                    player.sendMessage(S.toPrefixRed("�����������Ϊż��"));
                    return true;
                }
                room.setgetReqPlayerNum(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("timeout")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " timeout <����> <�����Ϸʱ�� ����>"));
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                room.setGameTimeoutMin(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("colorcap")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " colorcap <����> <true|false> - ����ɫ��ñ��"));
                    return true;
                }
                switch (args[2]) {
                    case ("true"): {
                        room.setUseColorCap(true);
                        break;
                    }
                    case ("false"): {
                        room.setUseColorCap(false);
                        break;
                    }
                    default: {
                        player.sendMessage(S.toPrefixRed("��������"));
                        return true;
                    }
                }
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
                room.addEffectPlace(player.getLocation(), material, new PotionEffect(type, time * 20, level), cd);
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
                player.sendMessage(S.toBoldDarkAqua("    �������: ") +
                        S.toGreen("" + room.getReqPlayerNum()));
                player.sendMessage(S.toBoldDarkAqua("    �����Ϸʱ�� ����: ") +
                        S.toGreen("" + room.getGameTimeoutMin()));
                Set<String> set = new HashSet<>();
                if (room instanceof InfectRoom) {
                    InfectRoom infectRoom = (InfectRoom) room;
                    player.sendMessage(S.toBoldDarkAqua("    ��Ϸʱ��: ") +
                            S.toGreen("" + infectRoom.getGameMin()));
                    player.sendMessage(S.toBoldDarkAqua("    ĸ������: ") +
                            S.toGreen("" + infectRoom.getAntigenNum()));
                    CSClass teamZombieDefaultClass = infectRoom.getTeamZombieDefaultClass();
                    player.sendMessage(S.toBoldDarkAqua("    Ĭ�Ͻ�ʬְҵ: ") +
                            (teamZombieDefaultClass == null ? S.toRed("δ����") : S.toGreen(teamZombieDefaultClass.getName())));

                    for (CSClass csclass : infectRoom.getTeamAclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    ��ʬְҵ: ") +
                            S.toGreen(set.toString()));

                    CSClass teamAntigenDefaultClass = infectRoom.getTeamAntigenDefaultClass();
                    player.sendMessage(S.toBoldDarkAqua("    Ĭ��ĸ��ְҵ: ") +
                            (teamAntigenDefaultClass == null ? S.toRed("δ����") : S.toGreen(teamAntigenDefaultClass.getName())));

                    set.clear();
                    for (CSClass csclass : infectRoom.getTeamAntigenClass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    ĸ��ְҵ: ") +
                            S.toGreen(set.toString()));

                    set.clear();
                    for (CSClass csclass : infectRoom.getTeamBclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    ����ְҵ: ") +
                            S.toGreen(set.toString()));
                } else {
                    for (CSClass csclass : room.getTeamAclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    ��Aְҵ: ") +
                            S.toGreen(set.toString()));
                    set.clear();
                    for (CSClass csclass : room.getTeamBclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    ��Bְҵ: ") +
                            S.toGreen(set.toString()));
                }
                if (room instanceof TeamRoom) {
                    TeamRoom teamRoom = (TeamRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    Ŀ������: ") +
                            S.toGreen("" + teamRoom.getGoal()));
                }
                if (room instanceof ProtectRoom) {
                    ProtectRoom protectRoom = (ProtectRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    ʵ��Ѫ��: ") +
                            S.toGreen("" + protectRoom.getHealth()));
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
            if (args[0].equalsIgnoreCase("goal")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " goal <����> <����> - Ŀ������"));
                    return true;
                }
                if (!(room instanceof TeamRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + TeamRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                ((TeamRoom) room).setGoal(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("entityA")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " entityA <����> - ����ʵ��A��λ��"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + ProtectRoom.name());
                    return true;
                }
                ((ProtectRoom) room).setEntityA(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("entityB")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " entityB <����> - ����ʵ��B��λ��"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + ProtectRoom.name());
                    return true;
                }
                ((ProtectRoom) room).setEntityB(player.getLocation());
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("health")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " health <����> <����> - ʵ������ֵ"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + ProtectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                ((ProtectRoom) room).setHealth(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("antigenNum")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " antigenNum <����> <����> - ����ĸ������"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                if (num >= room.getReqPlayerNum()) {
                    player.sendMessage(S.toPrefixRed("ĸ������������������̫С��"));
                    return true;
                }
                ((InfectRoom) room).setAntigenNum(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("time")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " time <����> <����> - ������Ϸʱ��"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                if (num <= room.getGameTimeoutMin()) {
                    player.sendMessage(S.toPrefixRed("ʱ�䲻�ܱȳ�ʱʱ���"));
                    return true;
                }
                ((InfectRoom) room).setGameMin(num);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("defaultAntigen")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " defaultAntigen <����> <ְҵ����> - ����Ĭ��ĸ��ְҵ"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                infectRoom.setTeamAntigenDefaultClass(csclass);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("defaultZombie")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " defaultZombie <����> <ְҵ����> - ����ʵ�彩ʬְҵ"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                infectRoom.setTeamZombieDefaultClass(csclass);
                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addAntigenClass")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " addAntigenClass <����> <ְҵ����> - ���ĸ��ְҵ"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (infectRoom.getTeamAntigenClass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "�Ѿ����б�����"));
                    return true;
                }
                infectRoom.getTeamAntigenClass().add(csclass);
                infectRoom.saveTeamAntigenClass();
                player.sendMessage(S.toPrefixGreen("�ɹ����"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeAclass")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " removeAntigenClass <����> <ְҵ����> - �h��ĸ��ְҵ"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("�˷��䲻��") + InfectRoom.name());
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!infectRoom.getTeamAntigenClass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "������"));
                    return true;
                }
                infectRoom.getTeamAntigenClass().remove(csclass);
                infectRoom.saveTeamAntigenClass();
                player.sendMessage(S.toPrefixGreen("�ɹ��h��"));
                return true;
            }
            player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
            return true;
        }
        return false;
    }

    private void sendHelpPage1(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("�����˵� 1"));
        sender.sendMessage(S.toYellow("/" + label + " create <����> <team|infect|protect> - ����<�Ŷ�|��Ⱦ|����>"));
        sender.sendMessage(S.toYellow("/" + label + " lobby <����> - �ȴ�����"));
        sender.sendMessage(S.toYellow("/" + label + " pos1 <����> - ���õ�ǰλ��Ϊ�߽�"));
        sender.sendMessage(S.toYellow("/" + label + " pos2 <����> - ���õ�ǰλ��Ϊ�߽�"));
        sender.sendMessage(S.toYellow("/" + label + " teamA <����> - ���õ�ǰλ��ΪA�ӳ�����"));
        sender.sendMessage(S.toYellow("/" + label + " teamB <����> - ���õ�ǰλ��ΪB�ӳ�����"));
        sender.sendMessage(S.toYellow("/" + label + " addAclass <����> <ְҵ����> - ���A��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " addBclass <����> <ְҵ����> - ���B��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " removeAclass <����> <ְҵ����> - �h��A��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " removeBclass <����> <ְҵ����> - �h��A��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " num <����> <�������>"));
        sender.sendMessage(S.toYellow("/" + label + " timeout <����> <�����Ϸʱ�� ����>"));
        sender.sendMessage(S.toYellow("/" + label + " colorcap <true|false> - ����ɫ��ñ��"));
        sender.sendMessage(S.toYellow("/" + label + " info <����> - �鿴��Ϣ"));
        sender.sendMessage(S.toYellow("/" + label + " list - �鿴�����б�"));
        sender.sendMessage(S.toYellow("/csreload - ������������"));
        sender.sendMessage(S.toYellow("/" + label + " help 2"));
    }

    private void sendHelpPage2(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("�����˵� 2"));
        sender.sendMessage(S.toYellow("/" + label + " effect <����> <Ư����ƷID> <���ҩˮ> <ҩˮ�ȼ�> <ʱ��> <��ȴ> - ҩˮ"));
        sender.sendMessage(S.toYellow("/" + label + " reward <����> �鿴���� (�������ļ�����ӽ���)"));
        sender.sendMessage(S.toYellow("/" + label + " remove <����> - ɾ��һ������"));
        sender.sendMessage(" - " + TeamRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " goal <����> <����> - Ŀ������"));
        sender.sendMessage(" - " + ProtectRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " entityA <����> - ����ʵ��A��λ��"));
        sender.sendMessage(S.toYellow("/" + label + " entityB <����> - ����ʵ��B��λ��"));
        sender.sendMessage(S.toYellow("/" + label + " health <����> <����ֵ> - ʵ������ֵ"));
        sender.sendMessage(S.toYellow("/" + label + " help 3"));

    }

    private void sendHelpPage3(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("�����˵� 3"));
        sender.sendMessage(" - " + InfectRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " time <����> <����> - ������Ϸʱ��"));
        sender.sendMessage(S.toYellow("/" + label + " antigenNum <����> <����> - ����ĸ������"));
        sender.sendMessage(S.toYellow("/" + label + " defaultAntigen <����> <ְҵ����> - ����Ĭ��ĸ��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " defaultZombie <����> <ְҵ����> - ����ʵ�彩ʬְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " addAclass <����> <ְҵ����> - ��ӽ�ʬְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " addBclass <����> <ְҵ����> - �������ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " addAntigenClass <����> <ְҵ����> - ���ĸ��ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " removeAclass <����> <ְҵ����> - �h����ʬְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " removeBclass <����> <ְҵ����> - �h������ְҵ"));
        sender.sendMessage(S.toYellow("/" + label + " removeAntigenClass <����> <ְҵ����> - �h��ĸ��ְҵ"));

    }

}