package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class IceBeamFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new SpecialAttack("Ice Beam", 70);
    }
}