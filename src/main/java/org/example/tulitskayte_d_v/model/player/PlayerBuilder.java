package org.example.tulitskayte_d_v.model.player;

import org.example.tulitskayte_d_v.controller.MoveField;
import org.example.tulitskayte_d_v.controller.ShipPlacementField;

public class PlayerBuilder {
    private String name;
    private PlayerLogic logic;
    private ShipPlacementField battleFieldManager;
    private MoveField enemyBattleFieldManager;
    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder setLogic(PlayerLogic logic) {
        this.logic = logic;
        return this;
    }
    public PlayerBuilder setBattleFieldManager(ShipPlacementField battleFieldManager) {
        this.battleFieldManager = battleFieldManager;
        return this;
    }

    public PlayerBuilder setEnemyBattleFieldManager(MoveField enemyBattleFieldManager) {
        this.enemyBattleFieldManager = enemyBattleFieldManager;
        return this;
    }
    public Player build() {
        if (logic == null) {
            throw new IllegalStateException("Player logic must be set");
        }

        PlayerStorage storage = new PlayerStorage(name, battleFieldManager, enemyBattleFieldManager);
        return new Player(storage, logic);
    }
}
