package com.taller.patrones.domain;

import com.taller.patrones.infrastructure.combat.DamageStrategy;

/**
 * Representa un ataque que puede ejecutar un personaje.
 */
public abstract class Attack {

    protected final String name;
    protected final int basePower;
    protected DamageStrategy damageStrategy;

    public Attack(String name, int basePower) {
        this.name = name;
        this.basePower = basePower;
    }
    public Attack(String name, int basePower, DamageStrategy damageStrategy) {
        this.name = name;
        this.basePower = basePower;
        this.damageStrategy = damageStrategy;
    }
    public String getName() { return name; }
    public int getBasePower() { return basePower; }

    public abstract int calculateDamage(Character attacker, Character defender);
}
