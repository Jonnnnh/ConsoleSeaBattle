package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.Radar;
import org.example.tulitskayte_d_v.controller.ShotMatrix;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.player.strategy.PlayerLogic;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;

public class Player {
    private final PlayerStorage storage;
    private final PlayerLogic logic;
    private Radar radar;

    public Player(PlayerStorage storage, PlayerLogic logic) {
        this.storage = storage;
        this.logic = logic;
    }
    public void placeShips(ArrayList<Ship> ships) {
        // bfManager здесь
        logic.placeShips(storage.getShipPlacementField(), ships);
        bf.fill(storage.getBattleField());
    }
    public Coordinate makeMove(BattleField enemyBattleFieldManager) {
        return logic.makeMove(storage.getMoveField());
    }
    public String getName() {
        return storage.getName();
    }

    public void setName(String name) {
        storage.setName(name);
    }

    public ShotMatrix getBattleField() { // ?
        return storage.getShotMatrix();
    }

    public void setBattleField(MoveField battleField) { // ?
        storage.setMoveField(battleField);
    }
    public HitResults move(Coordinate coordinate) { // здесь пока не понятно, но ход у нас не повторяется
        return getBattleField().getHitResultAtCoordinate(coordinate);
    }
}
