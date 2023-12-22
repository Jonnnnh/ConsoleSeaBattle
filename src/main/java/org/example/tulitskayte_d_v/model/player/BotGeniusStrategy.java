package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;


public class BotGeniusStrategy implements GameStrategy {
    public BotGeniusStrategy() {
    }

    @Override
    public boolean isBot() {
        return true;
    }

    @Override
    public void placeShips(BattleField battleField, ArrayList<Ship> ships) {
        String shipPlacement = BotGenius.generateBotShipPlacement(battleField.getSize());
        List<Ship> generatedShips = GameUtils.convertStringToShips(battleField.getSize(), shipPlacement);
        ships.addAll(generatedShips);
        battleField.arrangeTheShips(ships);
    }

    @Override
    public Coordinate makeMove(BattleField enemyBattleField) {
        return BotGenius.botMove(enemyBattleField);
    }

}
