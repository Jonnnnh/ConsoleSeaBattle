package org.example.tulitskayte_d_v.model.player.bot;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.FieldCalculator;
import org.example.tulitskayte_d_v.model.game.utils.helpers.CoordinateHelper;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipPlacementGenerator {
    public static String generateBotShipPlacement(int size) { // рандом расположение
        List<Ship> ships = new ArrayList<>();
        BattleField field = new BattleField(size, ships);
        int[] shipCounts = FieldCalculator.calculateShipCounts(size);
        Random random = new Random();

        for (int shipType = shipCounts.length; shipType >= 1; shipType--) {
            for (int j = 0; j < shipCounts[shipType - 1]; j++) {
                placeSingleShipRandomly(field, ships, shipType, random);
            }
        }
        return convertShipsPlacementToString(ships);
    }

    private static void placeSingleShipRandomly(BattleField field, List<Ship> ships, int shipType, Random random) { // размещаем
        boolean placed = false;
        while (!placed) {
            int row = random.nextInt(field.getSize());
            int col = random.nextInt(field.getSize());
            boolean horizontal = random.nextBoolean();

            if (isShipPlacementValid(field, row, col, shipType, horizontal)) { // проверка на размещение
                addShipToBattleField(field, ships, row, col, shipType, horizontal);
                placed = true;
            }
        }
    }

    private static String convertShipsPlacementToString(List<Ship> ships) {
        StringBuilder sb = new StringBuilder();
        for (Ship ship : ships) {
            Coordinate start = new Coordinate(ship.getDecks().getFirst().getRow(), ship.getDecks().getFirst().getColumn());
            Coordinate end = new Coordinate(ship.getDecks().getLast().getRow(), ship.getDecks().getLast().getColumn());

            String startColumn = CoordinateHelper.numberCoordinateToLetter(start.getColumn());
            String endColumn = CoordinateHelper.numberCoordinateToLetter(end.getColumn());
            int startRow = start.getRow() + 1;
            int endRow = end.getRow() + 1;

            sb.append(startColumn).append(startRow);
            if (!start.equals(end)) {
                sb.append("-").append(endColumn).append(endRow);
            }
            sb.append(", ");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    private static void addShipToBattleField(BattleField field, List<Ship> ships, int row, int col, int shipSize, boolean horizontal) {
        List<ShipDeck> shipDecks = new ArrayList<>();
        for (int i = 0; i < shipSize; i++) {
            int currentRow = row + (horizontal ? 0 : i);
            int currentCol = col + (horizontal ? i : 0);

            field.getCells()[currentRow][currentCol].setShipHere();
            shipDecks.add(new ShipDeck(currentRow, currentCol));
        }
        Ship newShip = new Ship(shipDecks);
        ships.add(newShip);
    }

    private static boolean isShipPlacementValid(BattleField field, int row, int col, int shipSize, boolean horizontal) {
        int fieldSize = field.getSize();
        for (int i = 0; i < shipSize; i++) {
            int currentRow = row + (horizontal ? 0 : i);
            int currentCol = col + (horizontal ? i : 0);

            if (currentRow < 0 || currentRow >= fieldSize || currentCol < 0 || currentCol >= fieldSize) {
                return false;
            }

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int neighborRow = currentRow + dr;
                    int neighborCol = currentCol + dc;

                    if (neighborRow >= 0 && neighborRow < fieldSize && neighborCol >= 0 && neighborCol < fieldSize) {
                        if (field.getCells()[neighborRow][neighborCol].isThereAShip()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
