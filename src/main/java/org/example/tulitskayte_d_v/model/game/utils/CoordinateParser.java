package org.example.tulitskayte_d_v.model.game.utils;

import org.example.tulitskayte_d_v.model.game.Coordinate;

public class CoordinateParser {
    public static Coordinate getCoordinate(String input) {
        String alphaPart = input.replaceAll("[^A-Za-z]", "");
        String numPart = input.replaceAll("[^0-9]", "");

        int column = 0;
        for (int i = 0; i < alphaPart.length(); i++) {
            column = column * 26 + (alphaPart.toUpperCase().charAt(i) - 'A' + 1);
        }
        column -= 1;

        int row = Integer.parseInt(numPart) - 1;

        return new Coordinate(row, column);
    }
}
