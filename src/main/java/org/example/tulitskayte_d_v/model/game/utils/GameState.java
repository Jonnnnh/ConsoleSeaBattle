package org.example.tulitskayte_d_v.model.game.utils;

import org.example.tulitskayte_d_v.controller.BattleField;

public class GameState {
    private final BattleField battleFieldPlayer1;
    private final BattleField battleFieldPlayer2;
    private final GamePhase currentTurn;

    public GameState(BattleField battleFieldPlayer1, BattleField battleFieldPlayer2, GamePhase currentTurn) {
        this.battleFieldPlayer1 = battleFieldPlayer1.clone();
        this.battleFieldPlayer2 = battleFieldPlayer2.clone();
        this.currentTurn = currentTurn;
    }

    public BattleField getBattleFieldPlayer1() {
        return battleFieldPlayer1;
    }

    public BattleField getBattleFieldPlayer2() {
        return battleFieldPlayer2;
    }

    public GamePhase getCurrentTurn() {
        return currentTurn;
    }
}
