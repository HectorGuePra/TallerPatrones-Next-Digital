package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class PhysicalAttack extends Attack {
    

    public PhysicalAttack(String name, int basePower) {
        super(name, basePower);
    }

    @Override
    public int calculateDamage(Character attacker, Character defender) {
        int raw = attacker.getAttack() * basePower / 100;
        return Math.max(1, raw - defender.getDefense());
    }
}