package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.player.Player;
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
        createEmptyCells(this.cells);
        this.ships = new ArrayList<>();
        addShips(ships);
        arrangeTheShips();
    }
    public void addShips(List<Ship> newShips) {
        ships.addAll(newShips);
    }

    public void arrangeTheShips() {
        if (this.ships == null) {
            this.ships = new ArrayList<>();
        }

        for (Ship ship : this.ships) {
            for (ShipDeck deck : ship.getDecks()) {
                int row = deck.getRow();
                int column = deck.getColumn();

                if (row >= 0 && row < this.size && column >= 0 && column < this.size) {
                    cells[row][column] = new Cell(row, column, true);
                }
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

    boolean isThereAShip(int row, int column) {
        return cells[row][column].isThereAShip();
    }

    public HitResults hitBattleField(Coordinate coordinate, ShotMatrix shotMatrix, BattleField enemyBattleField) {
        Cell targetCell = enemyBattleField.getCells()[coordinate.getRow()][coordinate.getColumn()];
        targetCell.setState(CellStates.CHECKED);

        for (Ship ship : enemyBattleField.getShips()) {
            for (ShipDeck deck : ship.getDecks()) {
                if (deck.getRow() == coordinate.getRow() && deck.getColumn() == coordinate.getColumn()) {
                    ship.hitTheShip(coordinate);
                    if (ship.getState() == ShipStates.KILLED) {
                        killTheShip(ship, shotMatrix);
                        return HitResults.KILLED;
                    }
                    return HitResults.HURT;
                }
            }
        }

        return targetCell.isThereAShip() ? HitResults.HURT : HitResults.MISS;
    }

    private void killTheShip(Ship ship, ShotMatrix shotMatrix) {
        for (ShipDeck shipDeck : ship.getDecks()) {
            int row = shipDeck.getRow();
            int column = shipDeck.getColumn();
            shotMatrix.makeChecked(row, column);
        }
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
