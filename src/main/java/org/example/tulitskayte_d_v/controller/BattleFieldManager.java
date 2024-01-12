package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;


public class BattleFieldManager implements ShipPlacementField {
    private final BattleField battleField;

    public BattleFieldManager(BattleField battleField) {
        this.battleField = battleField;
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
    public void arrangeShips(List<Ship> ships) { // здесь мы просто запоминаем, но не ставим
        if (ships == null) {
            throw new IllegalArgumentException("List of ships cannot be null");
        }
        battleField.arrangeTheShips(ships);
    }
//    @Override
//    public void fillField(BattleField battleField) { // а здесь уже окончательно ставим
//        battleField.arrangeTheShips();
//    }

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
