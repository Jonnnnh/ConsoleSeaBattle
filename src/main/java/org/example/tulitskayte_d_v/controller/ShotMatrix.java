package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;

public class ShotMatrix implements IShotMatrix {
    private final Cell[][] shotCells;

    public ShotMatrix(int size) {
        this.shotCells = new Cell[size][size];
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < shotCells.length; i++) {
            for (int j = 0; j < shotCells[i].length; j++) {
                shotCells[i][j] = new Cell(i, j, false);
            }
        }
    }
    public void markShot(Coordinate coordinate) {
        shotCells[coordinate.getRow()][coordinate.getColumn()].setState(CellStates.CHECKED);
    }

    public boolean isShot(Coordinate coordinate) {
        return shotCells[coordinate.getRow()][coordinate.getColumn()].getState() == CellStates.CHECKED;
    }

    public Cell[][] getShotCells() {
        return shotCells;
    }
    public ShotMatrix deepCopy() {
        int size = shotCells.length;
        ShotMatrix clonedMatrix = new ShotMatrix(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell originalCell = shotCells[i][j];
                clonedMatrix.shotCells[i][j] = new Cell(i, j, originalCell.isThereAShip());
                clonedMatrix.shotCells[i][j].setState(originalCell.getState());
            }
        }
        return clonedMatrix;
    }
}
