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
    private int size;
    private Cell[][] cells;
    private List<Ship> ships;
    private Radar radar;

    public BattleField(int size, List<Ship> ships) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.radar = new Radar();
        createEmptyCells(this.cells);
        arrangeTheShips(ships);
    }

    // используем матрицу ячеек для расстановки кораблей
     void arrangeTheShips(List<Ship> ships) {
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

    boolean isThereAShip(int row, int column) {
        return cells[row][column].isThereAShip();
    }

    HitResults hitBattleField(Coordinate coordinate) {
        shotMatrix.markShot(coordinate);
        radar.updateLastShotCoordinate(coordinate);

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
        return isWithinBounds(coordinate.getRow(), coordinate.getColumn())
                && !shotMatrix.isShot(coordinate);
    }

    private void makeChecked(int row, int column) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (isWithinBounds(i, j)) {
                    shotMatrix.markShot(new Coordinate(i, j));
                }
            }
        }
    }

    boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < getSize() && column >= 0 && column < getSize();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell[][] getShotCells() {
        return shotMatrix.getShotCells();
    }

    public ShotMatrix getShotMatrix() {
        return shotMatrix;
    }

    public Radar getRadar() {
        return radar;
    }

    public List<Ship> getShips() {
        return ships;
    }

    boolean isEnemyLose() {
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
        clonedBattleField.shotMatrix = this.shotMatrix.deepCopy();
        clonedBattleField.radar = this.radar.deepCopy();
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
