package cc.isotopestudio.cscraft.command;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCscraft implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cscraft")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("luckycat.admin")) {
                player.sendMessage(S.toPrefixRed("你没有权限"));
                return true;
            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("帮助菜单"));
                player.sendMessage(S.toYellow("/" + label + " setlot - 设置手中的物品为抽奖卷"));
                player.sendMessage(S.toYellow("/" + label + " add <幸运值> - 添加手中的物品为奖品"));
                player.sendMessage(S.toYellow("/" + label + " remove <ID> - 删除一个奖品(列表中的ID)"));
                player.sendMessage(S.toYellow("/" + label + " removeall - 删除所有奖品和玩家"));
                player.sendMessage(S.toYellow("/" + label + " removerecords - 删除所有玩家的中奖纪录"));
                player.sendMessage(S.toYellow("/" + label + " list - 查看奖品列表"));
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