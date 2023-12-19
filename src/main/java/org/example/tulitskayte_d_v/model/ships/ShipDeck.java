package org.example.tulitskayte_d_v.model.ships;

public class ShipDeck {
    private final int row;
    private final int column;

    private DeckConditions state;
    private OpponentDeckConditions enemyState;

    public ShipDeck(int row, int column) {
        this.row = row;
        this.column = column;
        this.state = DeckConditions.UNHURT;
        this.enemyState = OpponentDeckConditions.INVISIBLE;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private void setState(DeckConditions state) {
        this.state = state;
    }

    public DeckConditions getState() {
        return state;
    }

    public OpponentDeckConditions getEnemyState() {
        return enemyState;
    }

    public void hitTheDeck() {
        this.state = DeckConditions.HURT;
        this.enemyState = OpponentDeckConditions.VISIBLE;
    }
    public ShipDeck clone() {
        ShipDeck clonedDeck = new ShipDeck(this.row, this.column);
        clonedDeck.setState(this.state); // Копирование состояния палубы
        clonedDeck.enemyState = this.enemyState; // Копирование состояния палубы для противника
        return clonedDeck;
    }
}
