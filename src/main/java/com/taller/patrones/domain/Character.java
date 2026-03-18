package com.taller.patrones.domain;

/**
 * Representa un personaje en combate.
 * Utiliza el patrón Builder para construir objetos complejos paso a paso.
 */
public class Character {

    private final String name;
    private int currentHp;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final int speed;
    private final String equipment;
    private final String characterClass;
    private final boolean hasBuff;

    // Constructor privado - solo accesible desde el Builder
    private Character(Builder builder) {
        this.name = builder.name;
        this.maxHp = builder.maxHp;
        this.currentHp = builder.currentHp > 0 ? builder.currentHp : builder.maxHp;
        this.attack = builder.attack;
        this.defense = builder.defense;
        this.speed = builder.speed;
        this.equipment = builder.equipment;
        this.characterClass = builder.characterClass;
        this.hasBuff = builder.hasBuff;
    }

    // Constructor legacy para compatibilidad con código existente
    public Character(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.equipment = null;
        this.characterClass = "WARRIOR";
        this.hasBuff = false;
    }

    public String getName() { return name; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public String getEquipment() { return equipment; }
    public String getCharacterClass() { return characterClass; }
    public boolean hasBuff() { return hasBuff; }

    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, currentHp - damage);
    }

    public void restoreHp(int hp) {
        this.currentHp = Math.max(0, Math.min(maxHp, hp));
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public double getHpPercentage() {
        return maxHp > 0 ? (double) currentHp / maxHp * 100 : 0;
    }

    /**
     * Patrón Builder para construir personajes complejos paso a paso.
     * Permite valores por defecto y construcción legible.
     */
    public static class Builder {
        // Campos obligatorios
        private final String name;

        // Campos opcionales con valores por defecto
        private int maxHp = 100;
        private int currentHp = 0; // 0 significa "usar maxHp"
        private int attack = 10;
        private int defense = 10;
        private int speed = 10;
        private String equipment = null;
        private String characterClass = "WARRIOR";
        private boolean hasBuff = false;

        /**
         * Constructor del Builder - solo requiere el nombre (obligatorio)
         */
        public Builder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del personaje no puede estar vacío");
            }
            this.name = name;
        }

        public Builder maxHp(int maxHp) {
            if (maxHp <= 0) {
                throw new IllegalArgumentException("maxHp debe ser positivo");
            }
            this.maxHp = maxHp;
            return this;
        }

        public Builder currentHp(int currentHp) {
            this.currentHp = currentHp;
            return this;
        }

        public Builder attack(int attack) {
            if (attack < 0) {
                throw new IllegalArgumentException("attack no puede ser negativo");
            }
            this.attack = attack;
            return this;
        }

        public Builder defense(int defense) {
            if (defense < 0) {
                throw new IllegalArgumentException("defense no puede ser negativo");
            }
            this.defense = defense;
            return this;
        }

        public Builder speed(int speed) {
            if (speed < 0) {
                throw new IllegalArgumentException("speed no puede ser negativo");
            }
            this.speed = speed;
            return this;
        }

        public Builder equipment(String equipment) {
            this.equipment = equipment;
            return this;
        }

        public Builder characterClass(String characterClass) {
            this.characterClass = characterClass;
            return this;
        }

        public Builder hasBuff(boolean hasBuff) {
            this.hasBuff = hasBuff;
            return this;
        }

        /**
         * Construye el personaje con los valores configurados
         */
        public Character build() {
            return new Character(this);
        }
    }
}
