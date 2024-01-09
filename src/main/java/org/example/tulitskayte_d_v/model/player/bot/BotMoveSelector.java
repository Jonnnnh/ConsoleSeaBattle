package org.example.tulitskayte_d_v.model.player.bot;

import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BotMoveSelector {
    public static Coordinate calculateBotNextMove(MoveField enemyBattleFieldManager) {
        Coordinate lastShotCoordinate = enemyBattleFieldManager.getLastShotCoordinate();
        HitResults lastHitResult = enemyBattleFieldManager.getHitResultAtCoordinate(lastShotCoordinate);

        if (lastHitResult == HitResults.HURT) {
            return chooseNextTargetAround(lastShotCoordinate, enemyBattleFieldManager);
        } else {
            return findRandomValidAttackCoordinate(enemyBattleFieldManager);
        }
    }
    private static Coordinate chooseNextTargetAround(Coordinate lastShotCoordinate, MoveField enemyBattleFieldManager) {
        List<Coordinate> potentialTargets = findAdjacentTargets(lastShotCoordinate, enemyBattleFieldManager);

        if (!potentialTargets.isEmpty()) {
            return selectRandomTargetFromList(potentialTargets, enemyBattleFieldManager);
        }
        return findRandomValidAttackCoordinate(enemyBattleFieldManager);
    }

    private static List<Coordinate> findAdjacentTargets(Coordinate coordinate, MoveField enemyBattleFieldManager) {
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
    protected static boolean isCoordinateValidForAttack(int row, int col, MoveField enemyBattleFieldManager) {
        int size = enemyBattleFieldManager.getSize();
        return row >= 0 && row < size && col >= 0 && col < size &&
                enemyBattleFieldManager.getShotCells()[row][col].getState() == CellStates.EMPTY;
    }

    protected static boolean isCoordinateValidForAttack(Coordinate coordinate, MoveField enemyBattleFieldManager) {
        return isCoordinateValidForAttack(coordinate.getRow(), coordinate.getColumn(), enemyBattleFieldManager);
    }

    private static Coordinate findRandomValidAttackCoordinate(MoveField enemyBattleFieldManager) {
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

    protected static Coordinate selectRandomTargetFromList(List<Coordinate> targets, MoveField enemyBattleFieldManager) {
        Random rnd = new Random();
        return targets.get(rnd.nextInt(targets.size()));
    }
}
