package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    private final int size;
    private Cell[][] cells;
    private List<Ship> ships;

    public BattleField(int size, List<Ship> ships) {
        this.size = size;
        this.cells = new Cell[size][size];
        createEmptyCells();
//        addShips(ships);
        arrangeTheShips(ships);
    }

//    public void addShips(List<Ship> newShips) {
//        ships.addAll(newShips);
//    }

    // используем матрицу ячеек для расстановки кораблей
    public void arrangeTheShips(List<Ship> ships) {
        this.ships = ships;
        for (Ship s : ships) {
            for (int i = 0; i < s.getDecks().size(); i++) {
                int row = s.getDecks().get(i).getRow();
                int column = s.getDecks().get(i).getColumn();
                cells[row][column] = new Cell(row, column, true);
                System.out.print(" ");
            }
        }
    }

    private void createEmptyCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j, false);
            }
        }
    }

    public HitResults hitBattleField(Coordinate coordinate, ShotMatrix shotMatrix) {
        for (Ship ship : ships) {
            for (ShipDeck shipDeck : ship.getDecks()) {
                if (shipDeck.getRow() == coordinate.getRow() && shipDeck.getColumn() == coordinate.getColumn()) {
                    ShipStates shipState = ship.hitTheShip(coordinate);

                    if (shipState == ShipStates.KILLED) {
                        killTheShip(ship, shotMatrix);
                        return HitResults.KILLED;
                    }
                    return HitResults.HURT;
                }
            }
        }
        return HitResults.MISS;
    }

    public boolean isEnemyLose() {
        for (Ship s : ships) {
            if (s.getState() != ShipStates.KILLED) {
                return false;
            }
        }
        return true;
    }

    private void killTheShip(Ship ship, ShotMatrix shotMatrix) {
        for (ShipDeck shipDeck : ship.getDecks()) {
            int row = shipDeck.getRow();
            int column = shipDeck.getColumn();
            shotMatrix.makeChecked(row, column);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (isThereAShip(row, col)) {
                    sb.append("ship ");
                } else {
                    sb.append("null ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    boolean isThereAShip(int row, int column) {
        return cells[row][column].isThereAShip();
    }

    boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < getSize() && column >= 0 && column < getSize();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getSize() {
        return size;
    }

    public ShipDeck getShip(int row, int column) {
        for (Ship ship : ships) {
            for (ShipDeck shipDeck : ship.getDecks()) {
                if (shipDeck.getRow() == row && shipDeck.getColumn() == column) {
                    return shipDeck;
                }
            }
        }
        return null;
    }

    public BattleField deepCopy() {
        List<Ship> clonedShips = new ArrayList<>();
        if (this.ships != null) {
            for (Ship ship : this.ships) {
                if (ship != null) {
                    clonedShips.add(ship.deepCopy());
                } else {
                    clonedShips.add(null);
                }
            }
        }
        BattleField clonedBattleField = new BattleField(this.size, clonedShips);
        clonedBattleField.cells = copyCells(this.cells);
        return clonedBattleField;
    }

    private Cell[][] copyCells(Cell[][] sourceCells) {
        Cell[][] copiedCells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copiedCells[i][j] = sourceCells[i][j].deepCopy();
            }
        }
        return copiedCells;
    }
}
