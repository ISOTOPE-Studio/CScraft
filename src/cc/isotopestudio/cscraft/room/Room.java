package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.CScraft;
import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.EffectPlace;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.players.PlayerInfo;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.*;

public abstract class Room {

    public static Map<String, Room> rooms = new HashMap<>();

    final PluginFile config;
    PluginFile msgData;

    // Settings
    private final String name;
    // pos must smaller than pos2
    private Location pos1;
    private Location pos2;
    private Location teamA;
    private Location teamB;
    private Location lobby;
    private int reqPlayerNum;
    private boolean useColorCap;
    private Set<CSClass> teamAclass = new HashSet<>();
    private Set<CSClass> teamBclass = new HashSet<>();
    private Set<EffectPlace> effects = new HashSet<>();
    private Map<Item, EffectPlace> effectItems = new HashMap<>();
    private List<String> rewards = new ArrayList<>();

    // In-game
    private RoomStatus status = RoomStatus.WAITING;
    private long scheduleStart = -1;
    private Set<Player> teamAplayer = new HashSet<>();
    private Set<Player> teamBplayer = new HashSet<>();
    private Set<Player> players = new HashSet<>();
    Map<Player, Scoreboard> scoreboards = new HashMap<>();
    private Map<Player, CSClass> playerClassMap = new HashMap<>();
    private Map<Player, Integer> playerKillsMap = new HashMap<>();
    private Map<Player, Integer> playerDeathMap = new HashMap<>();


    public Room(String name) {
        this.name = name;
        config = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/config.yml");
        roomFiles.add(config);

        rooms.put(name, this);
        loadFromConfig();
    }

    private void loadFromConfig() {
        pos1 = Util.stringToLocation(config.getString("pos1"));
        pos2 = Util.stringToLocation(config.getString("pos2"));
        teamA = Util.stringToLocation(config.getString("teamA"));
        teamB = Util.stringToLocation(config.getString("teamB"));
        lobby = Util.stringToLocation(config.getString("lobby"));
        reqPlayerNum = config.getInt("reqPlayerNum", 4);
        useColorCap = config.getBoolean("useColorCap", true);
        teamAclass.clear();
        teamAclass.addAll(CSClass.parseSet(config.getStringList("teamAclass")));
        teamBclass.clear();
        teamBclass.addAll(CSClass.parseSet(config.getStringList("teamBclass")));
        for (String s : config.getStringList("effectPlace")) {
            effects.add(EffectPlace.deserialize(this, s));
        }
        rewards.clear();
        if (!config.isSet("reward")) {
            rewards.add("give <player> MINECRAFT:DIAMOND 1");
            config.set("reward", rewards);
        } else {
            rewards = config.getStringList("reward");
        }
        config.save();
    }

    // Settings

    public String getName() {
        return name;
    }

    public String getMsg(String path) {
        return ChatColor.translateAlternateColorCodes('&', msgData.getString(path));
    }

