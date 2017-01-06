package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.data.CSClass;
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
                player.sendMessage(S.toYellow("/" + label + " addclass <����> <ְҵ����> - ������е���ƷΪ��Ʒ"));

                player.sendMessage(S.toYellow("/" + label + " list - �鿴��Ʒ�б�"));
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
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " add <����ֵ> - ������е���ƷΪ��Ʒ"));
                    return true;
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

                return true;
            }
            player.sendMessage(S.toPrefixRed("δ֪����, ���� /" + label + " �鿴����"));
            return true;
        }
        return false;
    }
}