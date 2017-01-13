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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

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
    private Set<CSClass> teamAclass = new HashSet<>();
    private Set<CSClass> teamBclass = new HashSet<>();
    private Set<EffectPlace> effects = new HashSet<>();
    private Map<Item, EffectPlace> effectItems = new HashMap<>();

    // In-game
    private RoomStatus status = RoomStatus.WAITING;
    private long scheduleStart = -1;
    private Set<Player> teamAplayer = new HashSet<>();
    private Set<Player> teamBplayer = new HashSet<>();
    private Set<Player> players = new HashSet<>();
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

    void loadFromConfig() {
        pos1 = Util.stringToLocation(config.getString("pos1"));
        pos2 = Util.stringToLocation(config.getString("pos2"));
        teamA = Util.stringToLocation(config.getString("teamA"));
        teamB = Util.stringToLocation(config.getString("teamB"));
        lobby = Util.stringToLocation(config.getString("lobby"));
        reqPlayerNum = config.getInt("reqPlayerNum", 4);
        teamAclass.clear();
        teamAclass.addAll(CSClass.parseSet(config.getStringList("teamAclass")));
        teamBclass.clear();
        teamBclass.addAll(CSClass.parseSet(config.getStringList("teamBclass")));
        for (String s : config.getStringList("effectPlace")) {
            effects.add(EffectPlace.deserialize(this, s));
        }
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

    public long getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(long scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    // In-game

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
        player.teleport(lobby);
        player.getInventory().setItem(0, GameItems.getClassItem());
        player.getInventory().setItem(8, GameItems.getExitItem());
        players.add(player);
        PlayerInfo.playerRoomMap.put(player, this);
        sendAllPlayersMsg(S.toPrefixAqua(player.getDisplayName()) + S.toAqua(" 加入房间"));
    }

    public void exit(Player player) {
        PlayerInfo.teleportOut(player);
        leave(player);
    }


    public void leave(Player player) {
        playerData.set(player.getName() + ".room", null);
        players.remove(player);
        PlayerInfo.playerRoomMap.remove(player);
        sendAllPlayersMsg(S.toPrefixAqua(player.getDisplayName()) + S.toAqua(" 退出房间"));
        playerData.save();
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
        }
        for (EffectPlace effectPlace : effects) {
            effectPlace.spawn();
        }
    }

    public void playerEquip(Player player) {
        playerClassMap.get(player).equip(player);
        player.getInventory().setItem(8, GameItems.getInfoItem());
    }

    public void playerDeath(Player killer, Player player) {
        if (killer != null)
            playerKillsMap.put(killer, playerKillsMap.get(killer) + 1);

        playerDeathMap.put(player, playerDeathMap.get(player) + 1);
        sendAllPlayersMsg(CScraft.prefix + player.getDisplayName() + S.toYellow(" 死了"));
        player.setHealth(player.getMaxHealth());
        playerClassMap.get(player).equip(player);
        if (teamAplayer.contains(player))
            player.teleport(teamA);
        else
            player.teleport(teamB);
        new InvincibleListener(player);
    }

    public void teamAWin() {
    }

    public void teamBWin() {
    }

    void teleportOut() {
        playerKillsMap.clear();
        playerDeathMap.clear();
        teamAplayer.clear();
        teamBplayer.clear();
        status = RoomStatus.WAITING;
        for (Player player : new HashSet<>(players)) {
            exit(player);
        }
    }

    public String getTeamAName() {
        return S.toBoldDarkAqua("红队");
    }

    public String getTeamBName() {
        return S.toBoldRed("蓝队");
    }

    public static String name() {
        return S.toRed("错误!");
    }

    public String infoString() {
        return "Room{" + "\nname='" + name + '\'' +
                "\npos1=" + Util.locationToString(pos1) +
                "\npos2=" + Util.locationToString(pos2) +
                "\nteamA=" + Util.locationToString(teamA) +
                "\nteamB=" + Util.locationToString(teamB) +
                "\nlobby=" + Util.locationToString(lobby) +
                "\nreqPlayerNum=" + reqPlayerNum +
                "\nteamAclass=" + teamAclass +
                "\nteamBclass=" + teamBclass +
                "\neffects=" + effects +
                "\nstatus=" + status +
                "\nscheduleStart=" + scheduleStart +
                "\nteamAplayer=" + Util.playerToStringSet(teamAplayer) +
                "\nteamBplayer=" + Util.playerToStringSet(teamBplayer) +
                "\nplayers=" + Util.playerToStringSet(players) +
                "\nplayerClassMap=" + Util.playerToStringSet(playerClassMap.keySet()) + "/" + playerClassMap.values() +
                "\nplayerKillsMap=" + Util.playerToStringSet(playerKillsMap.keySet()) + "/" + playerKillsMap.values() +
                "\nplayerDeathMap=" + Util.playerToStringSet(playerDeathMap.keySet()) + "/" + playerDeathMap.values() +
                '}';
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
