package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.CoordinateParser;
import org.example.tulitskayte_d_v.model.game.utils.GameUtils;
import org.example.tulitskayte_d_v.model.player.GameStrategy;
import org.example.tulitskayte_d_v.model.ships.Ship;

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
                System.out.println("Введите расположение кораблей (например, A1-A2, B1-B4):");
                String shipPlacement = scanner.nextLine();
                List<Ship> parsedShips = GameUtils.convertStringToShips(battleField.getSize(), shipPlacement);
                ships.addAll(parsedShips);
                battleField.arrangeTheShips(ships);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + " Пожалуйста, попробуйте еще раз.");
            }
        }
    }

    @Override
    public Coordinate makeMove(BattleField enemyBattleField) {
        while (true) {
            try {
                System.out.println("Введите координаты для атаки (например, A1):");
                String input = scanner.nextLine();
                Coordinate coordinate = CoordinateParser.getCoordinate(input);

                // Проверка, что координаты в пределах поля
                if (!isValidCoordinate(coordinate, enemyBattleField)) {
                    System.out.println("Неверный ход. Выберите координаты в пределах поля.");
                    continue;
                }
                return coordinate;
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный формат координат. Пожалуйста, попробуйте еще раз.");
            }
        }
    }
    private boolean isValidCoordinate(Coordinate coordinate, BattleField battleField) {
        int row = coordinate.getRow();
        int col = coordinate.getColumn();
        return row >= 0 && row < battleField.getSize() && col >= 0 && col < battleField.getSize();
    }
}
