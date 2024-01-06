package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;

public class Player {
    private final PlayerStorage storage;
    private final PlayerLogic logic;

    public Player(PlayerStorage storage, PlayerLogic logic) {
        this.storage = storage;
        this.logic = logic;
    }
    public void placeShips(BattleField battleField, ArrayList<Ship> ships) {
        logic.placeShips(battleField, ships);
    }
    public Coordinate makeMove(BattleField enemyBattleField) {
        return logic.makeMove(enemyBattleField);
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
    public HitResults move(Coordinate coordinate) {
        return getBattleField().hitBattleField(coordinate);
    }
}
