package com.taller.patrones.infrastructure.observer;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Observer que actualiza estadísticas en tiempo real.
 */
public class StatsObserver implements DamageObserver {

    private int totalDamageDealt = 0;
    private int attackCount = 0;

    @Override
    public void onDamageDealt(Character attacker, Character defender, Attack attack, int damage) {
        totalDamageDealt += damage;
        attackCount++;

        double avgDamage = attackCount > 0 ? (double) totalDamageDealt / attackCount : 0;

        System.out.println("[STATS] Total ataques: " + attackCount +
                         " | Daño total: " + totalDamageDealt +
                         " | Daño promedio: " + String.format("%.2f", avgDamage));
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public int getAttackCount() {
        return attackCount;
    }
}

