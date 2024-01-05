package org.example.tulitskayte_d_v.model.player;

public class PlayerBuilder {
    private String name;
    private PlayerLogic logic;

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder setLogic(PlayerLogic logic) {
        this.logic = logic;
        return this;
    }

    public Player build() {
        PlayerStorage storage = new PlayerStorage();
        storage.setName(name);

        if (logic == null) {
            throw new IllegalStateException("Player logic must be set");
        }

        return new Player(storage, logic);
    }
}
