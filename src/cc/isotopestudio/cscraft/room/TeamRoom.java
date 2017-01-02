package cc.isotopestudio.cscraft.room;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;

public class TeamRoom extends Room {

    public TeamRoom(String name) {
        super(name);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String toString() {
        return S.toBoldGold("�Ŷ�ģʽ");
    }
}
