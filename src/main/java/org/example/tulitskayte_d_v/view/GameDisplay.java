package org.example.tulitskayte_d_v.view;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.utils.FieldCalculator;
import org.example.tulitskayte_d_v.model.game.utils.helpers.CoordinateHelper;
import org.example.tulitskayte_d_v.model.game.utils.helpers.TextHelper;
import org.example.tulitskayte_d_v.model.player.Player;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.OpponentDeckConditions;


public class GameDisplay {
    public void printBothBattleFields(Player currentPlayer, Player enemyPlayer) {
        System.out.print("\n");
        BattleField currentPlayerBattleField = currentPlayer.getBattleField();
        BattleField enemyPlayerBattleField = enemyPlayer.getBattleField();
        int size = Math.max(currentPlayerBattleField.getSize(), enemyPlayerBattleField.getSize());

        printFieldTitle(currentPlayer.getName(), enemyPlayer.getName(), size);
        printFieldHeaders(size);
        printFieldRows(currentPlayerBattleField, enemyPlayerBattleField, size);
    }

    private void printFieldTitle(String currentPlayerName, String enemyPlayerName, int size) {
        int totalFieldWidth = size * 4;
        int totalTitleLength = currentPlayerName.length() + " Ваше поле: ".length() + enemyPlayerName.length() + " Вражеское поле:".length();
        int spacesCount = totalFieldWidth - totalTitleLength + 30;
        String spaces = new String(new char[spacesCount]).replace("\0", " ");
        System.out.println(TextHelper.ANSI_GREEN_BACKGROUND + currentPlayerName + " Ваше поле: " + TextHelper.ANSI_RESET
                + spaces + TextHelper.ANSI_RED_BACKGROUND + enemyPlayerName + " Вражеское поле:" + TextHelper.ANSI_RESET);
    }

    private void printFieldHeaders(int size) {
        System.out.print("    ");
        printFieldHeader(size);
        System.out.print("       ");
        printFieldHeader(size);
        System.out.println();
    }

    private void printFieldHeader(int size) {
        for (int i = 0; i < size; i++) {
            String columnHeader = CoordinateHelper.numberCoordinateToLetter(i);
            int cellWidth = columnHeader.length() > 2 ? 3 : 4;
            String headerFormat = "%" + cellWidth + "s";
            System.out.printf(headerFormat, columnHeader + "  ");
        }
    }

    private void printFieldRows(BattleField currentPlayerBattleField, BattleField enemyPlayerBattleField, int size) {
        int maxLetterLength = CoordinateHelper.numberCoordinateToLetter(size - 1).length();
        for (int i = 0; i < size; i++) {
            String currentPlayerLine = getFieldLine(currentPlayerBattleField, i, true, maxLetterLength);
            String enemyPlayerLine = getFieldLine(enemyPlayerBattleField, i, false, maxLetterLength);
            System.out.printf("%2d | %s", i + 1, currentPlayerLine);
            System.out.print("  ");
            System.out.printf("%2d | %s", i + 1, enemyPlayerLine);
            System.out.println();
        }
    }

    private String getFieldLine(BattleField battleField, int row, boolean isPlayerField, int maxLetterLength) {
        int size = battleField.getSize();
        StringBuilder line = new StringBuilder();
        for (int col = 0; col < size; col++) {
            line.append(getCellSymbol(battleField, row, col, isPlayerField)).append(" | ");
        }
        return line.toString();
    }

    private String getCellSymbol(BattleField battleField, int row, int col, boolean isPlayerField) {
        Cell cell = battleField.getCells()[row][col];
        if (cell.isThereAShip()) {
            if (isPlayerField || battleField.getShip(row, col).getEnemyState() == OpponentDeckConditions.VISIBLE) {
                if (cell.getState() == CellStates.CHECKED) {
                    return TextHelper.ANSI_RED_BACKGROUND + "X" + TextHelper.ANSI_RESET;
                } else {
                    return TextHelper.ANSI_RED + "X" + TextHelper.ANSI_RESET;
                }
            } else {
                return TextHelper.ANSI_BLUE + "~" + TextHelper.ANSI_RESET;
            }
        } else if (cell.getState() == CellStates.CHECKED) {
            return TextHelper.ANSI_BLACK + "О" + TextHelper.ANSI_RESET;
        } else {
            return TextHelper.ANSI_BLUE + "~" + TextHelper.ANSI_RESET;
        }
    }

    public void arrangeHint(String name, int size) {
        String hint = createShipHint(FieldCalculator.calculateShipCounts(size));
        printShipArrangementInstructions(name, hint, size);
    }

    private void printShipArrangementInstructions(String playerName, String hint, int fieldSize) {
        String lastColumnHeader = CoordinateHelper.numberCoordinateToLetter(fieldSize - 1);
        System.out.printf("""
                        %s, расставьте корабли следующим образом: %s.
                        Каждый корабль должен иметь координаты X (A-%s) и Y (1-%d) в зависимости от размера поля.
                        """,
                playerName, hint, lastColumnHeader, fieldSize);
    }

    private String createShipHint(int[] shipCounts) {
        StringBuilder hint = new StringBuilder();
        for (int i = shipCounts.length - 1; i >= 0; i--) {
            hint.append(shipCounts[i]).append("x").append(i + 1).append("-палубный");
            if (i > 0) {
                hint.append(i > 1 ? ", " : " и ");
            }
        }
        return hint.toString();
    }

    public void processMoveResult(HitResults resultOfMove, Player player) { // TODO: проблема с пробелами и печатью
        switch (resultOfMove) {
            case MISS:
                System.out.printf(TextHelper.ANSI_RED + "\n%s, Вы не попали!\n" + TextHelper.ANSI_RESET, player.getName());
                break;
            case HURT:
            case KILLED:
                System.out.printf(TextHelper.ANSI_GREEN + "\n%s, Вы попали!%s\n" + TextHelper.ANSI_RESET, player.getName(), resultOfMove == HitResults.KILLED ? " И затопили корабль!" : "");
                break;
            default:
                System.out.printf(TextHelper.ANSI_RED + "\n%s, Выстрела не было.\n" + TextHelper.ANSI_RESET, player.getName());
                break;
        }
    }

    public void printWinner(String name) {
        System.out.printf(TextHelper.ANSI_GREEN + "Поздравляю с победой, %s. Игра окончена!\n" + TextHelper.ANSI_RESET, name);
    }
}
