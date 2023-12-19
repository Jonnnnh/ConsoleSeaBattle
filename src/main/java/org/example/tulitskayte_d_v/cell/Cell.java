package org.example.tulitskayte_d_v.cell;

import org.example.tulitskayte_d_v.model.ships.ShipDeck;

public class Cell {
    private final int column;
    private final int row;
    private boolean shipHere;
    private CellStates state;
    private ShipDeck shipDeck;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.shipHere = false;
        this.state = CellStates.NULL;
    }

    public Cell(int column, int row, boolean shipHere) {
        this.column = column;
        this.row = row;
        this.shipHere = shipHere;
        this.state = CellStates.NULL;
        this.shipDeck = null;
    }
    public Cell clone() {
        Cell clonedCell = new Cell(this.row, this.column, this.shipHere);
        clonedCell.setState(this.state);
        return clonedCell;
    }

    public String stateToStr() {
        return state.toString();
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

    public void setShipDeck(ShipDeck shipDeck) {
        this.shipDeck = shipDeck;
        this.shipHere = (shipDeck != null);
    }
}
