package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class PoisonStingFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new StatusAttack("Poison Sting", 20);
    }
}