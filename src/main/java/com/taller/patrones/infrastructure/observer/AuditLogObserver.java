package com.taller.patrones.infrastructure.observer;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Character;

/**
 * Observer que escribe en un log de auditoría.
 */
public class AuditLogObserver implements DamageObserver {

    @Override
    public void onDamageDealt(Character attacker, Character defender, Attack attack, int damage) {
        // Simulación de log de auditoría
        System.out.println("[AUDIT] Evento de combate registrado - Atacante: " + attacker.getName() +
                         ", Defensor: " + defender.getName() +
                         ", Ataque: " + attack.getName() +
                         ", Daño: " + damage +
                         ", HP restante: " + defender.getCurrentHp());
    }
}

