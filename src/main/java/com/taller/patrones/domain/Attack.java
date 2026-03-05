package com.taller.patrones.domain;

/**
 * Representa un ataque que puede ejecutar un personaje.
 */
public abstract class Attack {

    protected final String name;
    protected final int basePower;

    public Attack(String name, int basePower) {
        this.name = name;
        this.basePower = basePower;
    }

    public String getName() { return name; }
    public int getBasePower() { return basePower; }

    public abstract int calculateDamage(Character attacker, Character defender);
}
