package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Set;

import static cc.isotopestudio.cscraft.CScraft.roomData;

public abstract class Room {
    private ConfigurationSection config;

    // Settings
    private final String name;
    // pos must smaller than pos2
    private Location pos1;
    private Location pos2;
    private Location lobby;

    // In-game
    private Set<Player> players;
    private RoomStatus status;

    public Room(String name) {
        this.name = name;
        config = roomData.getConfigurationSection(name);
        if (config == null) {
            roomData.set(name + ".created", new Date().getTime());
            roomData.save();
            config = roomData.getConfigurationSection(name);
        } else {
            pos1 = Util.stringToLocation(config.getString("pos1"));
            pos2 = Util.stringToLocation(config.getString("pos2"));
            lobby = Util.stringToLocation(config.getString("lobby"));
        }
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        config.set("pos1", Util.locationToString(pos1));
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        config.set("pos2", Util.locationToString(pos2));
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
        config.set("lobby", Util.locationToString(lobby));
    }

    public boolean isReady() {
        return pos1 != null && pos2 != null && lobby != null;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Set<Player> getPlayersNames() {
        return players;
    }
}
