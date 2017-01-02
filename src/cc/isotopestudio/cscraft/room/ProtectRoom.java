package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;

public class ProtectRoom extends Room {

    public ProtectRoom(String name) {
        super(name);
    }

    @Override
    void loadFromConfig() {
        super.loadFromConfig();

    }


    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return S.toBoldPurple("йьнюдёй╫");
    }
}
