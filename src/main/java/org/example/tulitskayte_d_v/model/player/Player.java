package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;
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
        logic.placeShips(storage.getShipPlacementField(), ships);
    }
    public Coordinate makeMove() {
        return logic.makeMove(storage.getMoveField());
    }
    public String getName() {
        return storage.getName();
    }

    public void setName(String name) {
        storage.setName(name);
    }

    public MoveField getBattleField() { // ?
        return storage.getMoveField();
    }

    public void setBattleField(MoveField battleField) { // ?
        storage.setMoveField(battleField);
    }
    public HitResults move(Coordinate coordinate) {
        return getBattleField().getHitResultAtCoordinate(coordinate);
    }
}
