package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

public class StatusAttack extends Attack {
    

    public StatusAttack(String name, int basePower) {
        super(name, basePower);
    }

    @Override
    public int calculateDamage(Character attacker, Character defender) {
        // Los ataques de estado podrían no hacer daño directo, pero podrían aplicar efectos secundarios.
        return attacker.getAttack();
    }
}