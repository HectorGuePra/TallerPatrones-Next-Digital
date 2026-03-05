package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class SpecialAttack extends Attack {
    

    public SpecialAttack(String name, int basePower) {
        super(name, basePower);
    }

    @Override
    public int calculateDamage(Character attacker, Character defender) {
        int raw = attacker.getAttack() * basePower / 100;
        int effectiveDef = defender.getDefense() / 2;
        return Math.max(1, raw - effectiveDef);
    }
}