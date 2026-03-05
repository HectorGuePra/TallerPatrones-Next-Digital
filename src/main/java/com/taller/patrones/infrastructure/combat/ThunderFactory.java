package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class ThunderFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new SpecialAttack("Thunder", 90);
    }
}