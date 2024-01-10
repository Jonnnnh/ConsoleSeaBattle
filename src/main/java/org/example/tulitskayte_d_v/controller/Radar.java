package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public class Radar implements MoveField {
    //TODO: в радаре мы не храним последние выстрел, было как-то тоже передавать
    // TODO: радар должен принимать ссылку на BattleField расстановки кораблей и ShotMatrix, это как раз создает эффект радара
    private final BattleField battleField;
    private Coordinate lastShotCoordinate;

    public Radar(BattleField battleField) {
        this.battleField = battleField;
    }

    @Override
    public int getSize() {
        return battleField.getSize();
    }

    @Override
    public boolean isValidMove(Coordinate coordinate) {
        return false;
    }

    @Override
    public CellStates getCellState(int row, int column) {
        if (battleField.isWithinBounds(row, column)) {
            return battleField.getShotMatrix().getShotCells()[row][column].getState();
        }
        return null;
    }

    public void updateLastShotCoordinate(Coordinate coordinate) {
        this.lastShotCoordinate = coordinate;
    }

    public Coordinate getLastShotCoordinate() {
        return lastShotCoordinate;
    }

    @Override
    public List<Ship> getShips() {
        return null;
    }

    @Override
    public Cell[][] getShotCells() {
        return battleField.getShotCells();
    }

    @Override
    public HitResults getHitResultAtCoordinate(Coordinate coordinate) {
        return battleField.hitBattleField(coordinate);
    }

    @Override
    public boolean isEnemyLose() {
        return battleField.isEnemyLose();
    }

    public Radar deepCopy() {
        Radar clonedRadar = new Radar();
        if (this.lastShotCoordinate != null) {
            clonedRadar.updateLastShotCoordinate(new Coordinate(
                    this.lastShotCoordinate.getRow(),
                    this.lastShotCoordinate.getColumn()
            ));
        }
        return clonedRadar;
    }
}
