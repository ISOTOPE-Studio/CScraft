package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.data.CSClass;
import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

import static cc.isotopestudio.cscraft.CScraft.roomData;

public abstract class Room {

    public static Map<String, Room> rooms = new HashMap<>();

    ConfigurationSection config;

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
        config = roomData.getConfigurationSection(name);
        if (config == null) {
            roomData.set(name + ".created", new Date().getTime());
            roomData.save();
            config = roomData.getConfigurationSection(name);
        } else {
            loadFromConfig();
        }
        rooms.put(name, this);
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
        roomData.save();
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        config.set("pos2", Util.locationToString(pos2));
        roomData.save();
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
        config.set("lobby", Util.locationToString(lobby));
        roomData.save();
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
        config.set("min", minPlayer);
        roomData.save();
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
        config.set("max", maxPlayer);
        roomData.save();
    }

    /**
     * DON'T Write unless modifying settings
     */
    public Set<CSClass> getTeamAclass() {
        return teamAclass;
    }

    public void saveTeamAclass() {
        config.set("teamAclass", new ArrayList<>(teamAclass));
        roomData.save();
    }

    /**
     * DON'T Write unless modifying settings
     */
    public Set<CSClass> getTeamBclass() {
        return teamBclass;
    }

    public void saveTeamBclass() {
        config.set("teamBclass", new ArrayList<>(teamBclass));
        roomData.save();
    }

    public abstract boolean isReady();


    // In-game

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
        config.set("status", status.name());
        roomData.save();
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
