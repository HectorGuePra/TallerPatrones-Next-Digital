package com.taller.patrones.infrastructure.combat;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

import java.util.List;

/**
 * Composite: un ataque que agrupa varios ataques y se comporta como uno solo.
 */
public class CompositeAttack extends Attack {

    private final List<Attack> childAttacks;

    public CompositeAttack(String name, List<Attack> childAttacks) {
        super(name, sumBasePower(childAttacks));
        this.childAttacks = childAttacks;
    }

    @Override
    public int calculateDamage(Character attacker, Character defender) {
        int totalDamage = 0;
        for (Attack attack : childAttacks) {
            totalDamage += attack.calculateDamage(attacker, defender);
        }
        return totalDamage;
    }

    private static int sumBasePower(List<Attack> attacks) {
        int total = 0;
        if (attacks == null) {
            return total;
        }
        for (Attack attack : attacks) {
            if (attack != null) {
                total += attack.getBasePower();
            }
        }
        return total;
    }
}

