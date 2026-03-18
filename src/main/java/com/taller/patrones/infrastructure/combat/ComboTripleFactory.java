package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;

import java.util.List;

public class ComboTripleFactory extends AttackFactory {
    @Override
    public Attack createAttack() {
        return new CompositeAttack(
                "Combo Triple",
                List.of(
                        new TackleFactory().createAttack(),
                        new SlashFactory().createAttack(),
                        new FireballFactory().createAttack()
                )
        );
    }
}

