package org.example.tulitskayte_d_v.view;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.cell.CellStates;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.ShotMatrix;
import org.example.tulitskayte_d_v.model.game.utils.FieldCalculator;
import org.example.tulitskayte_d_v.model.game.utils.helpers.CoordinateHelper;
import org.example.tulitskayte_d_v.model.game.utils.helpers.TextHelper;
import org.example.tulitskayte_d_v.model.player.Player;
import org.example.tulitskayte_d_v.model.ships.HitResults;


public class GameDisplay {
    public void printBothBattleFields(Player currentPlayer, Player enemyPlayer) {
        System.out.print("\n");
        BattleField currentPlayerBattleField = currentPlayer.getBattleField();
        BattleField enemyPlayerBattleField = enemyPlayer.getBattleField();
        ShotMatrix currentPlayerShotMatrix = currentPlayer.getBattleFieldShotMatrix();
        ShotMatrix enemyPlayerShotMatrix = enemyPlayer.getBattleFieldShotMatrix();
        int size = Math.max(currentPlayerBattleField.getSize(), enemyPlayerBattleField.getSize());

        printFieldTitle(currentPlayer.getName(), enemyPlayer.getName(), size);
        printFieldHeaders(size);
        printFieldRows(currentPlayerBattleField, currentPlayerShotMatrix, enemyPlayerBattleField, enemyPlayerShotMatrix, size);
    }

    private void printFieldTitle(String currentPlayerName, String enemyPlayerName, int size) {
        int totalFieldWidth = size * 4;
        int totalTitleLength = currentPlayerName.length() + " Your field: ".length() + enemyPlayerName.length() + " Enemy field:".length();
        int spacesCount = totalFieldWidth - totalTitleLength + 30;
        String spaces = new String(new char[spacesCount]).replace("\0", " ");
        System.out.println(TextHelper.ANSI_GREEN_BACKGROUND + currentPlayerName + " Your field: " + TextHelper.ANSI_RESET
                + spaces + TextHelper.ANSI_RED_BACKGROUND + enemyPlayerName + " Enemy field:" + TextHelper.ANSI_RESET);
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

    private void printFieldRows(BattleField currentPlayerBattleField, ShotMatrix currentPlayerShotMatrix, BattleField enemyPlayerBattleField, ShotMatrix enemyPlayerShotMatrix, int size) {
        int maxLetterLength = CoordinateHelper.numberCoordinateToLetter(size - 1).length();
        for (int i = 0; i < size; i++) {
            String currentPlayerLine = getFieldLine(currentPlayerBattleField, currentPlayerShotMatrix, i, true, maxLetterLength);
            String enemyPlayerLine = getFieldLine(enemyPlayerBattleField, enemyPlayerShotMatrix, i, false, maxLetterLength);
            System.out.printf("%2d | %s", i + 1, currentPlayerLine);
            System.out.print("  ");
            System.out.printf("%2d | %s", i + 1, enemyPlayerLine);
            System.out.println();
        }
    }

    private String getFieldLine(BattleField battleField, ShotMatrix shotMatrix, int row, boolean isPlayerField, int maxLetterLength) {
        int size = battleField.getSize();
        StringBuilder line = new StringBuilder();
        for (int col = 0; col < size; col++) {
            line.append(getCellSymbol(battleField, shotMatrix, row, col, isPlayerField)).append(" | ");
        }
        return line.toString();
    }

    private String getCellSymbol(BattleField battleField, ShotMatrix shotMatrix, int row, int col, boolean isPlayerField) {
        Cell shipCell = battleField.getCells()[row][col];
        Cell shotCell = shotMatrix.getShotCells()[row][col];

        if (isPlayerField) {
            if (shipCell.isThereAShip()) {
                if (shotCell.getState() == CellStates.CHECKED) {
                    return TextHelper.ANSI_RED_BACKGROUND + "X" + TextHelper.ANSI_RESET;
                } else {
                    return TextHelper.ANSI_GREEN + "X" + TextHelper.ANSI_RESET;
                }
            } else {
                if (shotCell.getState() == CellStates.CHECKED) {
                    return TextHelper.ANSI_BLACK + "O" + TextHelper.ANSI_RESET;
                }
            }
        } else {
            if (shotCell.getState() == CellStates.CHECKED) {
                if (shipCell.isThereAShip()) {
                    return TextHelper.ANSI_RED_BACKGROUND + "X" + TextHelper.ANSI_RESET;
                } else {
                    return TextHelper.ANSI_BLACK + "O" + TextHelper.ANSI_RESET;
                }
            }
        }
        return TextHelper.ANSI_BLUE + "~" + TextHelper.ANSI_RESET;
    }

    public static String createShipHint(int[] shipCounts) {
        StringBuilder hint = new StringBuilder();
        for (int i = shipCounts.length - 1; i >= 0; i--) {
            hint.append(shipCounts[i]).append("x").append(i + 1).append("-deck");
            if (i > 0) {
                hint.append(i > 1 ? ", " : " и ");
            }
        }
        return hint.toString();
    }

    public void processMoveResult(HitResults resultOfMove, Player player) {
        switch (resultOfMove) {
            case MISS:
                System.out.printf(TextHelper.ANSI_RED + "\n%s, You didn't get!\n" + TextHelper.ANSI_RESET, player.getName());
                break;
            case HURT:
            case KILLED:
                System.out.printf(TextHelper.ANSI_GREEN + "\n%s, You're in!%s\n" + TextHelper.ANSI_RESET, player.getName(), resultOfMove == HitResults.KILLED ? " And they sank the ship!" : "");
                break;
            default:
                System.out.printf(TextHelper.ANSI_RED + "\n%s, There was no gunshot.\n" + TextHelper.ANSI_RESET, player.getName());
                break;
        }
    }

    public void printWinner(String name) {
        System.out.printf(TextHelper.ANSI_GREEN + "Congratulations on the win, %s. Game over!\n" + TextHelper.ANSI_RESET, name);
    }

    public void onArrangeShipsHint(String playerName, int fieldSize) {
        String hint = GameDisplay.createShipHint(FieldCalculator.calculateShipCounts(fieldSize));
        System.out.println();
        printShipArrangementInstructions(playerName, hint, fieldSize);
    }

    public static void printShipArrangementInstructions(String playerName, String hint, int fieldSize) {
        String lastColumnHeader = CoordinateHelper.numberCoordinateToLetter(fieldSize - 1);
        System.out.printf("""
                        %s, arrange your ships as follows: %s.
                        Each ship must have coordinates X (A-%s) и Y (1-%d) depending on the field size.
                        """,
                playerName, hint, lastColumnHeader, fieldSize);
    }
}
