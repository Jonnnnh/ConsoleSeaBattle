package org.example.tulitskayte_d_v.model.game.utils.history;

import org.example.tulitskayte_d_v.model.game.utils.GameState;

import java.util.Objects;

public final class Move {
    private final GameState gameStateBeforeMove;
    private final GameState gameStateAfterMove;

    public Move(GameState gameStateBeforeMove, GameState gameStateAfterMove) {
        this.gameStateBeforeMove = gameStateBeforeMove;
        this.gameStateAfterMove = gameStateAfterMove;
    }

    public GameState gameStateBeforeMove() {
        return gameStateBeforeMove;
    }

    public GameState gameStateAfterMove() {
        return gameStateAfterMove;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Move) obj;
        return Objects.equals(this.gameStateBeforeMove, that.gameStateBeforeMove) &&
                Objects.equals(this.gameStateAfterMove, that.gameStateAfterMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameStateBeforeMove, gameStateAfterMove);
    }

    @Override
    public String toString() {
        return "Move[" +
                "gameStateBeforeMove=" + gameStateBeforeMove + ", " +
                "gameStateAfterMove=" + gameStateAfterMove + ']';
    }

}
