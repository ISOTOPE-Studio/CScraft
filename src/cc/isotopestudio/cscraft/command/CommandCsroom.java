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
                player.sendMessage(S.toYellow("/" + label + " removeAclass <名字> <职业名字> - 刪除A队职业"));
                player.sendMessage(S.toYellow("/" + label + " removeBclass <名字> <职业名字> - 刪除A队职业"));
                player.sendMessage(S.toYellow("/" + label + " min <名字> <最少玩家数量>"));
                player.sendMessage(S.toYellow("/" + label + " max <名字> <最大玩家数量>"));
                player.sendMessage(S.toYellow("/" + label + " effect <漂浮物品ID> <获得药水> <药水等级> <时间> <冷却> - 药水"));
                player.sendMessage(S.toYellow("/" + label + " reward <名字> 查看奖励 (在配置文件里添加奖励)"));
                player.sendMessage(S.toYellow("/" + label + " remove <名字> - 删除一个房间"));
                player.sendMessage(S.toYellow("/" + label + " info <名字> - 查看信息"));
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
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage(S.toPrefixYellow("房间列表"));
                for (Room room : Room.rooms.values()) {
                    player.sendMessage("   " + S.toBoldDarkGreen(room.getName()) + " - " + S.toAqua(room.toString()));
                }
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
                return true;
            }

            if (!Room.rooms.containsKey(args[1])) {
                player.sendMessage(S.toPrefixRed(args[1] + "不存在"));
                return true;
            }
            Room room = Room.rooms.get(args[1]);
            if (args[0].equalsIgnoreCase("lobby")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " lobby <名字> - 等待大厅"));
                    return true;
                }
                room.setLobby(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("pos1")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " pos1 <名字> - 设置当前位置为边界"));
                    return true;
                }
                room.setPos1(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("pos2")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " pos2 <名字> - 设置当前位置为边界"));
                    return true;
                }
                room.setPos2(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("teamA")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " teamA <名字> - 设置当前位置为A队出生点"));
                    return true;
                }
                room.setTeamALocation(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("teamB")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " teamB <名字> - 设置当前位置为B队出生点"));
                    return true;
                }
                room.setTeamBLocation(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addAclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " addAclass <名字> <职业名字> - 添加A队职业"));
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (room.getTeamAclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "已经在列表中了"));
                    return true;
                }
                room.getTeamAclass().add(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("成功添加"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addBclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " addBclass <名字> <职业名字> - 添加B队职业"));
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (room.getTeamBclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "已经在列表中了"));
                    return true;
                }
                room.getTeamBclass().add(csclass);
                room.saveTeamBclass();
                player.sendMessage(S.toPrefixGreen("成功添加"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeAclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " removeAclass <名字> <职业名字> - 刪除A队职业"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!room.getTeamAclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                room.getTeamAclass().remove(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("成功刪除"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeBclass")) {
                if (args.length < 3) {
                    player.sendMessage(S.toYellow("/" + label + " removeBclass <名字> <职业名字> - 刪除B队职业"));
                    return true;
                }
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!room.getTeamBclass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                room.getTeamBclass().remove(csclass);
                room.saveTeamAclass();
                player.sendMessage(S.toPrefixGreen("成功刪除"));
                return true;
            }
            if (args[0].equalsIgnoreCase("min")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " min <名字> <最少玩家数量>"));
                    return true;
                }
                int min;
                try {
                    min = Integer.parseInt(args[2]);
                    if (min < 2) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (min > room.getMaxPlayer()) {
                    player.sendMessage(S.toPrefixRed("最小玩家数量不能大于最大玩家数量"));
                    return true;
                }
                room.setMinPlayer(min);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("max")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " max <名字> <最大玩家数量>"));
                    return true;
                }
                int max;
                try {
                    max = Integer.parseInt(args[2]);
                    if (max < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (max < room.getMinPlayer()) {
                    player.sendMessage(S.toPrefixRed("最大玩家数量不能小于最小玩家数量"));
                    return true;
                }
                room.setMaxPlayer(max);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("effect")) {
                if (args.length < 6) {
                    player.sendMessage(S.toYellow("/" + label + " effect <房间> <漂浮物品ID> <获得药水> <药水等级> <时间> <冷却> - 药水"));
                    return true;
                }
                boolean failed = false;
                Material material = Util.getMaterialByName(args[2]);
                if (material == null) {
                    player.sendMessage(S.toPrefixRed("物品ID/名称不存在"));
                    failed = true;
                }
                PotionEffectType type = PotionEffectType.getByName(args[3]);
                if (type == null) {
                    player.sendMessage(S.toPrefixRed("药水名称不存在"));
                    failed = true;
                }
                int level = 0;
                try {
                    level = Integer.parseInt(args[4]);
                    if (level < 0 || level > 255) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("药水等级数字不对"));
                    failed = true;
                }
                int time = 0;
                try {
                    time = Integer.parseInt(args[5]);
                    if (time < 0 || time > 255) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("时间数字不对"));
                    failed = true;
                }
                int cd = -1;
                if (args.length > 6)
                    try {
                        cd = Integer.parseInt(args[6]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(S.toPrefixRed("冷却时间数字不对"));
                        failed = true;
                    }
                if (failed) {
                    return true;
                }
                room.addEffectPlace(player.getLocation(), material, new PotionEffect(type, time, level), cd);
                player.sendMessage(S.toPrefixGreen("成功添加"));
                return true;
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " info <名字> - 查看信息"));
                    return true;
                }
                player.sendMessage(S.toPrefixAqua(" INFO - ") + room.toString() + " " + room.getName());
                player.sendMessage(S.toBoldDarkAqua("    大厅: ") +
                        (room.getLobby() != null ? S.toGreen("已设置") : S.toRed("未设置")));
                player.sendMessage(S.toBoldDarkAqua("    POS1: ") +
                        (room.getPos1() != null ? S.toGreen("已设置") : S.toRed("未设置")));
                player.sendMessage(S.toBoldDarkAqua("    POS2: ") +
                        (room.getPos2() != null ? S.toGreen("已设置") : S.toRed("未设置")));
                player.sendMessage(S.toBoldDarkAqua("    队A出生点: ") +
                        (room.getTeamALocation() != null ? S.toGreen("已设置") : S.toRed("未设置")));
                player.sendMessage(S.toBoldDarkAqua("    队B出生点: ") +
                        (room.getTeamBLocation() != null ? S.toGreen("已设置") : S.toRed("未设置")));
                player.sendMessage(S.toBoldDarkAqua("    最小/大玩家: ") +
                        S.toGreen(room.getMinPlayer() + " / " + room.getMaxPlayer()));
                Set<String> set = new HashSet<>();
                for (CSClass csclass : room.getTeamAclass()) {
                    set.add(csclass.getName());
                }
                player.sendMessage(S.toBoldDarkAqua("    队A职业: ") +
                        S.toGreen(set.toString()));
                set = new HashSet<>();
                for (CSClass csclass : room.getTeamBclass()) {
                    set.add(csclass.getName());
                }
                player.sendMessage(S.toBoldDarkAqua("    队B职业: ") +
                        S.toGreen(set.toString()));
                if (room instanceof TeamRoom) {
                    TeamRoom teamRoom = (TeamRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    目标人数: ") +
                            S.toGreen("" + teamRoom.getGoal()));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " remove <名字> - 删除一个房间"));
                    return true;
                }
                if (room.getStatus() == RoomStatus.WAITING) {
                    room.remove();
                    player.sendMessage(S.toPrefixGreen("成功删除"));
                } else {
                    player.sendMessage(S.toPrefixRed("此房间游戏中"));
                }
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }
}