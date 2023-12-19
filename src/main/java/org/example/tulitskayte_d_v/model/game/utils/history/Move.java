package org.example.tulitskayte_d_v.model.game.utils.history;

import org.example.tulitskayte_d_v.model.game.utils.GameState;

public record Move(GameState gameStateBeforeMove, GameState gameStateAfterMove) {
}
