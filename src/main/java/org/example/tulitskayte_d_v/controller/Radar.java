package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.model.game.Coordinate;

public class Radar {
    private Coordinate lastShotCoordinate;

    public void updateLastShotCoordinate(Coordinate coordinate) {
        this.lastShotCoordinate = coordinate;
    }

    public Coordinate getLastShotCoordinate() {
        return lastShotCoordinate;
    }
    public Radar deepCopy() {
        Radar clonedRadar = new Radar();
        if (this.lastShotCoordinate != null) {
            clonedRadar.updateLastShotCoordinate(new Coordinate(
                    this.lastShotCoordinate.getRow(),
                    this.lastShotCoordinate.getColumn()
            ));
        }
        return clonedRadar;
    }
}
