package cc.isotopestudio.cscraft.element;
/*
 * Created by Mars Tan on 12/31/2016.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.cscraft.util.S;

public enum RoomStatus {
    WAITING(S.toGreen("�ȴ���")),
    PROGRESS(S.toYellow("��Ϸ��"));

    private final String name;

    RoomStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
