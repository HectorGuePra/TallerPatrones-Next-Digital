package com.taller.patrones.infrastructure.adapter;

/**
 * DTO para datos externos del Proveedor 2.
 * Formato: player: {name, health, attack, defense, speed}, enemy: {name, health, attack, defense, speed}
 */
public class Provider2BattleData {
    private FighterData player;
    private FighterData enemy;

    public Provider2BattleData() {}

    public Provider2BattleData(FighterData player, FighterData enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public FighterData getPlayer() { return player; }
    public void setPlayer(FighterData player) { this.player = player; }

    public FighterData getEnemy() { return enemy; }
    public void setEnemy(FighterData enemy) { this.enemy = enemy; }

    /**
     * Clase interna para representar un luchador en el formato del Proveedor 2
     */
    public static class FighterData {
        private String name;
        private int health;
        private int attack;
        private int defense;
        private int speed;

        public FighterData() {}

        public FighterData(String name, int health, int attack, int defense, int speed) {
            this.name = name;
            this.health = health;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getHealth() { return health; }
        public void setHealth(int health) { this.health = health; }

        public int getAttack() { return attack; }
        public void setAttack(int attack) { this.attack = attack; }

        public int getDefense() { return defense; }
        public void setDefense(int defense) { this.defense = defense; }

        public int getSpeed() { return speed; }
        public void setSpeed(int speed) { this.speed = speed; }
    }
}

