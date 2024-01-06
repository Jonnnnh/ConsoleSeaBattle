package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;


public class BattleFieldManager implements ShipPlacementField, MoveField {
    private final BattleField battleField;
    private final Radar radar;
    public BattleFieldManager(BattleField battleField, Radar radar) {
        this.battleField = battleField;
        this.radar = radar;
    }

    @Override
    public boolean isValidMove(Coordinate coordinate) {
        return battleField.isValidMove(coordinate);
    }

    @Override
    public CellStates getCellState(int row, int column) {
        if (battleField.isWithinBounds(row, column)) {
            return battleField.getShotMatrix().getShotCells()[row][column].getState();
        }
        return null;
    }


    @Override
    public void updateLastShotCoordinate(Coordinate coordinate) {
        radar.updateLastShotCoordinate(coordinate);
    }

    @Override
    public Coordinate getLastShotCoordinate() {
        return radar.getLastShotCoordinate();
    }

    @Override
    public List<Ship> getShips() {
        return battleField.getShips();
    }

    @Override
    public Cell[][] getShotCells() {
        return battleField.getShotMatrix().getShotCells();
    }

    @Override
    public HitResults getHitResultAtCoordinate(Coordinate coordinate) {
        return battleField.hitBattleField(coordinate);
    }

    @Override
    public int getSize() {
        return battleField.getSize();
    }

    @Override
    public boolean isThereAShip(int row, int column) {
        return battleField.isThereAShip(row, column);
    }

    @Override
    public boolean canPlaceShip(int row, int col, int shipSize, boolean horizontal) {
        // проверяем, находятся ли все ячейки корабля в пределах поля
        if (horizontal && (col + shipSize > battleField.getSize()) ||
                !horizontal && (row + shipSize > battleField.getSize())) {
            return false;
        }

        // проверяем, заняты ли ячейки корабля или его окрестности другими кораблями
        for (int i = 0; i < shipSize; i++) {
            int checkRow = row + (horizontal ? 0 : i);
            int checkCol = col + (horizontal ? i : 0);

            if (!isCellAvailableForShip(checkRow, checkCol)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void arrangeShips(List<Ship> ships) {
        battleField.arrangeTheShips(ships);
    }

    private boolean isCellAvailableForShip(int row, int col) {
        if (!battleField.isWithinBounds(row, col)) {
            return false;
        }
        // проверяем ячейку и соседние ячейки
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int checkRow = row + i;
                int checkCol = col + j;
                if (battleField.isWithinBounds(checkRow, checkCol) &&
                        battleField.isThereAShip(checkRow, checkCol)) {
                    return false;
                }
            }
        }
        return true;
    }
}
