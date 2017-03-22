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
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;

            if (!player.hasPermission("cscraft.admin")) {
                player.sendMessage(S.toPrefixRed("你没有权限"));
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
            if (args[0].equalsIgnoreCase("num")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " min <名字> <最少玩家数量>"));
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 2) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (num % 2 != 0) {
                    player.sendMessage(S.toPrefixRed("玩家数量必须为偶数"));
                    return true;
                }
                room.setgetReqPlayerNum(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("timeout")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " timeout <名字> <最大游戏时长 分钟>"));
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                room.setGameTimeoutMin(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("colorcap")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " colorcap <房间> <true|false> - 带颜色的帽子"));
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
                        player.sendMessage(S.toPrefixRed("参数不对"));
                        return true;
                    }
                }
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
                room.addEffectPlace(player.getLocation(), material, new PotionEffect(type, time * 20, level), cd);
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
                player.sendMessage(S.toBoldDarkAqua("    玩家数量: ") +
                        S.toGreen("" + room.getReqPlayerNum()));
                player.sendMessage(S.toBoldDarkAqua("    最大游戏时长 分钟: ") +
                        S.toGreen("" + room.getGameTimeoutMin()));
                Set<String> set = new HashSet<>();
                if (room instanceof InfectRoom) {
                    InfectRoom infectRoom = (InfectRoom) room;
                    player.sendMessage(S.toBoldDarkAqua("    游戏时间: ") +
                            S.toGreen("" + infectRoom.getGameMin()));
                    player.sendMessage(S.toBoldDarkAqua("    母体数量: ") +
                            S.toGreen("" + infectRoom.getAntigenNum()));
                    CSClass teamZombieDefaultClass = infectRoom.getTeamZombieDefaultClass();
                    player.sendMessage(S.toBoldDarkAqua("    默认僵尸职业: ") +
                            (teamZombieDefaultClass == null ? S.toRed("未设置") : S.toGreen(teamZombieDefaultClass.getName())));

                    for (CSClass csclass : infectRoom.getTeamAclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    僵尸职业: ") +
                            S.toGreen(set.toString()));

                    CSClass teamAntigenDefaultClass = infectRoom.getTeamAntigenDefaultClass();
                    player.sendMessage(S.toBoldDarkAqua("    默认母体职业: ") +
                            (teamAntigenDefaultClass == null ? S.toRed("未设置") : S.toGreen(teamAntigenDefaultClass.getName())));

                    set.clear();
                    for (CSClass csclass : infectRoom.getTeamAntigenClass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    母体职业: ") +
                            S.toGreen(set.toString()));

                    set.clear();
                    for (CSClass csclass : infectRoom.getTeamBclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    人类职业: ") +
                            S.toGreen(set.toString()));
                } else {
                    for (CSClass csclass : room.getTeamAclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    队A职业: ") +
                            S.toGreen(set.toString()));
                    set.clear();
                    for (CSClass csclass : room.getTeamBclass()) {
                        set.add(csclass.getName());
                    }
                    player.sendMessage(S.toBoldDarkAqua("    队B职业: ") +
                            S.toGreen(set.toString()));
                }
                if (room instanceof TeamRoom) {
                    TeamRoom teamRoom = (TeamRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    目标人数: ") +
                            S.toGreen("" + teamRoom.getGoal()));
                }
                if (room instanceof ProtectRoom) {
                    ProtectRoom protectRoom = (ProtectRoom) room;
                    player.sendMessage(S.toBoldDarkGreen("    实体血量: ") +
                            S.toGreen("" + protectRoom.getHealth()));
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
            if (args[0].equalsIgnoreCase("goal")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " goal <名字> <数量> - 目标人数"));
                    return true;
                }
                if (!(room instanceof TeamRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + TeamRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                ((TeamRoom) room).setGoal(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("entityA")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " entityA <名字> - 设置实体A队位置"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + ProtectRoom.name());
                    return true;
                }
                ((ProtectRoom) room).setEntityA(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("entityB")) {
                if (args.length < 2) {
                    sender.sendMessage(S.toYellow("/" + label + " entityB <名字> - 设置实体B队位置"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + ProtectRoom.name());
                    return true;
                }
                ((ProtectRoom) room).setEntityB(player.getLocation());
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("health")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " health <名字> <数量> - 实体生命值"));
                    return true;
                }
                if (!(room instanceof ProtectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + ProtectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                ((ProtectRoom) room).setHealth(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("antigenNum")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " antigenNum <名字> <数量> - 设置母体数量"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (num >= room.getReqPlayerNum()) {
                    player.sendMessage(S.toPrefixRed("母体数量相比于玩家数量太小了"));
                    return true;
                }
                ((InfectRoom) room).setAntigenNum(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("time")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " time <名字> <分钟> - 设置游戏时间"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                int num;
                try {
                    num = Integer.parseInt(args[2]);
                    if (num < 1) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    player.sendMessage(S.toPrefixRed("数字不对"));
                    return true;
                }
                if (num <= room.getGameTimeoutMin()) {
                    player.sendMessage(S.toPrefixRed("时间不能比超时时间短"));
                    return true;
                }
                ((InfectRoom) room).setGameMin(num);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("defaultAntigen")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " defaultAntigen <名字> <职业名字> - 设置默认母体职业"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                infectRoom.setTeamAntigenDefaultClass(csclass);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("defaultZombie")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " defaultZombie <名字> <职业名字> - 设置实体僵尸职业"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                infectRoom.setTeamZombieDefaultClass(csclass);
                player.sendMessage(S.toPrefixGreen("成功设置"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addAntigenClass")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " addAntigenClass <名字> <职业名字> - 添加母体职业"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                if (!CSClass.classes.containsKey(args[2])) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (infectRoom.getTeamAntigenClass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "已经在列表中了"));
                    return true;
                }
                infectRoom.getTeamAntigenClass().add(csclass);
                infectRoom.saveTeamAntigenClass();
                player.sendMessage(S.toPrefixGreen("成功添加"));
                return true;
            }
            if (args[0].equalsIgnoreCase("removeAclass")) {
                if (args.length < 3) {
                    sender.sendMessage(S.toYellow("/" + label + " removeAntigenClass <名字> <职业名字> - 刪除母体职业"));
                    return true;
                }
                if (!(room instanceof InfectRoom)) {
                    player.sendMessage(S.toPrefixRed("此房间不是") + InfectRoom.name());
                    return true;
                }
                InfectRoom infectRoom = (InfectRoom) room;
                CSClass csclass = CSClass.getClassByName(args[2]);
                if (!infectRoom.getTeamAntigenClass().contains(csclass)) {
                    player.sendMessage(S.toPrefixRed(args[2] + "不存在"));
                    return true;
                }
                infectRoom.getTeamAntigenClass().remove(csclass);
                infectRoom.saveTeamAntigenClass();
                player.sendMessage(S.toPrefixGreen("成功刪除"));
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知命令, 输入 /" + label + " 查看帮助"));
            return true;
        }
        return false;
    }

    private void sendHelpPage1(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("帮助菜单 1"));
        sender.sendMessage(S.toYellow("/" + label + " create <名字> <team|infect|protect> - 创建<团队|感染|守卫>"));
        sender.sendMessage(S.toYellow("/" + label + " lobby <名字> - 等待大厅"));
        sender.sendMessage(S.toYellow("/" + label + " pos1 <名字> - 设置当前位置为边界"));
        sender.sendMessage(S.toYellow("/" + label + " pos2 <名字> - 设置当前位置为边界"));
        sender.sendMessage(S.toYellow("/" + label + " teamA <名字> - 设置当前位置为A队出生点"));
        sender.sendMessage(S.toYellow("/" + label + " teamB <名字> - 设置当前位置为B队出生点"));
        sender.sendMessage(S.toYellow("/" + label + " addAclass <名字> <职业名字> - 添加A队职业"));
        sender.sendMessage(S.toYellow("/" + label + " addBclass <名字> <职业名字> - 添加B队职业"));
        sender.sendMessage(S.toYellow("/" + label + " removeAclass <名字> <职业名字> - 刪除A队职业"));
        sender.sendMessage(S.toYellow("/" + label + " removeBclass <名字> <职业名字> - 刪除A队职业"));
        sender.sendMessage(S.toYellow("/" + label + " num <名字> <玩家数量>"));
        sender.sendMessage(S.toYellow("/" + label + " timeout <名字> <最大游戏时长 分钟>"));
        sender.sendMessage(S.toYellow("/" + label + " colorcap <true|false> - 带颜色的帽子"));
        sender.sendMessage(S.toYellow("/" + label + " info <名字> - 查看信息"));
        sender.sendMessage(S.toYellow("/" + label + " list - 查看房间列表"));
        sender.sendMessage(S.toYellow("/csreload - 重载所有配置"));
        sender.sendMessage(S.toYellow("/" + label + " help 2"));
    }

    private void sendHelpPage2(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("帮助菜单 2"));
        sender.sendMessage(S.toYellow("/" + label + " effect <名字> <漂浮物品ID> <获得药水> <药水等级> <时间> <冷却> - 药水"));
        sender.sendMessage(S.toYellow("/" + label + " reward <名字> 查看奖励 (在配置文件里添加奖励)"));
        sender.sendMessage(S.toYellow("/" + label + " remove <名字> - 删除一个房间"));
        sender.sendMessage(" - " + TeamRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " goal <名字> <数量> - 目标人数"));
        sender.sendMessage(" - " + ProtectRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " entityA <名字> - 设置实体A队位置"));
        sender.sendMessage(S.toYellow("/" + label + " entityB <名字> - 设置实体B队位置"));
        sender.sendMessage(S.toYellow("/" + label + " health <名字> <生命值> - 实体生命值"));
        sender.sendMessage(S.toYellow("/" + label + " help 3"));

    }

    private void sendHelpPage3(String label, CommandSender sender) {
        sender.sendMessage(S.toPrefixGreen("帮助菜单 3"));
        sender.sendMessage(" - " + InfectRoom.name());
        sender.sendMessage(S.toYellow("/" + label + " time <名字> <分钟> - 设置游戏时间"));
        sender.sendMessage(S.toYellow("/" + label + " antigenNum <名字> <数量> - 设置母体数量"));
        sender.sendMessage(S.toYellow("/" + label + " defaultAntigen <名字> <职业名字> - 设置默认母体职业"));
        sender.sendMessage(S.toYellow("/" + label + " defaultZombie <名字> <职业名字> - 设置实体僵尸职业"));
        sender.sendMessage(S.toYellow("/" + label + " addAclass <名字> <职业名字> - 添加僵尸职业"));
        sender.sendMessage(S.toYellow("/" + label + " addBclass <名字> <职业名字> - 添加人类职业"));
        sender.sendMessage(S.toYellow("/" + label + " addAntigenClass <名字> <职业名字> - 添加母体职业"));
        sender.sendMessage(S.toYellow("/" + label + " removeAclass <名字> <职业名字> - 刪除僵尸职业"));
        sender.sendMessage(S.toYellow("/" + label + " removeBclass <名字> <职业名字> - 刪除人类职业"));
        sender.sendMessage(S.toYellow("/" + label + " removeAntigenClass <名字> <职业名字> - 刪除母体职业"));

    }

}