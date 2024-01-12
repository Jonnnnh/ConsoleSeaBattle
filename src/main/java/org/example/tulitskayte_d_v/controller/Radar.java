package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public class Radar implements MoveField {
    private final BattleField battleField;
    private final ShotMatrix shotMatrix;

    public Radar(BattleField battleField, ShotMatrix shotMatrix) {
        this.battleField = battleField;
        this.shotMatrix = shotMatrix;
    }

    @Override
    public int getSize() {
        return shotMatrix.getSize();
    }

    @Override
    public boolean isValidMove(Coordinate coordinate) {
        return battleField.isWithinBounds(coordinate.getRow(), coordinate.getColumn())
                && !shotMatrix.isShot(coordinate);
    }

    @Override
    public CellStates getCellState(int row, int column) {
        return shotMatrix.getShotCells()[row][column].getState();
    }

    @Override
    public List<Ship> getShips() {
        return battleField.getShips();
    }

    @Override
    public Cell[][] getShotCells() {
        return shotMatrix.getShotCells();
    }

    @Override
    public Cell[][] getCells() {
        return battleField.getCells();
    }

    public HitResults getHitResultAtCoordinate(Coordinate coordinate) {
        if (isValidMove(coordinate)) {
            shotMatrix.markShot(coordinate);
            return checkForHit(coordinate);
        }
        return HitResults.MISS;
    }

    private HitResults checkForHit(Coordinate coordinate) {
        return battleField.hitBattleField(coordinate, shotMatrix); // проверяем, есть ли попадание по кораблю из BattleField
    }

    @Override
    public boolean isEnemyLose() {
        return battleField.isEnemyLose();
    }

    @Override
    public Radar deepCopy() {
        BattleField copiedBattleField = this.battleField.deepCopy();
        ShotMatrix copiedShotMatrix = this.shotMatrix.deepCopy();
        return new Radar(copiedBattleField, copiedShotMatrix);
    }
}
