package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.cell.Cell;
import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.model.game.Coordinate;
import org.example.tulitskayte_d_v.model.ships.HitResults;
import org.example.tulitskayte_d_v.model.ships.Ship;

import java.util.List;

public class BattleFieldManager { // также можно добавить информацию уже об убитых кораблях,
    // сколько таких кораблей, и информацию об подбиты кораблях через гет, чтобы стратегия принимала решения куда бить
    private BattleField battleField;

    public BattleFieldManager(int size, List<Ship> ships) {
        this.battleField = new BattleField(size, ships);
    }
    // Публичный метод для атаки по координатам.
    // Он обеспечивает безопасный и контролируемый доступ к функционалу поля битвы.
    public HitResults hitBattleField(Coordinate coordinate) {
        if (!battleField.isValidMove(coordinate)) {
            // Обработка недопустимой попытки атаки
            throw new IllegalStateException("Invalid move");
        }
        return battleField.hitBattleField(coordinate);
    }
    // Публичный метод, предоставляющий информацию о клетках поля битвы.
    // Возвращает копию данных, предотвращая непосредственное изменение внутреннего состояния.
    public Cell[][] getCells() {
        return battleField.copyCells();
    }
    // Метод для проверки условия победы. Не позволяет изменять состояние поля.
    public boolean isEnemyLose() {
        return battleField.isEnemyLose();
    }
    // Метод для получения размера поля.
    public int getSize() {
        return battleField.getSize();
    }
}
