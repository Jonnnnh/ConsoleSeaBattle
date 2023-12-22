package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;

public class Player {
    private final String name;
    private BattleField battleField;
    private final GameStrategy gameStrategy;

    public Player(String name, GameStrategy gameStrategy) {
        this.name = name;
        this.gameStrategy = gameStrategy;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public String getName() {
        return name;
    }

    public GameStrategy getStrategy() {
        return gameStrategy;
    }

    public HitResults move(Coordinate coordinate) {
        return this.battleField.hitBattleField(coordinate);
    }
}
