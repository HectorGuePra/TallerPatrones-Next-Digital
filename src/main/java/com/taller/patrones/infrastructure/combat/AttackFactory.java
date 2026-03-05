package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

public abstract class AttackFactory {
    public abstract Attack createAttack();
}