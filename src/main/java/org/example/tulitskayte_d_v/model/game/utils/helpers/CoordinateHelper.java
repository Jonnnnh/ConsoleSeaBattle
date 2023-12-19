package org.example.tulitskayte_d_v.model.game.utils.helpers;

public class CoordinateHelper {
    private static final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static String numberCoordinateToLetter(int coordinate) {
        StringBuilder letterBuilder = new StringBuilder();
        while (coordinate >= 0) {
            letterBuilder.insert(0, letters[coordinate % letters.length]);
            coordinate = coordinate / letters.length - 1;
        }
        return letterBuilder.toString();
    }
}
