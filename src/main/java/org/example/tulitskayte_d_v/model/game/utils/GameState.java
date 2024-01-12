package org.example.tulitskayte_d_v.model.game.utils;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.Radar;
import org.example.tulitskayte_d_v.controller.ShotMatrix;
import org.example.tulitskayte_d_v.model.player.PlayerStorage;

public class GameState {
    private final PlayerStorage battleFieldPlayer1;
    private final PlayerStorage battleFieldPlayer2;
    private final GamePhase currentTurn;

    public GameState(PlayerStorage battleFieldPlayer1, PlayerStorage battleFieldPlayer2, GamePhase currentTurn) {
        this.battleFieldPlayer1 = battleFieldPlayer1.deepCopy();
        this.battleFieldPlayer2 = battleFieldPlayer2.deepCopy();
        this.currentTurn = currentTurn;
    }

    public PlayerStorage getBattleFieldPlayer1() {
        return battleFieldPlayer1;
    }

    public PlayerStorage getBattleFieldPlayer2() {
        return battleFieldPlayer2;
    }

    public GamePhase getCurrentTurn() {
        return currentTurn;
    }
}
