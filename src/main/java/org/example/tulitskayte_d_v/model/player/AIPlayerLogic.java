package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.player.bot.BotMoveSelector;
import org.example.tulitskayte_d_v.model.player.bot.ShipPlacementGenerator;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class AIPlayerLogic implements PlayerLogic{

    @Override
    public void placeShips(ShipPlacementField battleFieldManager, ArrayList<Ship> ships) {
        String shipPlacement = ShipPlacementGenerator.generateBotShipPlacement(battleFieldManager.getSize());
        List<Ship> generatedShips = GameUtils.convertStringToShips(battleFieldManager.getSize(), shipPlacement);
        ships.addAll(generatedShips);
        battleFieldManager.arrangeShips(ships);
    }

    @Override
    public Coordinate makeMove(MoveField enemyBattleFieldManager) {
        return BotMoveSelector.calculateBotNextMove(enemyBattleFieldManager);
    }
}
