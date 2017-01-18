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
                player.sendMessage(S.toYellow("/" + label + " set <名字> - 背包里的装备当此职业的装备"));
                player.sendMessage(S.toYellow("/" + label + " setinvisible <名字> <true|false> - 隐身"));
                player.sendMessage(S.toYellow("/" + label + " health <名字> <生命值> - 最大生命值"));
                player.sendMessage(S.toYellow("/" + label + " permission <名字> <权限> - 设置权限"));
                player.sendMessage(S.toYellow("/" + label + " delete <名字> - 删除职业"));
                player.sendMessage(S.toRed("    会删除所有房间内的此职业"));
                player.sendMessage(S.toYellow("/" + label + " list - 查看职业列表"));
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length < 2) {
                    player.sendMessage(S.toPrefixYellow("/" + label + " set <名字> - 背包里的装备当此职业的装备"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.setClass(player);
                } else {
                    csclass = new CSClass(args[1]);
                    csclass.setClass(player);
                }
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("setinvisible")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " setinvisible <名字> <true|false> - 隐身"));
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
                            player.sendMessage(S.toPrefixRed("参数错误"));
                            return true;
                    }
                    player.sendMessage(S.toPrefixGreen("成功设置"));
                } else {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("health")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " health <名字> <生命值> - 最大生命值"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    int num;
                    try {
                        num = Integer.parseInt(args[2]);
                        if (num < 1) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        player.sendMessage(S.toPrefixRed("数字不对"));
                        return true;
                    }
                    csclass.setHealth(num);
                    player.sendMessage(S.toPrefixGreen("成功设置"));
                } else {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("permission")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " permission <名字> <权限> - 设置权限"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.setPermission(args[2]);
                    player.sendMessage(S.toPrefixGreen("成功设置"));
                } else {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " delete <名字> - 删除职业"));
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
                    player.sendMessage(S.toPrefixGreen("成功删除"));
                } else {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("职业列表"));
                for (CSClass csclass : CSClass.classes.values()) {
                    player.sendMessage(S.toBoldDarkGreen("   - " + csclass.getName()));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("equip")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " equip <名字>"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[1]);
                if (csclass != null) {
                    csclass.equip(player);
                    player.sendMessage(S.toPrefixGreen("成功"));
                } else {
                    player.sendMessage(S.toPrefixRed("职业不存在"));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }
}