package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class PhysicalAttack extends Attack {
    

    public PhysicalAttack(String name, int basePower) {
        super(name, basePower, new NormalDamageStrategy());
    }

    public PhysicalAttack(String name, int basePower, DamageStrategy damageStrategy) {
        super(name, basePower, damageStrategy);
    }

    @Override
    public int calculateDamage(Character attacker, Character defender) {
        if (damageStrategy != null) {
            return damageStrategy.calculate(attacker, defender, basePower);
        }
        // Fallback en caso de que no haya estrategia (no debería pasar)
        int raw = attacker.getAttack() * basePower / 100;
        return Math.max(1, raw - defender.getDefense());
    }
}