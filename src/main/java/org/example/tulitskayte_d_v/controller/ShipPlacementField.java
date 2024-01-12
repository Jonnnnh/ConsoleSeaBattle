package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public interface ShipPlacementField {
    int getSize();
    boolean isThereAShip(int row, int column);
    void arrangeShips(List<Ship> ships);
//    void fillField(BattleField battleField);

}
