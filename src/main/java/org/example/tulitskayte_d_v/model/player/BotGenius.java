package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.FieldCalculator;
import org.example.tulitskayte_d_v.model.game.utils.helpers.CoordinateHelper;
import org.example.tulitskayte_d_v.model.ships.DeckConditions;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class BotGenius {

    public static String generateBotShipPlacement(int size) {
        List<Ship> ships = new ArrayList<>();
        BattleField field = new BattleField(size, ships);
        int[] shipCounts = FieldCalculator.calculateShipCounts(size);
        Random random = new Random();

        for (int shipType = shipCounts.length; shipType >= 1; shipType--) {
            for (int j = 0; j < shipCounts[shipType - 1]; j++) {
                placeShipRandomly(field, ships, shipType, random);
            }
        }
        return convertShipsToString(ships);
    }

    private static void placeShipRandomly(BattleField field, List<Ship> ships, int shipType, Random random) {
        boolean placed = false;
        while (!placed) {
            int row = random.nextInt(field.getSize());
            int col = random.nextInt(field.getSize());
            boolean horizontal = random.nextBoolean();

            if (canPlaceShip(field, row, col, shipType, horizontal)) {
                placeShip(field, ships, row, col, shipType, horizontal);
                placed = true;
            }
        }
    }

    private static String convertShipsToString(List<Ship> ships) {
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

    private static void placeShip(BattleField field, List<Ship> ships, int row, int col, int shipSize, boolean horizontal) {
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

    private static boolean canPlaceShip(BattleField field, int row, int col, int shipSize, boolean horizontal) {
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

    public static Coordinate botMove(BattleField enemyBattleField) {
        List<Coordinate> potentialTargets = getPotentialHurtTargets(enemyBattleField);

        if (!potentialTargets.isEmpty()) {
            return chooseTargetFromList(potentialTargets, enemyBattleField);
        }
        return getRandomValidCoordinate(enemyBattleField);
    }

    private static List<Coordinate> getPotentialHurtTargets(BattleField enemyBattleField) {
        List<Coordinate> potentialTargets = new ArrayList<>();
        for (Ship ship : enemyBattleField.getShips()) {
            if (ship.getState() == ShipStates.HURT) {
                for (ShipDeck deck : ship.getDecks()) {
                    if (deck.getState() == DeckConditions.HURT) {
                        Coordinate lastHit = new Coordinate(deck.getRow(), deck.getColumn());
                        potentialTargets.addAll(getTargetLine(lastHit, ship, enemyBattleField));
                        break; // Прерываем цикл после нахождения первой поврежденной палубы
                    }
                }
            }
        }
        return potentialTargets;
    }

    private static List<Coordinate> getTargetLine(Coordinate lastHit, Ship hurtShip, BattleField enemyBattleField) {
        List<Coordinate> lineTargets = new ArrayList<>();

        // Получаем список поврежденных палуб корабля
        List<ShipDeck> hurtDecks = hurtShip.getDecks().stream()
                .filter(deck -> deck.getState() == DeckConditions.HURT)
                .collect(Collectors.toList());

        if (hurtDecks.size() > 1) {
            ShipDeck secondLastHitDeck = hurtDecks.get(hurtDecks.size() - 2);
            Coordinate secondLastHit = new Coordinate(secondLastHitDeck.getRow(), secondLastHitDeck.getColumn());
            int dRow = lastHit.getRow() - secondLastHit.getRow();
            int dCol = lastHit.getColumn() - secondLastHit.getColumn();

            // Проверяем следующую и противоположную координаты в линии попаданий
            addLineTargetIfExists(lineTargets, lastHit.getRow() + dRow, lastHit.getColumn() + dCol, enemyBattleField);
            addLineTargetIfExists(lineTargets, hurtDecks.getFirst().getRow() - dRow, hurtDecks.getFirst().getColumn() - dCol, enemyBattleField);
        } else {
            // Добавляем соседние координаты, если у корабля только одна поврежденная палуба
            lineTargets.addAll(getAdjacentCoordinates(lastHit, enemyBattleField));
        }

        return lineTargets;
    }
    private static void addLineTargetIfExists(List<Coordinate> lineTargets, int row, int col, BattleField enemyBattleField) {
        if (isValidCoordinate(row, col, enemyBattleField)) {
            lineTargets.add(new Coordinate(row, col));
        }
    }
    private static List<Coordinate> getAdjacentCoordinates(Coordinate coordinate, BattleField enemyBattleField) {
        List<Coordinate> adjacentCoordinates = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newRow = coordinate.getRow() + dir[0];
            int newCol = coordinate.getColumn() + dir[1];
            if (isValidCoordinate(newRow, newCol, enemyBattleField)) {
                adjacentCoordinates.add(new Coordinate(newRow, newCol));
            }
        }
        return adjacentCoordinates;
    }

    private static boolean isValidCoordinate(int row, int col, BattleField enemyBattleField) {
        int size = enemyBattleField.getSize();
        return row >= 0 && row < size && col >= 0 && col < size &&
                enemyBattleField.getCells()[row][col].getState() == CellStates.NULL;
    }

    private static boolean isValidCoordinate(Coordinate coordinate, BattleField enemyBattleField) {
        return isValidCoordinate(coordinate.getRow(), coordinate.getColumn(), enemyBattleField);
    }


    private static Coordinate getRandomValidCoordinate(BattleField enemyBattleField) {
        Random rnd = new Random();
        int size = enemyBattleField.getSize();
        Coordinate coordinate;
        do {
            int row = rnd.nextInt(size);
            int col = rnd.nextInt(size);
            coordinate = new Coordinate(row, col);
        } while (!isValidCoordinate(coordinate, enemyBattleField));
        return coordinate;
    }

    private static Coordinate chooseTargetFromList(List<Coordinate> targets, BattleField enemyBattleField) {
        Random rnd = new Random();
        return targets.get(rnd.nextInt(targets.size()));
    }

}
