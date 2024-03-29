package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;

public interface PlayerLogic {
    void placeShips(ShipPlacementField battleField, ArrayList<Ship> ships);
    Coordinate makeMove(MoveField enemyBattleFieldManager);
}
