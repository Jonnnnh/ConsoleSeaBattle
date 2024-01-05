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

    public static Coordinate calculateBotNextMove(BattleField enemyBattleField) {
        List<Coordinate> potentialTargets = findTargetsAroundHurtShip(enemyBattleField);

        if (!potentialTargets.isEmpty()) {
            return selectRandomTargetFromList(potentialTargets, enemyBattleField);
        }
        return findRandomValidAttackCoordinate(enemyBattleField);
    }

    protected static List<Coordinate> findTargetsAroundHurtShip(BattleField enemyBattleField) {
        List<Coordinate> potentialTargets = new ArrayList<>();
        for (Ship ship : enemyBattleField.getShips()) {
            if (ship.getState() == ShipStates.HURT) {
                for (ShipDeck deck : ship.getDecks()) {
                    if (deck.getState() == DeckConditions.HURT) {
                        Coordinate lastHit = new Coordinate(deck.getRow(), deck.getColumn());
                        potentialTargets.addAll(calculateAttackLineForHurtShip(lastHit, ship, enemyBattleField));
                        break; // Прерываем цикл после нахождения первой поврежденной палубы
                    }
                }
            }
        }
        return potentialTargets;
    }

    protected static List<Coordinate> calculateAttackLineForHurtShip(Coordinate lastHit, Ship hurtShip, BattleField enemyBattleField) {
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
            addTargetIfValid(lineTargets, lastHit.getRow() + dRow, lastHit.getColumn() + dCol, enemyBattleField);
            addTargetIfValid(lineTargets, hurtDecks.getFirst().getRow() - dRow, hurtDecks.getFirst().getColumn() - dCol, enemyBattleField);
        } else {
            // Добавляем соседние координаты, если у корабля только одна поврежденная палуба
            lineTargets.addAll(findAdjacentTargets(lastHit, enemyBattleField));
        }

        return lineTargets;
    }

    private static void addTargetIfValid(List<Coordinate> lineTargets, int row, int col, BattleField enemyBattleField) {
        if (isCoordinateValidForAttack(row, col, enemyBattleField)) {
            lineTargets.add(new Coordinate(row, col));
        }
    }

    private static List<Coordinate> findAdjacentTargets(Coordinate coordinate, BattleField enemyBattleField) {
        List<Coordinate> adjacentCoordinates = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newRow = coordinate.getRow() + dir[0];
            int newCol = coordinate.getColumn() + dir[1];
            if (isCoordinateValidForAttack(newRow, newCol, enemyBattleField)) {
                adjacentCoordinates.add(new Coordinate(newRow, newCol));
            }
        }
        return adjacentCoordinates;
    }

    // обновленный метод, который проверяет матриц выстрелов, а не матрицу клеток, и будет атаковать, только в те, в которые ранее не стрелял
    protected static boolean isCoordinateValidForAttack(int row, int col, BattleField enemyBattleField) {
        int size = enemyBattleField.getSize();
        return row >= 0 && row < size && col >= 0 && col < size &&
                enemyBattleField.getShotCells()[row][col].getState() == CellStates.EMPTY;
    }

    protected static boolean isCoordinateValidForAttack(Coordinate coordinate, BattleField enemyBattleField) {
        return isCoordinateValidForAttack(coordinate.getRow(), coordinate.getColumn(), enemyBattleField);
    }

    private static Coordinate findRandomValidAttackCoordinate(BattleField enemyBattleField) {
        Random rnd = new Random();
        int size = enemyBattleField.getSize();
        Coordinate coordinate;
        do {
            int row = rnd.nextInt(size);
            int col = rnd.nextInt(size);
            coordinate = new Coordinate(row, col);
        } while (!isCoordinateValidForAttack(coordinate, enemyBattleField));
        return coordinate;
    }

    protected static Coordinate selectRandomTargetFromList(List<Coordinate> targets, BattleField enemyBattleField) {
        Random rnd = new Random();
        return targets.get(rnd.nextInt(targets.size()));
    }
}
