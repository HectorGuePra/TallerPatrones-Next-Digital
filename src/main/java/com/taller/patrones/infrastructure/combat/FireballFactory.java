package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class FireballFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new SpecialAttack("Fireball", 80);
    }
}