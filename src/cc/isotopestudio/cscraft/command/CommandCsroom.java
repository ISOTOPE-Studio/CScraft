package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.data.CSClass;
import cc.isotopestudio.cscraft.room.InfectRoom;
import cc.isotopestudio.cscraft.room.ProtectRoom;
import cc.isotopestudio.cscraft.room.Room;
import cc.isotopestudio.cscraft.room.TeamRoom;
import cc.isotopestudio.cscraft.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                player.sendMessage(S.toYellow("/" + label + " min <����> <�����������>"));
                player.sendMessage(S.toYellow("/" + label + " max <����> <����������>"));
                player.sendMessage(S.toYellow("/" + label + " effect <Ư����ƷID> <���ҩˮ> <ҩˮ�ȼ�> <ʱ��> <��ȴ> - ҩˮ"));
                player.sendMessage(S.toYellow("/" + label + " reward <����> �鿴���� (�������ļ�����ӽ���)"));
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
            if (args[0].equalsIgnoreCase("addclass")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " add <����ֵ> - ������е���ƷΪ��Ʒ"));
                    return true;
                }
                if (CSClass.classes.containsKey(args[1])) {
                    final CSClass csclass = CSClass.classes.get(args[1]);
                    csclass.setClass(player);

                } else {
                    final CSClass csclass = new CSClass(args[1]);
                    csclass.setClass(player);
                }

                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " remove <ID> - ɾ��һ����Ʒ(�б��е�ID)"));
                    return true;
                }
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(S.toPrefixRed("���ֲ���"));
                    return true;
                }
                player.sendMessage(S.toPrefixGreen("�ɹ�ɾ��"));
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("�����б�"));
                for (Room room : Room.rooms.values()) {
                    player.sendMessage("   " + S.toBoldDarkGreen(room.getName()) + " - " + S.toAqua(room.toString()));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
            return true;
        }
        return false;
    }
}