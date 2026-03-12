package com.taller.patrones.infrastructure.observer;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Patrón Observer: Interfaz para observadores de eventos de daño.
 */
public interface DamageObserver {
    void onDamageDealt(Character attacker, Character defender, Attack attack, int damage);
}

