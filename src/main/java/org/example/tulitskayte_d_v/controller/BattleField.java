package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.ArrayList;
import java.util.List;

public class BattleField {
    public int size;
    protected Cell[][] cells;
    protected Cell[][] shotCells;
    protected List<Ship> ships;

    public BattleField(int size, List<Ship> ships) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.shotCells = new Cell[size][size];
        createEmptyCells(this.cells);
        createEmptyCells(this.shotCells);
        arrangeTheShips(ships);
    }

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

    private void createEmptyCells(Cell[][] cells) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j, false);
            }
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

    public boolean isThereAShip(int row, int column) {
        return cells[row][column].isThereAShip();
    }

    public HitResults hitBattleField(Coordinate coordinate) {
        shotCells[coordinate.getRow()][coordinate.getColumn()].setState(CellStates.CHECKED);

        for (Ship ship : ships) {
            for (ShipDeck shipDeck : ship.getDecks()) {
                if (shipDeck.getRow() == coordinate.getRow() && shipDeck.getColumn() == coordinate.getColumn()) {
                    ShipStates shipState = ship.hitTheShip(coordinate);

                    if (shipState == ShipStates.KILLED) {
                        killTheShip(ship);
                        return HitResults.KILLED;
                    }
                    return HitResults.HURT;
                }
            }
        }
        return HitResults.MISS;
    }

    private void killTheShip(Ship ship) {
        for (ShipDeck shipDeck : ship.getDecks()) {
            int row = shipDeck.getRow();
            int column = shipDeck.getColumn();
            makeChecked(row, column);
        }
    }

    public boolean isValidMove(Coordinate coordinate) {
        int row = coordinate.getRow();
        int column = coordinate.getColumn();
        return isWithinBounds(row, column) && shotCells[row][column].getState() != CellStates.CHECKED;
    }

    private void makeChecked(int row, int column) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (isWithinBounds(i, j)) {
                    shotCells[i][j].setState(CellStates.CHECKED);
                }
            }
        }
    }

    private boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < getSize() && column >= 0 && column < getSize();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell[][] getShotCells() {
        return shotCells;
    }
    public List<Ship> getShips() {
        return ships;
    }

    public boolean isEnemyLose() {
        for (Ship s : ships) {
            if (s.getState() != ShipStates.KILLED) {
                return false;
            }
        }
        return true;
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
        for (Ship ship : this.ships) {
            clonedShips.add(ship.deepCopy());
        }

        BattleField clonedBattleField = new BattleField(this.size, clonedShips);
        clonedBattleField.cells = copyCells(this.cells);
        clonedBattleField.shotCells = copyCells(this.shotCells);
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
