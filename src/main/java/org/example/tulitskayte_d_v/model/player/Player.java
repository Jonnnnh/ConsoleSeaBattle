package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.*;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;

public class Player {
    private final PlayerStorage storage;
    private final PlayerLogic logic;

    public Player(PlayerStorage storage, PlayerLogic logic) {
        this.storage = storage;
        this.logic = logic;
    }

    public void placeShips(ArrayList<Ship> ships) {
        BattleFieldManager bfManager = new BattleFieldManager(storage.getBattleField());
        logic.placeShips(bfManager, ships);
        bfManager.fillField(storage.getBattleField());
    }

    public Coordinate makeMove(Radar radar) {
        return logic.makeMove(radar);
    }

    public String getName() {
        return storage.getName();
    }

    public void setName(String name) {
        storage.setName(name);
    }

    public BattleField getBattleField() {
        return storage.getBattleField();
    }

    public void setBattleField(BattleField battleField) {
        storage.setBattleField(battleField);
    }

    public ShotMatrix getBattleFieldShotMatrix() {
        return storage.getShotMatrix();
    }

    public void setShotMatrix(ShotMatrix shotMatrix) {
        storage.setShotMatrix(shotMatrix);
    }

    public PlayerStorage getStorage() {
        return storage;
    }
}
