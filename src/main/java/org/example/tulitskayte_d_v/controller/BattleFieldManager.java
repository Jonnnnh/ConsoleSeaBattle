package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.List;

public class BattleFieldManager {

    private final BattleField battleField;

    public BattleFieldManager(int size, List<Ship> ships) {
        this.battleField = new BattleField(size, ships);
    }

    public HitResults hitBattleField(Coordinate coordinate) {
        if (!battleField.isValidMove(coordinate)) {
            throw new IllegalStateException("Invalid move");
        }
        return battleField.hitBattleField(coordinate);
    }

    public void arrangeShips(List<Ship> ships) {
        battleField.arrangeTheShips(ships);
    }

    public Cell[][] getCells() {
        return battleField.copyCells();
    }

    public boolean isEnemyLose() {
        return battleField.isEnemyLose();
    }

    public int getSize() {
        return battleField.getSize();
    }

    public List<Ship> getShips() {
        return battleField.getShips();
    }
    public ShipDeck getShip(int row, int column) {
        return battleField.getShip(row, column);
    }
    public boolean isValidMove(Coordinate coordinate) {
        return battleField.isValidMove(coordinate);
    }
}
