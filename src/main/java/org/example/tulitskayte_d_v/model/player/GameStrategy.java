package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;

public interface GameStrategy {
    void placeShips(BattleField battleField, ArrayList<Ship> ships);
    Coordinate makeMove(BattleField enemyBattleField);
}
