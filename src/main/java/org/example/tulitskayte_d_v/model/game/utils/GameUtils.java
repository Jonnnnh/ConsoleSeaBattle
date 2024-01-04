package org.example.tulitskayte_d_v.model.game.utils;

import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipFactory;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {
    public static List<Ship> convertStringToShips(int fieldSize, String data) {
        int expectedNumberOfShips = FieldCalculator.calculateExpectedNumberOfShips(fieldSize);
        List<Ship> ships = new ArrayList<>();
        String[] splitShips = data.split(",\\s*");

        if (splitShips.length != expectedNumberOfShips) {
            throw new IllegalArgumentException("We need to set up " + expectedNumberOfShips + " ships. Your input: " + data);
        }

        for (String shipCoords : splitShips) {
            ships.add(createShipFromCoords(shipCoords));
        }

        checkShips(ships, fieldSize);
        return ships;
    }

    private static Ship createShipFromCoords(String shipCoords) {
        String[] coords = shipCoords.split("-");
        if (coords.length != 2) {
            throw new IllegalArgumentException("Incorrect ship coordinate format: " + shipCoords);
        }

        Coordinate start = CoordinateParser.getCoordinate(coords[0]);
        Coordinate end = CoordinateParser.getCoordinate(coords[1]);
        return ShipFactory.createShip(createShipDecks(start, end));
    }

    private static List<ShipDeck> createShipDecks(Coordinate start, Coordinate end) {
        List<ShipDeck> shipDecks = new ArrayList<>();
        if (start.getRow() == end.getRow()) {
            for (int col = Math.min(start.getColumn(), end.getColumn()); col <= Math.max(start.getColumn(), end.getColumn()); col++) {
                shipDecks.add(new ShipDeck(start.getRow(), col));
            }
        } else {
            for (int row = Math.min(start.getRow(), end.getRow()); row <= Math.max(start.getRow(), end.getRow()); row++) {
                shipDecks.add(new ShipDeck(row, start.getColumn()));
            }
        }
        return shipDecks;
    }

    private static void checkShips(List<Ship> ships, int fieldSize) {
        int[] countByDecks = new int[4];
        int[] expectedCounts = FieldCalculator.calculateShipCounts(fieldSize);

        for (Ship ship : ships) {
            int decks = ship.getDecks().size();
            if (decks < 1 || decks > 4) {
                throw new IllegalStateException("Found the wrong ship with the number of decks: " + decks);
            }
            countByDecks[decks - 1]++;
        }
        if (fieldSize == 5) {
            if (countByDecks[2] > 0 || countByDecks[3] > 0) {
                throw new IllegalStateException("A 5x5 field may not contain three-deck or four-deck ships.");
            }
        }
        for (int i = 0; i < countByDecks.length; i++) {
            if (countByDecks[i] != expectedCounts[i]) {
                throw new IllegalStateException("Wrong number " + (i + 1) + "-of deck ships: expected " + expectedCounts[i] + ", found " + countByDecks[i]);
            }
        }

        checkForOverlappingShips(ships);
    }

    private static void checkForOverlappingShips(List<Ship> ships) {
        for (int i = 0; i < ships.size(); i++) {
            for (int j = i + 1; j < ships.size(); j++) {
                for (ShipDeck deck1 : ships.get(i).getDecks()) {
                    for (ShipDeck deck2 : ships.get(j).getDecks()) {
                        if (Math.abs(deck1.getRow() - deck2.getRow()) <= 1 &&
                                Math.abs(deck1.getColumn() - deck2.getColumn()) <= 1) {
                            throw new IllegalStateException("The ships overlap each other. Ship 1: " + ships.get(i) + ", Ship 2: " + ships.get(j));
                        }
                    }
                }
            }
        }
    }

}