    public List<String> getMsgList(String path) {
        List<String> list = new ArrayList<>();
        for (String line : msgData.getStringList(path)) {
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return list;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        config.set("pos1", Util.locationToString(pos1));
        config.save();
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        int x1, y1, z1, x2, y2, z2;
        if (pos1.getBlockX() > pos2.getBlockX()) {
            x1 = pos2.getBlockX();
            x2 = pos1.getBlockX();
        } else {
            x1 = pos1.getBlockX();
            x2 = pos2.getBlockX();
        }
        if (pos1.getBlockY() > pos2.getBlockY()) {
            y1 = pos2.getBlockY();
            y2 = pos1.getBlockY();
        } else {
            y1 = pos1.getBlockY();
            y2 = pos2.getBlockY();
        }
        if (pos1.getBlockZ() > pos2.getBlockZ()) {
            z1 = pos2.getBlockZ();
            z2 = pos1.getBlockZ();
        } else {
            z1 = pos1.getBlockZ();
            z2 = pos2.getBlockZ();
        }
        setPos1(new Location(pos1.getWorld(), x1, y1, z1));
        this.pos2 = new Location(pos1.getWorld(), x2, y2, z2);
        config.set("pos2", Util.locationToString(this.pos2));
        config.save();
    }

    public Location getTeamALocation() {
        return teamA;
    }

    public void setTeamALocation(Location teamA) {
        this.teamA = teamA;
        config.set("teamA", Util.locationToString(teamA));
        config.save();
    }

    public Location getTeamBLocation() {
        return teamB;
    }

    public void setTeamBLocation(Location teamB) {
        this.teamB = teamB;
        config.set("teamB", Util.locationToString(teamB));
        config.save();
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
        config.set("lobby", Util.locationToString(lobby));
        config.save();
    }

    public int getReqPlayerNum() {
        return reqPlayerNum;
    }

    public void setgetReqPlayerNum(int maxPlayer) {
        this.reqPlayerNum = maxPlayer;
        config.set("reqPlayerNum", maxPlayer);
        config.save();
    }

    public boolean isUseColorCap() {
        return useColorCap;
    }

    public void setUseColorCap(boolean useColorCap) {
        this.useColorCap = useColorCap;
        config.set("useColorCap", useColorCap);
        config.save();
    }

    /**
     * DON'T Write unless modifying settings
     */
    public Set<CSClass> getTeamAclass() {
        return teamAclass;
    }

    public void saveTeamAclass() {
        List<String> list = new ArrayList<>();
        for (CSClass csclass : teamAclass) {
            list.add(csclass.getName());
        }
        config.set("teamAclass", list);
        config.save();
    }

    /**
     * DON'T Write unless modifying settings
     */
    public Set<CSClass> getTeamBclass() {
        return teamBclass;
    }

    public void saveTeamBclass() {
        List<String> list = new ArrayList<>();
        for (CSClass csclass : teamBclass) {
            list.add(csclass.getName());
        }
        config.set("teamBclass", list);
        config.save();
    }

    public void addEffectPlace(Location location, Material material, PotionEffect effect, int cd) {
        effects.add(new EffectPlace(this, location, material, effect, cd));
        List<String> list = new ArrayList<>();
        for (EffectPlace effectPlace : effects) {
            list.add(effectPlace.serialize());
        }
        config.set("effectPlace", list);
        config.save();
    }

    public Set<EffectPlace> getEffectPlaces() {
        return effects;
    }

    public void remove() {
        config.getFile().delete();
        msgData.getFile().delete();
        config.getFile().getParentFile().delete();
        roomFiles.remove(config);
        msgFiles.remove(msgData);
        rooms.remove(name);
    }

    public boolean isReady() {
        return lobby != null && pos1 != null && pos2 != null && teamA != null && teamB != null
                && teamAclass.size() > 0 && teamBclass.size() > 0;
    }

    // In-game

    public long getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(long scheduleStart) {
        this.scheduleStart = scheduleStart;
    }


    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
        config.set("status", status.name());
        config.save();
    }

    public Map<Item, EffectPlace> getEffectItems() {
        return effectItems;
    }

