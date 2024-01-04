package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.CoordinateParser;
import org.example.tulitskayte_d_v.model.game.utils.FieldCalculator;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.game.utils.helpers.CoordinateHelper;
import org.example.tulitskayte_d_v.model.player.GameStrategy;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.view.GameDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HumanStrategy implements GameStrategy {
    private final Scanner scanner;

    public HumanStrategy() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void placeShips(BattleField battleField, ArrayList<Ship> ships) {
        while (true) {
            try {
                System.out.println("\nEnter the location of the ships (e.g. g1-g2, D1-D4):");
                String shipPlacement = scanner.nextLine();
                List<Ship> parsedShips = GameUtils.convertStringToShips(battleField.getSize(), shipPlacement);
                ships.addAll(parsedShips);
                battleField.arrangeTheShips(ships);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + "\nPlease try again.");
            }
        }
    }

    @Override
    public Coordinate makeMove(BattleField enemyBattleField) {
        while (true) {
            try {
                System.out.println("\nEnter the coordinates for the attack (e.g. g1):");
                String input = scanner.nextLine();
                Coordinate coordinate = CoordinateParser.getCoordinate(input);
                if (!isValidCoordinate(coordinate, enemyBattleField)) {
                    System.out.println("Wrong move. Select coordinates within the field.");
                    continue;
                }
                return coordinate;
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect coordinate format. Please try again.");
            }
        }
    }
    private boolean isValidCoordinate(Coordinate coordinate, BattleField battleField) {
        int row = coordinate.getRow();
        int col = coordinate.getColumn();
        return row >= 0 && row < battleField.getSize() && col >= 0 && col < battleField.getSize();
    }
    public static void arrangeHint(String name, int size) {
        String hint = GameDisplay.createShipHint(FieldCalculator.calculateShipCounts(size));
        printShipArrangementInstructions(name, hint, size);
    }
    private static void printShipArrangementInstructions(String playerName, String hint, int fieldSize) {
        String lastColumnHeader = CoordinateHelper.numberCoordinateToLetter(fieldSize - 1);
        System.out.printf("""
                        %s, arrange your ships as follows: %s.
                        Each ship must have coordinates X (A-%s) и Y (1-%d) depending on the field size.
                        """,
                playerName, hint, lastColumnHeader, fieldSize);
    }
}
