package com.taller.patrones.infrastructure.persistence;

import com.taller.patrones.domain.Battle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Almacena las batallas activas en memoria.
 * Implementa el patrón Singleton para garantizar una única instancia
 * compartida en toda la aplicación.
 *
 * Thread-safe usando inicialización perezosa con sincronización.
 */
public class BattleRepository {

    // Instancia única del Singleton
    private static volatile BattleRepository instance;

    // Almacenamiento thread-safe de batallas, no es estatico
    private final Map<String, Battle> battles = new ConcurrentHashMap<>();

    /**
     * Constructor privado para evitar instanciación directa.
     * Solo accesible desde getInstance().
     */
    private BattleRepository() {
        // Constructor privado - patrón Singleton
    }

    /**
     * Obtiene la única instancia de BattleRepository.
     * Thread-safe usando double-checked locking.
     *
     * @return La instancia única de BattleRepository
     */
    public static BattleRepository getInstance() {
        if (instance == null) {
            synchronized (BattleRepository.class) {
                if (instance == null) {
                    instance = new BattleRepository();
                }
            }
        }
        return instance;
    }

    public void save(String id, Battle battle) {
        battles.put(id, battle);
    }

    public Battle findById(String id) {
        return battles.get(id);
    }

    public void remove(String id) {
        battles.remove(id);
    }

    /**
     * Limpia todas las batallas (útil para testing)
     */
    public void clear() {
        battles.clear();
    }

    /**
     * Obtiene el número de batallas activas
     */
    public int size() {
        return battles.size();
    }
}
