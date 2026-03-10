package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Character;

public class SpecialDamageStrategy implements DamageStrategy {
    @Override
    public int calculate(Character attacker, Character defender, int basePower) {
        int raw = (attacker.getAttack() * basePower) / 100;
        int effectiveDef = defender.getDefense() / 2;
        return Math.max(1, raw - effectiveDef);
    }
}

