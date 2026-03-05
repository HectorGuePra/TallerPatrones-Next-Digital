package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class TackleFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new PhysicalAttack("Tackle", 40);
    }
}