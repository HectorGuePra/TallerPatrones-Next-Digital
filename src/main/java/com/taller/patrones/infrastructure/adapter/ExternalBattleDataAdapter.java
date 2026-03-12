package com.taller.patrones.infrastructure.adapter;

import com.taller.patrones.domain.Character;

/**
 * Patrón Adapter: Interfaz común para adaptar datos externos de batalla
 * a nuestro modelo de dominio (Character).
 *
 * Permite que diferentes proveedores con formatos distintos puedan
 * ser usados de forma uniforme sin modificar el controller o service.
 */
public interface ExternalBattleDataAdapter {

    /**
     * Obtiene el jugador adaptado a nuestro dominio
     * @return Character del jugador
     */
    Character getPlayer();

    /**
     * Obtiene el enemigo adaptado a nuestro dominio
     * @return Character del enemigo
     */
    Character getEnemy();

    /**
     * Obtiene el nombre del proveedor (para logging/debugging)
     * @return Nombre del proveedor
     */
    String getProviderName();
}

