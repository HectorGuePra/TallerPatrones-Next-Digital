package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public class SlashFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new PhysicalAttack("Slash", 55, new CriticalDamageStrategy());
    }
}