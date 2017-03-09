package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.element.RoomStatus;
import cc.isotopestudio.cscraft.players.PlayerInfo;
import cc.isotopestudio.cscraft.util.ParticleEffect;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.msgFiles;
import static cc.isotopestudio.cscraft.CScraft.plugin;

public class InfectRoom extends Room {

    // Team Antigen: Antigen
    // Team A: Zombie + Antigen
    // Team B: Human

    // Settings
    // teamAclass for zombie
    // teamBclass for Human
    private CSClass teamAntigenDefaultClass;
    private CSClass teamZombieDefaultClass;
    private Set<CSClass> teamAntigenClass = new HashSet<>();

    private int antigenNum;

    // In-game
    private Set<Player> teamAntigenPlayer = new HashSet<>();
    private Set<Player> hasSelectedNewClass = new HashSet<>();
    public int antigenCounting = -1;

    public InfectRoom(String name) {
        super(name);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg_infect.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);
        antigenNum = config.getInt("antigenNum");
        teamAntigenDefaultClass = CSClass.getClassByName(config.getString("teamAntigenDefaultClass"));
        teamZombieDefaultClass = CSClass.getClassByName(config.getString("teamZombieDefaultClass"));
        teamAntigenClass.clear();
        teamAntigenClass.addAll(CSClass.parseSet(config.getStringList("teamAntigenClass")));
    }

    public int getAntigenNum() {
        return antigenNum;
    }

    public void setAntigenNum(int antigenNum) {
        this.antigenNum = antigenNum;
        config.set("antigenNum", antigenNum);
        config.save();
    }

    public CSClass getTeamAntigenDefaultClass() {
        return teamAntigenDefaultClass;
    }


    public void setTeamAntigenDefaultClass(CSClass teamAntigenDefaultClass) {
        this.teamAntigenDefaultClass = teamAntigenDefaultClass;
        config.set("teamAntigenDefaultClass", teamAntigenDefaultClass.getName());
        config.save();
    }

    public CSClass getTeamZombieDefaultClass() {
        return teamZombieDefaultClass;
    }

    public void setTeamZombieDefaultClass(CSClass teamZombieDefaultClass) {
        this.teamZombieDefaultClass = teamZombieDefaultClass;
        config.set("teamZombieDefaultClass", teamZombieDefaultClass.getName());
        config.save();
    }

    public Set<CSClass> getTeamAntigenClass() {
        return teamAntigenClass;
    }

    public void saveTeamAntigenClass() {
        List<String> list = new ArrayList<>();
        for (CSClass csclass : teamAntigenClass) {
            list.add(csclass.getName());
        }
        config.set("teamAntigenClass", list);
        config.save();
    }

    public Set<Player> getTeamAntigenPlayers() {
        return teamAntigenPlayer;
    }

    @Override
    public boolean isReady() {
        return super.isReady() && teamAntigenDefaultClass != null && teamZombieDefaultClass != null
                && teamAntigenClass.size() > 0 && antigenNum > 0;
    }


    @Override
    public void join(Player player) {
        super.join(player);
        getTeamBplayer().add(player);
        player.getInventory().setItem(0, GameItems.getHumanClassItem());
    }

    @Override
    public void leave(Player player) {
        super.leave(player);
        if (teamAntigenPlayer.contains(player))
            teamAntigenPlayer.remove(player);
    }

    @Override
    public void updateScoreBoardAtLobby() {
    }

    @Override
    public void updateScoreBoardInGame() {
        for (Player player : getPlayers()) {
            final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective(getMsg("name"), "Scoreboard");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            scoreboards.put(player, board);
            player.setScoreboard(board);
            board.getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("击杀")).setScore(getPlayerKillsMap().get(player));
            board.getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("死亡")).setScore(getPlayerDeathMap().get(player));
        }
    }

    private final static PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, 20 * 10, 10);
    private final static PotionEffect DAMAGE_RESISTANCE = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 10);
    private final static PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 10, 10);

    @Override
    public void start() {
        super.start();
        List<Player> players = new ArrayList<>(getPlayers());
        Collections.shuffle(players);
        for (int i = 0; i < antigenNum; i++) {
            Player player = players.get(i);
            getTeamBplayer().remove(player);
            teamAntigenPlayer.add(player);
            player.sendMessage(S.toPrefixGreen("你变成了母体, 请选择职业"));
            PlayerInfo.clearInventory(player);
            player.getInventory().setItem(5, GameItems.getAntigenClassItem());
            player.addPotionEffect(SLOW);
            player.addPotionEffect(DAMAGE_RESISTANCE);
            player.addPotionEffect(INVISIBILITY);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!hasSelectedNewClass.contains(player)) {
                        playerJoinClass(player, teamAntigenDefaultClass);
                        player.sendMessage(S.toPrefixRed("选择职业超时，你成为了默认职业"
                                + teamAntigenDefaultClass.getDisplayName()));
                    }
                }
            }.runTaskLater(plugin, 10 * 20);
        }
        antigenCounting = 10;
    }

    @Override
    public void playerDeath(Player killer, Player player, ItemStack item) {
        super.playerDeath(killer, player, item);
        if (getTeamAplayer().contains(player)) {
            // Zombie dies

        } else if (getTeamBplayer().contains(player)) {
            // Human dies
            getTeamAplayer().add(player);
            getTeamBplayer().remove(player);
            player.sendMessage(S.toPrefixGreen("你被感染了, 请选择职业"));
            sendAllPlayersMsg(getPlayerFullName(player) + "被感染了");
            player.getInventory().setItem(5, GameItems.getZombieClassItem());
            player.addPotionEffect(SLOW);
            player.addPotionEffect(DAMAGE_RESISTANCE);
            player.addPotionEffect(INVISIBILITY);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!hasSelectedNewClass.contains(player)) {
                        playerJoinClass(player, teamZombieDefaultClass);
                        player.sendMessage(S.toPrefixRed("选择职业超时，你成为了默认职业"
                                + teamZombieDefaultClass.getDisplayName()));
                    }
                }
            }.runTaskLater(plugin, 10 * 20);
        } else {
            // Antigen dies

        }
    }

    @Override
    public void playerJoinClass(Player player, CSClass csclass) {
        super.playerJoinClass(player, csclass);
        if (getStatus() == RoomStatus.PROGRESS) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            playerEquip(player);
            hasSelectedNewClass.add(player);
        }
    }

    @Override
    void resetRoom() {
        super.resetRoom();
        teamAntigenPlayer.clear();
        hasSelectedNewClass.clear();
    }

    @Override
    public String getPlayerTeamName(Player player) {
        if (getTeamAplayer().contains(player))
            return S.toBoldRed("僵尸");
        else if (teamAntigenPlayer.contains(player))
            return S.toBoldDarkAqua("母体");
        else
            return S.toBoldRed("人类");
    }

    @Override
    public String toString() {
        return name();
    }

    public static String name() {
        return S.toBoldDarkGreen("感染模式");
    }

    @Override
    public String infoString() {
        return super.infoString() +
                "\nzombie=" + Util.playerToStringSet(getTeamAplayer()) +
                "\nantigen=" + Util.playerToStringSet(getTeamAntigenPlayers()) +
                "\nhuman=" + Util.playerToStringSet(getTeamBplayer());
    }
}
