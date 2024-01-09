package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public interface MoveField {
    int getSize();

    boolean isValidMove(Coordinate coordinate);

    CellStates getCellState(int row, int column);

    void updateLastShotCoordinate(Coordinate coordinate);

    Coordinate getLastShotCoordinate();

    List<Ship> getShips();

    Cell[][] getShotCells();

    HitResults getHitResultAtCoordinate(Coordinate coordinate);

    boolean isEnemyLose();

    MoveField deepCopy();
}
