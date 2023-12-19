package org.example.tulitskayte_d_v.model.ships;

import java.util.List;

public class ShipFactory {
    public static Ship createShip(List<ShipDeck> shipDecks) {
        return new Ship(shipDecks);
    }
}
