package org.example.tulitskayte_d_v.model.game.utils;


import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.player.*;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.history.GameHistory;
import org.example.tulitskayte_d_v.model.game.utils.history.Move;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.view.GameDisplay;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class GamePlayer {
    private int botCounter = 0;
    private final GameDisplay gameDisplay;
    private int fieldSize;

    public GamePlayer() {
        this.gameDisplay = new GameDisplay();
    }

    public void startGame(Player firstPlayer, Player secondPlayer) {
        Scanner sc = new Scanner(System.in);
        fieldSize = promptForFieldSize(sc);
        setupGame(sc, firstPlayer, secondPlayer);
        playGame(firstPlayer, secondPlayer, sc);
    }

    private void setupGame(Scanner sc, Player firstPlayer, Player secondPlayer) {
        setupPlayerShips(sc, firstPlayer);
        setupPlayerShips(sc, secondPlayer);
    }

    private void playGame(Player firstPlayer, Player secondPlayer, Scanner sc) {
        GameHistory gameHistory = new GameHistory();
        GamePhase state = GamePhase.FIRST_PLAYER_MOTION;

        while (state != GamePhase.END) {
            state = performGameRound(firstPlayer, secondPlayer, state, gameHistory, sc);
        }
    }

    private GamePhase performGameRound(Player firstPlayer, Player secondPlayer, GamePhase state, GameHistory gameHistory, Scanner sc) {
        GameState gameStateBeforeMove = new GameState(firstPlayer.getBattleField(), secondPlayer.getBattleField(), state);

        state = (state == GamePhase.FIRST_PLAYER_MOTION)
                ? playerMove(firstPlayer, secondPlayer, state)
                : playerMove(secondPlayer, firstPlayer, state);

        if (state == GamePhase.END) {
            return state;
        }

        GameState gameStateAfterMove = new GameState(firstPlayer.getBattleField(), secondPlayer.getBattleField(), state);
        gameHistory.saveMove(new Move(gameStateBeforeMove, gameStateAfterMove));

        gameDisplay.printBothBattleFields((state == GamePhase.FIRST_PLAYER_MOTION) ? secondPlayer : firstPlayer, (state == GamePhase.FIRST_PLAYER_MOTION) ? firstPlayer : secondPlayer);
        state = checkAndHandleUndoOption(firstPlayer, secondPlayer, state, gameHistory, sc);
        return state;

    }

    private GamePhase checkAndHandleUndoOption(Player firstPlayer, Player secondPlayer, GamePhase state, GameHistory gameHistory, Scanner sc) {
        while (gameHistory.canUndo()) {
            System.out.println("Желаете отменить ход? (да/нет)");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("да")) {
                GameState prevState = gameHistory.undo();
                if (prevState != null) {
                    restoreGameState(prevState, firstPlayer, secondPlayer);
                    state = prevState.getCurrentTurn() == GamePhase.FIRST_PLAYER_MOTION ? GamePhase.SECOND_PLAYER_MOTION : GamePhase.FIRST_PLAYER_MOTION; // переключение текущего игрока
                    gameDisplay.printBothBattleFields(firstPlayer, secondPlayer);
                }
            } else {
                break;
            }
        }
        return state;
    }

    private int promptForFieldSize(Scanner sc) {
        System.out.println("Введите размер поля (минимум 5):");
        int size;
        while (true) {
            if (sc.hasNextInt()) {
                size = sc.nextInt();
                sc.nextLine();
                if (size >= 5) {
                    break;
                } else {
                    System.out.println("Размер поля должен быть не меньше 5. Пожалуйста, попробуйте снова:");
                }
            } else {
                System.out.println("Неверный ввод. Пожалуйста, введите число.");
                sc.next();
                sc.nextLine();
            }
        }
        return size;
    }

    private boolean isBotGame(Player firstPlayer, Player secondPlayer) {
        return firstPlayer.getStrategy().isBot() && secondPlayer.getStrategy().isBot();
    }

    private GamePhase restoreGameState(GameState gameState, Player firstPlayer, Player secondPlayer) {
        firstPlayer.setBattleField(gameState.getBattleFieldPlayer1().deepCopy()); // восстановление состояния игровых полей
        secondPlayer.setBattleField(gameState.getBattleFieldPlayer2().deepCopy());
        return gameState.getCurrentTurn();// восстановление текущей фазы игры
    }

    public GamePhase playerMove(Player player, Player enemy, GamePhase state) {
        Coordinate coordinate;
        boolean isValidMove;

        do {
            coordinate = player.getStrategy().makeMove(enemy.getBattleField());
            isValidMove = isValidMove(coordinate, enemy);
            if (!isValidMove) {
                System.out.println("Неверный ход. Пожалуйста, выберите другие координаты.");
            }
        } while (!isValidMove);
        HitResults resultOfMove = enemy.move(coordinate);
        gameDisplay.processMoveResult(resultOfMove, player);
        return updateGameState(resultOfMove, state, player, enemy);
    }

    private static boolean isValidMove(Coordinate coordinate, Player enemy) {
        return enemy.getBattleField().isValidMove(coordinate);
    }

    public Player initializePlayer(Scanner sc, String playerLabel) {
        System.out.printf("\n%s, введите 0, если хотите, чтобы вашу игру вел бот.\n", playerLabel);
        String input = sc.nextLine();
        GameStrategy strategy;
        String playerName;

        if (Objects.equals(input, "0")) {
            botCounter++;
            playerName = "Bot" + botCounter;
            strategy = chooseBotStrategy(); // Выбор стратегии для бота
        } else {
            System.out.printf("\n%s, введите свое имя:\n", playerLabel);
            playerName = sc.nextLine();
            strategy = new HumanStrategy();
        }

        return new Player(playerName, strategy);
    }
    private GameStrategy chooseBotStrategy() {
        if (fieldSize > 30) {
            return new BotDiagonalStrategy();
        } else {
            return new BotGeniusStrategy();
        }
    }
    public void setupPlayerShips(Scanner sc, Player player) {
        boolean shipsArranged = false;
        while (!shipsArranged) {
            try {
                if (!player.getStrategy().isBot()) {
                    gameDisplay.arrangeHint(player.getName(), fieldSize);
                }
                ArrayList<Ship> ships = new ArrayList<>();
                player.getStrategy().placeShips(new BattleField(fieldSize, ships), ships);
                player.setBattleField(new BattleField(fieldSize, ships));
                shipsArranged = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Пожалуйста, попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Произошла неожиданная ошибка: " + e.getMessage());
            }
        }
    }

    public GamePhase updateGameState(HitResults resultOfMove, GamePhase state, Player player, Player enemy) {
        if (enemy.getBattleField().isEnemyLose()) {
            gameDisplay.printWinner(player.getName());
            return GamePhase.END;
        }
        return (state == GamePhase.FIRST_PLAYER_MOTION)
                ? GamePhase.SECOND_PLAYER_MOTION
                : GamePhase.FIRST_PLAYER_MOTION;
    }
}