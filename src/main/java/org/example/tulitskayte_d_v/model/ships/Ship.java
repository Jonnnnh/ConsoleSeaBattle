package org.example.tulitskayte_d_v.model.ships;


import org.example.tulitskayte_d_v.model.game.Coordinate;

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


    public ShipStates hitTheShip(Coordinate coordinate) { // обработка попаданий по кораблю в определенной координате
        ShipDeck targetDeck = getDeckAtCoordinate(coordinate);
        if (targetDeck != null) {
            targetDeck.hitTheDeck();
            aliveDecksCounter--;
            updateShipState();
            return this.state;
        }
        return ShipStates.UNTAPPED;
    }

    private ShipDeck getDeckAtCoordinate(Coordinate coordinate) {
        for (ShipDeck shipDeck : shipDecks) {
            if (shipDeck.getRow() == coordinate.getRow() && shipDeck.getColumn() == coordinate.getColumn()) {
                return shipDeck;
            }
        }
        return null;
    }

    private void updateShipState() {
        if (aliveDecksCounter == 0) {
            this.state = ShipStates.KILLED;
        } else if (aliveDecksCounter != shipDecks.size()) {
            this.state = ShipStates.HURT;
        }
    }
    public Ship deepCopy() {
        List<ShipDeck> clonedDecks = new ArrayList<>();
        for (ShipDeck deck : this.shipDecks) {
            clonedDecks.add(deck.deepCopy());
        }
        return new Ship(clonedDecks);
    }
}
