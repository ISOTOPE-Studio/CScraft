package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;

public enum RoomStatus {
    WAITING(S.toGreen("等待中")),
    PROGRESS(S.toYellow("游戏中"));
    private final String name;

    RoomStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
