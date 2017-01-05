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
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("cscraft.admin")) {
                player.sendMessage(S.toPrefixRed("你没有权限"));
                return true;
            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("帮助菜单"));
                player.sendMessage(S.toYellow("/" + label + " create <名字> <team|infect|protect> - 创建<团队|感染|守卫>"));
                player.sendMessage(S.toYellow("/" + label + " addclass <名字> <职业名字> - 添加手中的物品为奖品"));

                player.sendMessage(S.toYellow("/" + label + " list - 查看奖品列表"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addclass")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " add <幸运值> - 添加手中的物品为奖品"));
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
                    player.sendMessage(S.toYellow("/" + label + " add <幸运值> - 添加手中的物品为奖品"));
                    return true;
                }

                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " remove <ID> - 删除一个奖品(列表中的ID)"));
                    return true;
                }
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                player.sendMessage(S.toPrefixGreen("成功删除"));
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {

                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }
}