package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.*;

public class PlayerStorage {
    private String name;

//    public PlayerStorage(String name, BattleField battleField, ShotMatrix shotMatrix) {
//        this.name = name;
//        this.battleField = battleField;
//        this.shotMatrix = shotMatrix;
//    }

    private BattleField battleField;
    private ShotMatrix shotMatrix;


    public void setShotMatrix(ShotMatrix shotMatrix) {
        this.shotMatrix = shotMatrix;
    }

    public ShotMatrix getShotMatrix() {
        return shotMatrix;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerStorage deepCopy() {
        PlayerStorage copy = new PlayerStorage();
        copy.setName(this.name);
        copy.setBattleField(this.battleField.deepCopy());
        copy.setShotMatrix(this.shotMatrix.deepCopy());
        return copy;
    }
}
