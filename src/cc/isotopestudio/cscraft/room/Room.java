package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.element.CSClass;
import cc.isotopestudio.cscraft.element.EffectPlace;
import cc.isotopestudio.cscraft.element.GameItems;
import cc.isotopestudio.cscraft.listener.PlayerInfo;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.S;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

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
    private int minPlayer;
    private int maxPlayer;
    Set<CSClass> teamAclass = new HashSet<>();
    Set<CSClass> teamBclass = new HashSet<>();
    Set<EffectPlace> effects = new HashSet<>();

    // In-game
    private RoomStatus status = RoomStatus.WAITING;
    private long scheduleStart = -1;
    Set<Player> teamAplayer = new HashSet<>();
    Set<Player> teamBplayer = new HashSet<>();
    Set<Player> players = new HashSet<>();
    Map<Player, CSClass> playerClassMap = new HashMap<>();


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
        minPlayer = config.getInt("min", 2);
        maxPlayer = config.getInt("max", 4);
        teamAclass.clear();
        teamAclass.addAll(CSClass.parseSet(config.getStringList("teamAclass")));
        teamBclass.clear();
        teamBclass.addAll(CSClass.parseSet(config.getStringList("teamBclass")));
        for (String s : config.getStringList("effectPlace")) {
            effects.add(EffectPlace.deserialize(s));
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
        this.pos2 = pos2;
        config.set("pos2", Util.locationToString(pos2));
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

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
        config.set("min", minPlayer);
        config.save();
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
        config.set("max", maxPlayer);
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
        effects.add(new EffectPlace(location, material, effect, cd));
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

    public void sendAllPlayersMsg(String msg) {
        for (Player player : players)
            player.sendMessage(msg);
    }

    public void start() {
        status = RoomStatus.PROGRESS;
    }

    public String infoString() {
        final StringBuffer sb = new StringBuffer("Room{");
        sb.append("\nname='").append(name).append('\'');
        sb.append("\npos1=").append(Util.locationToString(pos1));
        sb.append("\npos2=").append(Util.locationToString(pos2));
        sb.append("\nteamA=").append(Util.locationToString(teamA));
        sb.append("\nteamB=").append(Util.locationToString(teamB));
        sb.append("\nlobby=").append(Util.locationToString(lobby));
        sb.append("\nminPlayer=").append(minPlayer);
        sb.append("\nmaxPlayer=").append(maxPlayer);
        sb.append("\nteamAclass=").append(teamAclass);
        sb.append("\nteamBclass=").append(teamBclass);
        sb.append("\neffects=").append(effects);
        sb.append("\nstatus=").append(status);
        sb.append("\nscheduleStart=").append(scheduleStart);
        sb.append("\nteamAplayer=").append(Util.playerToStringSet(teamAplayer));
        sb.append("\nteamBplayer=").append(Util.playerToStringSet(teamBplayer));
        sb.append("\nplayers=").append(Util.playerToStringSet(players));
        sb.append('}');
        return sb.toString();
    }


}
