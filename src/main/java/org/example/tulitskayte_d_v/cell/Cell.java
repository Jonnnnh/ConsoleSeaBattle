package org.example.tulitskayte_d_v.cell;

import org.example.tulitskayte_d_v.model.ships.ShipDeck;

public class Cell {
    private final int column;
    private final int row;
    private boolean shipHere;
    private CellStates state;
    private final ShipDeck shipDeck;

    public Cell(int column, int row, boolean shipHere) {
        this.column = column;
        this.row = row;
        this.shipHere = shipHere;
        this.state = CellStates.EMPTY;
        this.shipDeck = null;
    }
    public Cell deepCopy() {
        Cell clonedCell = new Cell(this.row, this.column, this.shipHere);
        clonedCell.setState(this.state);
        return clonedCell;
    }

    public void setShipHere() {
        this.shipHere = true;
    }

    public void setState(CellStates state) {
        this.state = state;
    }

    public CellStates getState() {
        return state;
    }

    public boolean isThereAShip() {
        return shipHere;
    }
    public ShipDeck getShipDeck() {
        return shipDeck;
    }
}
