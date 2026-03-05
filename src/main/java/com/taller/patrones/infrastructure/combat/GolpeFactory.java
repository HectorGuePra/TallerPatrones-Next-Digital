package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class GolpeFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new PhysicalAttack("Golpe", 30);
    }
}