package org.example.tulitskayte_d_v.model.player.bot;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.DeckConditions;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BotMoveSelector {
    public Coordinate calculateBotNextMove(MoveField enemyBattleFieldManager) {
        List<Coordinate> potentialTargets = findTargetsAroundHurtShip(enemyBattleFieldManager);

        if (!potentialTargets.isEmpty()) {
            return selectRandomTargetFromList(potentialTargets, enemyBattleFieldManager);
        }
        return findRandomValidAttackCoordinate(enemyBattleFieldManager);
    }

    protected List<Coordinate> findTargetsAroundHurtShip(MoveField enemyBattleFieldManager) {
        List<Coordinate> potentialTargets = new ArrayList<>();
        for (Ship ship : enemyBattleFieldManager.getShips()) {
            if (ship.getState() == ShipStates.HURT) {
                for (ShipDeck deck : ship.getDecks()) {
                    if (deck.getState() == DeckConditions.HURT) {
                        Coordinate lastHit = new Coordinate(deck.getRow(), deck.getColumn());
                        potentialTargets.addAll(calculateAttackLineForHurtShip(lastHit, ship, enemyBattleFieldManager));
                        break; // Прерываем цикл после нахождения первой поврежденной палубы
                    }
                }
            }
        }
        return potentialTargets;
    }

    protected  List<Coordinate> calculateAttackLineForHurtShip(Coordinate lastHit, Ship hurtShip, MoveField enemyBattleFieldManager) {
        List<Coordinate> lineTargets = new ArrayList<>();
        // получаем список поврежденных палуб корабля
        List<ShipDeck> hurtDecks = hurtShip.getDecks().stream()
                .filter(deck -> deck.getState() == DeckConditions.HURT)
                .collect(Collectors.toList());

        if (hurtDecks.size() > 1) {
            ShipDeck secondLastHitDeck = hurtDecks.get(hurtDecks.size() - 2);
            Coordinate secondLastHit = new Coordinate(secondLastHitDeck.getRow(), secondLastHitDeck.getColumn());
            int dRow = lastHit.getRow() - secondLastHit.getRow();
            int dCol = lastHit.getColumn() - secondLastHit.getColumn();
            // проверяем следующую и противоположную координаты в линии попаданий
            addTargetIfValid(lineTargets, lastHit.getRow() + dRow, lastHit.getColumn() + dCol, enemyBattleFieldManager);
            addTargetIfValid(lineTargets, hurtDecks.getFirst().getRow() - dRow, hurtDecks.getFirst().getColumn() - dCol, enemyBattleFieldManager);
        } else {
            // добавляем соседние координаты, если у корабля только одна поврежденная палуба
            lineTargets.addAll(findAdjacentTargets(lastHit, enemyBattleFieldManager));
        }

        return lineTargets;
    }

    private void addTargetIfValid(List<Coordinate> lineTargets, int row, int col, MoveField enemyBattleFieldManager) {
        if (isCoordinateValidForAttack(row, col, enemyBattleFieldManager)) {
            lineTargets.add(new Coordinate(row, col));
        }
    }

    private  List<Coordinate> findAdjacentTargets(Coordinate coordinate, MoveField enemyBattleFieldManager) {
        List<Coordinate> adjacentCoordinates = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int newRow = coordinate.getRow() + dir[0];
            int newCol = coordinate.getColumn() + dir[1];
            if (isCoordinateValidForAttack(newRow, newCol, enemyBattleFieldManager)) {
                adjacentCoordinates.add(new Coordinate(newRow, newCol));
            }
        }
        return adjacentCoordinates;
    }

    // обновленный метод, который проверяет матриц выстрелов, а не матрицу клеток, и будет атаковать, только в те, в которые ранее не стрелял
    protected boolean isCoordinateValidForAttack(int row, int col, MoveField enemyBattleFieldManager) {
        int size = enemyBattleFieldManager.getSize();
        Cell[][] shotCells = enemyBattleFieldManager.getShotCells();

        return row >= 0 && row < size && col >= 0 && col < size &&
                shotCells[row][col].getState() == CellStates.EMPTY;
    }

    protected  boolean isCoordinateValidForAttack(Coordinate coordinate, MoveField enemyBattleFieldManager) {
        return isCoordinateValidForAttack(coordinate.getRow(), coordinate.getColumn(), enemyBattleFieldManager);
    }

    private Coordinate findRandomValidAttackCoordinate(MoveField enemyBattleFieldManager) {
        Random rnd = new Random();
        int size = enemyBattleFieldManager.getSize();
        Coordinate coordinate;
        do {
            int row = rnd.nextInt(size);
            int col = rnd.nextInt(size);
            coordinate = new Coordinate(row, col);
        } while (!isCoordinateValidForAttack(coordinate, enemyBattleFieldManager));
        return coordinate;
    }

    protected  Coordinate selectRandomTargetFromList(List<Coordinate> targets, MoveField enemyBattleFieldManager) {
        Random rnd = new Random();
        return targets.get(rnd.nextInt(targets.size()));
    }
}
