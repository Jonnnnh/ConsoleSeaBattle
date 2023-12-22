package org.example.tulitskayte_d_v.model.game.utils.history;

import org.example.tulitskayte_d_v.model.game.utils.GameState;
import org.example.tulitskayte_d_v.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameHistory {
    private final Stack<Move> executedMoves = new Stack<>();
    private final Stack<Move> undoneMoves = new Stack<>();

    public void saveMove(Move move) {
        executedMoves.push(move);
        undoneMoves.clear(); // очищаем стек отменённых ходов, тк новый ход сделан
    }
    public boolean canUndo() {
        return !executedMoves.isEmpty();
    }

    public GameState undo() {
        if (!canUndo()) {
            return null;
        }
        Move move = executedMoves.pop();
        undoneMoves.push(move);
        return move.gameStateBeforeMove();
    }
}