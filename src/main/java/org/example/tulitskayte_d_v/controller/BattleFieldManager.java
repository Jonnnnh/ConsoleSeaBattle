package org.example.tulitskayte_d_v.controller;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;
import org.example.tulitskayte_d_v.model.ships.ShipDeck;
import org.example.tulitskayte_d_v.model.ships.ShipStates;

import java.util.List;

public class BattleFieldManager {

    private BattleField battleField;

    public BattleFieldManager(int size, List<Ship> ships) {
        this.battleField = new BattleField(size, ships);
    }

    // обеспечивает безопасный и контролируемый доступ к функционалу поля битвы
    public HitResults hitBattleField(Coordinate coordinate) {
        if (!battleField.isValidMove(coordinate)) {
            throw new IllegalStateException("Invalid move");
        }
        return battleField.hitBattleField(coordinate);
    }

    // метод для установки кораблей
    public void arrangeShips(List<Ship> ships) {
        battleField.arrangeTheShips(ships);
    }

    // возвращает копию данных, предотвращая непосредственное изменение внутреннего состояния
    public Cell[][] getCells() {
        return battleField.copyCells();
    }

    public boolean isEnemyLose() {
        return battleField.isEnemyLose();
    }

    public int getSize() {
        return battleField.getSize();
    }
}
