package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.data.CSClass;
import cc.isotopestudio.cscraft.util.PluginFile;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.*;

public abstract class Room {

    public static Map<String, Room> rooms = new HashMap<>();

    private final PluginFile config;
    private final PluginFile msgData;

    // Settings
    private final String name;
    // pos must smaller than pos2
    private Location pos1;
    private Location pos2;
    private Location lobby;
    private int minPlayer;
    private int maxPlayer;
    private Set<CSClass> teamAclass;
    private Set<CSClass> teamBclass;

    // In-game
    private RoomStatus status;
    private Set<Player> teamAplayer;
    private Set<Player> teamBplayer;


    public Room(String name) {
        this.name = name;
        config = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/config.yml");
        roomFiles.add(config);
        msgData = new PluginFile(plugin, "rooms/" + ChatColor.stripColor(getClass().getSimpleName()) + "." + name + "/msg.yml", "msg.yml");
        msgFiles.add(msgData);
        msgData.setEditable(false);
        rooms.put(name, this);
        loadFromConfig();
    }

    void loadFromConfig() {
        pos1 = Util.stringToLocation(config.getString("pos1"));
        pos2 = Util.stringToLocation(config.getString("pos2"));
        lobby = Util.stringToLocation(config.getString("lobby"));
        minPlayer = config.getInt("min");
        maxPlayer = config.getInt("max");
    }

    // Settings

    public String getName() {
        return name;
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
        config.set("teamAclass", new ArrayList<>(teamAclass));
        config.save();
    }

    /**
     * DON'T Write unless modifying settings
     */
    public Set<CSClass> getTeamBclass() {
        return teamBclass;
    }

    public void saveTeamBclass() {
        config.set("teamBclass", new ArrayList<>(teamBclass));
        config.save();
    }

    public void remove() {
        config.getFile().getParentFile().delete();
        roomFiles.remove(config);
        msgFiles.remove(msgData);
        rooms.remove(name);
    }

    public abstract boolean isReady();


    // In-game

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
        config.set("status", status.name());
        config.save();
    }

    /**
     * @return type name with chatcolor
     */
    @Override
    public abstract String toString();

    public Set<Player> getPlayers() {
        Set<Player> player = new HashSet<>(teamAplayer);
        player.addAll(teamBplayer);
        return player;
    }

}
