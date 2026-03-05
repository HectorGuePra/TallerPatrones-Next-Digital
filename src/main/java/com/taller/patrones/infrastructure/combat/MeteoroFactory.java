package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class MeteoroFactory extends AttackFactory{

    @Override
    public Attack createAttack() {
        return new SpecialAttack("Meteoro", 120);
    }
}
