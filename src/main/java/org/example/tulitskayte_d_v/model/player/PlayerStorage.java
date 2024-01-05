package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;

public class PlayerStorage {
    private String name;
    private BattleField battleField;

    public void setName(String name) {
        this.name = name;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public String getName() {
        return name;
    }

    public BattleField getBattleField() {
        return battleField;
    }
}
