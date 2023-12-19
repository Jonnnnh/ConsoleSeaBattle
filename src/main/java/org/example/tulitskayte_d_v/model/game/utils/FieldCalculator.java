package org.example.tulitskayte_d_v.model.game.utils;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.Arrays;

public class FieldCalculator {
    private static final double[] fieldSizes = new double[]{5, 10, 15, 20, 25, 30, 50, 70, 100};
    private static final double[] oneDeckShipCounts = new double[]{3, 4, 6, 8, 10, 12, 20, 28, 40};
    private static final double[] twoDeckShipCounts = new double[]{2, 3, 4, 5, 6, 7, 11, 15, 21};
    private static final double[] threeDeckShipCounts = new double[]{0, 2, 3, 4, 5, 6, 10, 14, 20};
    private static final double[] fourDeckShipCounts = new double[]{0, 1, 2, 3, 4, 5, 9, 13, 19};

    public static int calculateExpectedNumberOfShips(int fieldSize) {
        int[] shipCounts = calculateShipCounts(fieldSize);
        return Arrays.stream(shipCounts).sum();
    }

    public static int[] calculateShipCounts(int fieldSize) {
        int[] shipCounts = new int[4];
        if (fieldSize < 5) {
            throw new IllegalArgumentException("Размер поля должен быть не менее 5");
        }
        if (fieldSize <= 100) {
            shipCounts[0] = (int) calculateSplineValue(fieldSizes, oneDeckShipCounts, fieldSize);
            shipCounts[1] = (int) calculateSplineValue(fieldSizes, twoDeckShipCounts, fieldSize);
            shipCounts[2] = (int) calculateSplineValue(fieldSizes, threeDeckShipCounts, fieldSize);
            shipCounts[3] = (int) calculateSplineValue(fieldSizes, fourDeckShipCounts, fieldSize);
        } else {
            shipCounts[0] = calculateExtrapolatedValue(fieldSize, fieldSizes, oneDeckShipCounts);
            shipCounts[1] = calculateExtrapolatedValue(fieldSize, fieldSizes, twoDeckShipCounts);
            shipCounts[2] = calculateExtrapolatedValue(fieldSize, fieldSizes, threeDeckShipCounts);
            shipCounts[3] = calculateExtrapolatedValue(fieldSize, fieldSizes, fourDeckShipCounts);

        }

        return shipCounts;
    }

    private static int calculateExtrapolatedValue(int fieldSize, double[] sizes, double[] counts) {
        int lastIdx = sizes.length - 1;
        double x1 = sizes[lastIdx - 1];
        double y1 = counts[lastIdx - 1];
        double x2 = sizes[lastIdx];
        double y2 = counts[lastIdx];

        double slope = (y2 - y1) / (x2 - x1);
        double intercept = y1 - slope * x1;

        return (int) (slope * fieldSize + intercept);
    }

    private static double calculateSplineValue(double[] x, double[] y, int xValue) {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction splineFunction = interpolator.interpolate(x, y);
        return splineFunction.value(xValue);
    }
}