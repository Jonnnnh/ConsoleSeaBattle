package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.player.bot.BotMoveSelector;
import org.example.tulitskayte_d_v.model.player.bot.ShipPlacementGenerator;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class AIPlayerLogic implements PlayerLogic{

    @Override
    public void placeShips(BattleField battleField, ArrayList<Ship> ships) {
        String shipPlacement = ShipPlacementGenerator.generateBotShipPlacement(battleField.getSize());
        List<Ship> generatedShips = GameUtils.convertStringToShips(battleField.getSize(), shipPlacement);
        ships.addAll(generatedShips);
        battleField.arrangeTheShips(ships);
    }

    @Override
    public Coordinate makeMove(BattleField enemyBattleField) {
        return BotMoveSelector.calculateBotNextMove(enemyBattleField);
    }
}
