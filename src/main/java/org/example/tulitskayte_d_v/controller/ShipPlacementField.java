package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public interface ShipPlacementField {
    int getSize();
    boolean isThereAShip(int row, int column);
    boolean canPlaceShip(int row, int col, int shipSize, boolean horizontal);
    void arrangeShips(List<Ship> ships);
}
