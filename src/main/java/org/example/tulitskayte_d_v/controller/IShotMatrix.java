package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.model.game.Coordinate;

public interface IShotMatrix {
    void markShot(Coordinate coordinate);
    boolean  isShot(Coordinate coordinate);
    Cell[][] getShotCells();
}
