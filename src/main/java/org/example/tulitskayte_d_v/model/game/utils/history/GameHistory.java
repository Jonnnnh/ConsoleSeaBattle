package org.example.tulitskayte_d_v.model.game.utils.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameHistory {
    private final Stack<Move> history = new Stack<>();

    public void saveMove(Move move) {
        history.push(move);
    }

    public Move undoMove() {
        return history.isEmpty() ? null : history.pop();
    }


    public boolean canUndo() {
        return !history.isEmpty();
    } // проверка, есть ли история для отката
    public List<Move> getMoves() {
        return new ArrayList<>(history);
    }
}