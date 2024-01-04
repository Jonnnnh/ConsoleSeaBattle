package org.example.tulitskayte_d_v.model.commands;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public class PlaceShipsCommand implements Command {
    private final BattleField battleField;
    private final List<Ship> ships;

    public PlaceShipsCommand(BattleField battleField, List<Ship> ships) {
        this.battleField = battleField;
        this.ships = ships;
    }

    @Override
    public void execute() {
        battleField.arrangeTheShips(ships);
    }
}