    public void join(Player player) {
        playerData.set(player.getName(), null);
        playerData.set(player.getName() + ".created", new Date().getTime());
        playerData.set(player.getName() + ".room", name);
        Util.saveInventory(player, playerData.getConfigurationSection(player.getName()), "inventory");
        playerData.set(player.getName() + ".location", Util.locationToString(player.getLocation()));
        playerData.save();
        player.getInventory().clear();
        PlayerInfo.teleport(player, lobby);
        player.getInventory().setItem(0, GameItems.getClassItem());
        player.getInventory().setItem(8, GameItems.getExitItem());
        players.add(player);
        PlayerInfo.playerRoomMap.put(player, this);
        sendAllPlayersMsg(S.toPrefixAqua(player.getDisplayName()) + S.toAqua(" 加入房间"));

        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective(getMsg("name"), "Scoreboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        scoreboards.put(player, board);
        player.setScoreboard(board);

        updateScoreBoardAtLobby();
    }

    public void exit(Player player) {
        PlayerInfo.teleportOut(player);
        leave(player);
    }

    public void leave(Player player) {
        playerData.set(player.getName() + ".room", null);
        players.remove(player);
        if (teamAplayer.contains(player)) teamAplayer.remove(player);
        if (teamBplayer.contains(player)) teamBplayer.remove(player);
        PlayerInfo.playerRoomMap.remove(player);
        sendAllPlayersMsg(S.toPrefixAqua(player.getDisplayName()) + S.toAqua(" 退出房间"));
        playerData.save();
        scoreboards.remove(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void updateScoreBoardAtLobby() {
        for (Player player : players) {
            scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(getTeamAName()).setScore(getTeamAplayer().size());
            scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(getTeamBName()).setScore(getTeamBplayer().size());
        }
    }

    public void updateScoreBoardInGame() {
        for (Player player : players) {
            scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldGreen("击杀")).setScore(playerKillsMap.get(player));
            scoreboards.get(player).getObjective(DisplaySlot.SIDEBAR).getScore(S.toBoldRed("死亡")).setScore(playerDeathMap.get(player));
        }
    }

    /**
     * @return type name with chatcolor
     */
    @Override
    public abstract String toString();

    public Set<Player> getPlayers() {
        return players;
    }

    public Set<Player> getTeamAplayer() {
        return teamAplayer;
    }

    public Set<Player> getTeamBplayer() {
        return teamBplayer;
    }

    public Map<Player, CSClass> getPlayerClassMap() {
        return playerClassMap;
    }

    public Map<Player, Integer> getPlayerKillsMap() {
        return playerKillsMap;
    }

    public Map<Player, Integer> getPlayerDeathMap() {
        return playerDeathMap;
    }

    public void sendAllPlayersMsg(String msg) {
        for (Player player : players)
            player.sendMessage(msg);
    }

    public void prestart() {
        start();
    }

    public void start() {
        status = RoomStatus.PROGRESS;
        for (Player player : players) {
            playerKillsMap.put(player, 0);
            playerDeathMap.put(player, 0);
            playerEquip(player);
            new InvincibleListener(player);
            final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective(getMsg("name"), "Scoreboard");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            scoreboards.put(player, board);
            player.setScoreboard(board);
        }
        for (EffectPlace effectPlace : effects) {
            effectPlace.spawn();
        }
    }

    private static final PotionEffect INVISIBLE = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true);

    public void playerEquip(Player player) {
        if (useColorCap) {
            if (teamAplayer.contains(player)) {
                ItemStack cap = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta lch = (LeatherArmorMeta) cap.getItemMeta();
                lch.setColor(Color.fromRGB(255, 0, 0));
                cap.setItemMeta(lch);
                player.getEquipment().setHelmet(cap);
            } else {
                ItemStack cap = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta lch = (LeatherArmorMeta) cap.getItemMeta();
                lch.setColor(Color.fromRGB(0, 0, 255));
                cap.setItemMeta(lch);
                player.getEquipment().setHelmet(cap);
            }
        }
        CSClass csclass = playerClassMap.get(player);
        csclass.equip(player);
        player.getInventory().setItem(8, GameItems.getInfoItem());
        if (csclass.isInvisible()) {
            player.addPotionEffect(INVISIBLE);
        }
        player.setMaxHealth(csclass.getHealth());
        player.setHealth(csclass.getHealth());
    }

    public void playerDeath(Player killer, Player player, ItemStack item) {
        if (killer != null) {
            playerKillsMap.put(killer, playerKillsMap.get(killer) + 1);
            switch (playerKillsMap.get(killer)) {
                case (1):
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.1"), killer, player, item));
                    break;
                case (2):
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.2"), killer, player, item));
                    break;
                case (3):
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.3"), killer, player, item));
                    break;
                case (4):
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.4"), killer, player, item));
                    break;
                case (5):
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.5"), killer, player, item));
                    break;
                default:
                    sendAllPlayersMsg(getReplacedMsg(getMsg("msg.kill.n"), killer, player, item));
                    break;
            }
        }

        playerDeathMap.put(player, playerDeathMap.get(player) + 1);
        sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toYellow(" 死了"));
        player.setHealth(player.getMaxHealth());
        playerEquip(player);
        if (teamAplayer.contains(player))
            PlayerInfo.teleport(player, teamA);
        else
            PlayerInfo.teleport(player, teamB);
        new InvincibleListener(player);
    }

    public void teamAWin() {
        for (Player player : teamAplayer) {
            player.getInventory().clear();
            sendReward(player);
            for (String line : getMsgList("msg.win")) {
                player.sendMessage(getReplacedMsg(line, player, null, null));
            }
        }
        for (Player player : teamBplayer) {
            player.getInventory().clear();
            for (String line : getMsgList("msg.lose")) {
                player.sendMessage(getReplacedMsg(line, player, null, null));
            }
        }
        resetRoom();
    }

