package com.taller.patrones.infrastructure.observer;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Observer que envía eventos a un sistema de analytics.
 */
public class AnalyticsObserver implements DamageObserver {

    @Override
    public void onDamageDealt(Character attacker, Character defender, Attack attack, int damage) {
        // Simulación de envío a analytics
        System.out.println("[ANALYTICS] Daño registrado: " + attacker.getName() +
                         " -> " + defender.getName() + " | Ataque: " + attack.getName() +
                         " | Daño: " + damage);
    }
}

