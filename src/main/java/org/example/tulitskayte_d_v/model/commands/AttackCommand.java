package org.example.tulitskayte_d_v.model.commands;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;

public class AttackCommand implements Command {
    private final BattleField battleField;
    private final Coordinate coordinate;

    public AttackCommand(BattleField battleField, Coordinate coordinate) {
        this.battleField = battleField;
        this.coordinate = coordinate;
    }

    @Override
    public void execute() {
        battleField.hitBattleField(coordinate);
    }
}