    public void teamBWin() {
        for (Player player : teamBplayer) {
            player.getInventory().clear();
            sendReward(player);
            for (String line : getMsgList("msg.win")) {
                player.sendMessage(getReplacedMsg(line, player, null, null));
            }
        }
        for (Player player : teamAplayer) {
            player.getInventory().clear();
            for (String line : getMsgList("msg.lose")) {
                player.sendMessage(getReplacedMsg(line, player, null, null));
            }
        }
        resetRoom();
    }

    public void sendReward(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String line : rewards) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replaceAll("<player>", player.getName()));
                }
            }
        }.runTaskLater(plugin, 1);
    }

    void resetRoom() {
        playerClassMap.clear();
        playerKillsMap.clear();
        playerDeathMap.clear();
        teamAplayer.clear();
        teamBplayer.clear();
        for (Item item : effectItems.keySet()) {
            item.remove();
        }
        status = RoomStatus.WAITING;
        for (Player player : new HashSet<>(players)) {
            exit(player);
        }
    }

    private String getReplacedMsg(String msg, Player player, Player enemy, ItemStack item) {
        msg = msg.replaceAll("<player>", getPlayerFullName(player))
                .replaceAll("<kill>", String.valueOf(playerKillsMap.get(player)))
                .replaceAll("<death>", String.valueOf(playerDeathMap.get(player)));
        if (enemy != null)
            msg = msg.replaceAll("<enemy>", getPlayerFullName(enemy));
        if (item != null)
            msg = msg.replaceAll("<item>", getItemName(item));
        return msg;
    }

    public String getPlayerFullName(Player player) {
        String result = "";
        if (playerClassMap.containsKey(player)) {
            result += "[" + playerClassMap.get(player).getDisplayName() + "]";
        }
        result += player.getDisplayName();
        return result;
    }

    private String getItemName(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() ?
                item.getItemMeta().getDisplayName() : item.getType().toString();
    }

    public String getTeamAName() {
        return S.toBoldRed("红队");
    }

    public String getTeamBName() {
        return S.toBoldDarkAqua("蓝队");
    }

    public static String name() {
        return S.toRed("错误!");
    }

    public String infoString() {
        return "- Room" + "\nname='" + name + '\'' +
                "\npos1=" + Util.locationToString(pos1) +
                "\npos2=" + Util.locationToString(pos2) +
                "\nteamA=" + Util.locationToString(teamA) +
                "\nteamB=" + Util.locationToString(teamB) +
                "\nlobby=" + Util.locationToString(lobby) +
                "\nreqPlayerNum=" + reqPlayerNum +
                "\nteamAclass=" + teamAclass +
                "\nteamBclass=" + teamBclass +
                "\neffects=" + effects +
                "\nrewards=" + rewards +
                "\nuseColorCap=" + useColorCap +
                "\nstatus=" + status +
                "\nscheduleStart=" + scheduleStart +
                "\nteamAplayer=" + Util.playerToStringSet(teamAplayer) +
                "\nteamBplayer=" + Util.playerToStringSet(teamBplayer) +
                "\nplayers=" + Util.playerToStringSet(players) +
                "\nplayerClassMap=" + Util.playerToStringSet(playerClassMap.keySet()) + "/" + playerClassMap.values() +
                "\nplayerKillsMap=" + Util.playerToStringSet(playerKillsMap.keySet()) + "/" + playerKillsMap.values() +
                "\nplayerDeathMap=" + Util.playerToStringSet(playerDeathMap.keySet()) + "/" + playerDeathMap.values();
    }


    static class InvincibleListener implements Listener {
        private final Player player;

        InvincibleListener(Player player) {
            this.player = player;
            Bukkit.getPluginManager().registerEvents(this, plugin);
            InvincibleListener listener = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    HandlerList.unregisterAll(listener);
                }
            }.runTaskLater(plugin, 20 * 5);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onDamage(EntityDamageEvent event) {
            if (event.getEntity().equals(player)) {
                event.setCancelled(true);
            }
        }
    }


}
