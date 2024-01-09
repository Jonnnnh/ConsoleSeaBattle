package org.example.tulitskayte_d_v.model.game.utils;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;

public class GameState {
    private final MoveField battleFieldPlayer1;
    private final MoveField battleFieldPlayer2;
    private final GamePhase currentTurn;

    public GameState(MoveField battleFieldPlayer1, MoveField battleFieldPlayer2, GamePhase currentTurn) {
        this.battleFieldPlayer1 = battleFieldPlayer1.deepCopy();
        this.battleFieldPlayer2 = battleFieldPlayer2.deepCopy();
        this.currentTurn = currentTurn;
    }

    public MoveField getBattleFieldPlayer1() {
        return battleFieldPlayer1;
    }

    public MoveField getBattleFieldPlayer2() {
        return battleFieldPlayer2;
    }

    public GamePhase getCurrentTurn() {
        return currentTurn;
    }
}
