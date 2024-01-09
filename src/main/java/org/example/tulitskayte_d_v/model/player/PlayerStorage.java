package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.BattleField;
import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;

public class PlayerStorage {
    private String name;
    private ShipPlacementField battleFieldManager;
    private MoveField enemyBattleFieldManager;

    public PlayerStorage(String name, ShipPlacementField battleFieldManager, MoveField enemyBattleFieldManager) {
        this.name = name;
        this.battleFieldManager = battleFieldManager;
        this.enemyBattleFieldManager = enemyBattleFieldManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ShipPlacementField getShipPlacementField() {
        return battleFieldManager;
    }

    public void setShipPlacementField(ShipPlacementField battleFieldManager) {
        this.battleFieldManager = battleFieldManager;
    }

    public MoveField getMoveField() {
        return enemyBattleFieldManager;
    }

    public void setMoveField(MoveField enemyBattleFieldManager) {
        this.enemyBattleFieldManager = enemyBattleFieldManager;
    }
}
