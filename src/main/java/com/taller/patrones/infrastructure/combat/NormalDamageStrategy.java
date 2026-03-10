package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Character;

public class NormalDamageStrategy implements DamageStrategy {
    @Override
    public int calculate(Character attacker, Character defender, int basePower) {
        int raw = (attacker.getAttack() * basePower) / 100;
        return Math.max(1, raw - defender.getDefense());
    }
}

