package org.example.tulitskayte_d_v.model.player;

public class PlayerBuilder {
    private String name;
    private GameStrategy strategy;

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public void setStrategy(GameStrategy strategy) {
        this.strategy = strategy;
    }

    public Player build() {
        return new Player(name, strategy);
    }
}
