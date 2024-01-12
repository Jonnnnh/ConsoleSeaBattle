package org.example.tulitskayte_d_v.model.player.strategy;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.CoordinateParser;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HumanPlayerLogic implements PlayerLogic{
    private final Scanner scanner;


    public HumanPlayerLogic() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void placeShips(ShipPlacementField battleFieldManager, ArrayList<Ship> ships) {
        while (true) {
            try {
                System.out.println("\nEnter the location of the ships (e.g. g1-g2, D1-D4):");
                String shipPlacement = scanner.nextLine();
                List<Ship> parsedShips = GameUtils.convertStringToShips(battleFieldManager.getSize(), shipPlacement);
                ships.addAll(parsedShips);
                battleFieldManager.arrangeShips(ships);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + "\nPlease try again.");
            }
        }
    }

    @Override
    public Coordinate makeMove(MoveField enemyBattleFieldManager) {
        while (true) {
            try {
                System.out.println("\nEnter the coordinates for the attack (e.g. g1):");
                String input = scanner.nextLine();
                Coordinate coordinate = CoordinateParser.getCoordinate(input);
                if (!isValidCoordinate(coordinate, enemyBattleFieldManager)) {
                    System.out.println("Wrong move. Select coordinates within the field.");
                    continue;
                }
                return coordinate;
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect coordinate format. Please try again.");
            }
        }
    }
    private boolean isValidCoordinate(Coordinate coordinate, MoveField enemyBattleFieldManager) {
        int row = coordinate.getRow();
        int col = coordinate.getColumn();
        return row >= 0 && row < enemyBattleFieldManager.getSize() && col >= 0 && col < enemyBattleFieldManager.getSize();
    }
}
