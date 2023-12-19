package org.example.tulitskayte_d_v.model.ships;


import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.game.utils.CoordinateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Ship {
    private final List<ShipDeck> shipDecks;
    private int aliveDecksCounter;

    private ShipStates state;

    public Ship(List<ShipDeck> shipDecks) {
        this.shipDecks = shipDecks;
        this.aliveDecksCounter = shipDecks.size();
        this.state = ShipStates.UNTAPPED;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (ShipDeck deck : shipDecks) {
            joiner.add(deck.getRow() + "" + deck.getColumn());
        }
        return joiner.toString();
    }

    public List<ShipDeck> getDecks() {
        return new ArrayList<>(shipDecks);
    }

    private void setState(ShipStates state) {
        this.state = state;
    }

    public ShipStates getState() {
        return state;
    }

    public int getAliveDecksCounter() {
        return aliveDecksCounter;
    }

    public ShipStates hitTheShip(Coordinate coordinate) {
        for (ShipDeck shipDeck : shipDecks) {
            if (shipDeck.getRow() == coordinate.getRow() && shipDeck.getColumn() == coordinate.getColumn()) {
                shipDeck.hitTheDeck(); // ударяю палубу
                aliveDecksCounter--;
            }
        }
        if (aliveDecksCounter == 0) { // обновляю статус корабля
            this.state = ShipStates.KILLED;
        } else if (aliveDecksCounter != shipDecks.size()) {
            this.state = ShipStates.HURT;
        }
        return this.state;
    }
    public Ship clone() {
        List<ShipDeck> clonedDecks = new ArrayList<>();
        for (ShipDeck deck : this.shipDecks) {
            clonedDecks.add(deck.clone());
        }
        return new Ship(clonedDecks);
    }
}
