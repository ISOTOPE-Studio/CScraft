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
                player.sendMessage(S.toYellow("/" + label + " lobby <名字> - 等待大厅"));
                player.sendMessage(S.toYellow("/" + label + " pos1 <名字> - 设置当前位置为边界"));
                player.sendMessage(S.toYellow("/" + label + " pos2 <名字> - 设置当前位置为边界"));
                player.sendMessage(S.toYellow("/" + label + " teamA <名字> - 设置当前位置为A队出生点"));
                player.sendMessage(S.toYellow("/" + label + " teamB <名字> - 设置当前位置为B队出生点"));
                player.sendMessage(S.toYellow("/" + label + " addAclass <名字> <职业名字> - 添加A队职业"));
                player.sendMessage(S.toYellow("/" + label + " addBclass <名字> <职业名字> - 添加B队职业"));
                player.sendMessage(S.toYellow("/" + label + " min <名字> <最少玩家数量>"));
                player.sendMessage(S.toYellow("/" + label + " max <名字> <最大玩家数量>"));
                player.sendMessage(S.toYellow("/" + label + " effect <漂浮物品ID> <获得药水> <药水等级> <时间> <冷却> - 药水"));
                player.sendMessage(S.toYellow("/" + label + " reward <名字> 查看奖励 (在配置文件里添加奖励)"));
                player.sendMessage(S.toYellow("/" + label + " list - 查看房间列表"));
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " create <名字> <team|infect|protect> - 创建<团队|感染|守卫>"));
                    return true;
                }
                if (Room.rooms.containsKey(args[1])) {
                    player.sendMessage(S.toPrefixRed(args[1] + "已经存在"));
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
                        player.sendMessage(S.toPrefixRed("参数错误"));
                        return true;
                }
                player.sendMessage(S.toPrefixGreen("创建成功"));
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
                player.sendMessage(S.toPrefixYellow("房间列表"));
                for (Room room : Room.rooms.values()) {
                    player.sendMessage("   " + S.toBoldDarkGreen(room.getName()) + " - " + S.toAqua(room.toString()));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }
}