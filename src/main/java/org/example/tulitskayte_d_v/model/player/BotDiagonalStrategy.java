package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class BotDiagonalStrategy implements GameStrategy {
    private int currentRow = 0;
    private int currentCol = -1;

    @Override
    public void placeShips(BattleField battleField, ArrayList<Ship> ships) {
        String shipPlacement = BotGenius.generateBotShipPlacement(battleField.getSize());
        List<Ship> generatedShips = GameUtils.convertStringToShips(battleField.getSize(), shipPlacement);
        ships.addAll(generatedShips);
        battleField.arrangeTheShips(ships);
    }

    @Override
    public Coordinate makeMove(BattleField enemyBattleField) {
        List<Coordinate> potentialTargets = BotGenius.findTargetsAroundHurtShip(enemyBattleField);
        if (!potentialTargets.isEmpty()) {
            return BotGenius.selectRandomTargetFromList(potentialTargets, enemyBattleField);
        }
        return nextDiagonalMove(enemyBattleField);
    }
    private Coordinate nextDiagonalMove(BattleField enemyBattleField) {
        do {
            currentCol++;
            currentRow++;

            if (currentRow >= enemyBattleField.getSize()) {
                currentRow = 0;
                currentCol -= enemyBattleField.getSize();
            }

            if (currentCol >= enemyBattleField.getSize()) {
                currentCol = 0; // меняем старт точку
                currentRow = 1;
            }
        } while (!BotGenius.isCoordinateValidForAttack(currentRow, currentCol, enemyBattleField));

        return new Coordinate(currentRow, currentCol);
    }

}
