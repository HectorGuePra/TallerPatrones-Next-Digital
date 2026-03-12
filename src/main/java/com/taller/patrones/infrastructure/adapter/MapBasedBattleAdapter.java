package com.taller.patrones.infrastructure.adapter;

import com.taller.patrones.domain.Character;

import java.util.Map;

/**
 * Adapter genérico para formato Map<String, Object>.
 * Detecta automáticamente qué formato es y adapta en consecuencia.
 *
 * Esta clase facilita la transición desde el código legacy que usaba Map directamente.
 */
public class MapBasedBattleAdapter implements ExternalBattleDataAdapter {

    private final Map<String, Object> data;
    private final ExternalBattleDataAdapter delegate;

    public MapBasedBattleAdapter(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data map no puede ser null");
        }
        this.data = data;
        this.delegate = detectAndCreateAdapter(data);
    }

    /**
     * Detecta el formato del Map y crea el adapter apropiado
     */
    private ExternalBattleDataAdapter detectAndCreateAdapter(Map<String, Object> data) {
        // Detectar Provider 1: tiene fighter1_name, fighter1_hp, etc.
        if (data.containsKey("fighter1_name") || data.containsKey("fighter1_hp")) {
            return createProvider1Adapter(data);
        }

        // Detectar Provider 2: tiene player y enemy como objetos
        if (data.containsKey("player") && data.get("player") instanceof Map) {
            return createProvider2Adapter(data);
        }

        // Default: asumir Provider 1 con valores por defecto
        return createProvider1Adapter(data);
    }

    /**
     * Crea un adapter del Provider 1 desde un Map
     */
    private ExternalBattleDataAdapter createProvider1Adapter(Map<String, Object> data) {
        Provider1BattleData battleData = new Provider1BattleData();

        battleData.setFighter1_name((String) data.getOrDefault("fighter1_name", "Héroe"));
        battleData.setFighter1_hp(getIntValue(data, "fighter1_hp", 150));
        battleData.setFighter1_atk(getIntValue(data, "fighter1_atk", 25));
        battleData.setFighter1_def(getIntValue(data, "fighter1_def", 10));
        battleData.setFighter1_spd(getIntValue(data, "fighter1_spd", 10));

        battleData.setFighter2_name((String) data.getOrDefault("fighter2_name", "Dragón"));
        battleData.setFighter2_hp(getIntValue(data, "fighter2_hp", 120));
        battleData.setFighter2_atk(getIntValue(data, "fighter2_atk", 30));
        battleData.setFighter2_def(getIntValue(data, "fighter2_def", 10));
        battleData.setFighter2_spd(getIntValue(data, "fighter2_spd", 10));

        return new Provider1BattleAdapter(battleData);
    }

    /**
     * Crea un adapter del Provider 2 desde un Map
     */
    @SuppressWarnings("unchecked")
    private ExternalBattleDataAdapter createProvider2Adapter(Map<String, Object> data) {
        Provider2BattleData battleData = new Provider2BattleData();

        Map<String, Object> playerMap = (Map<String, Object>) data.get("player");
        if (playerMap != null) {
            Provider2BattleData.FighterData player = new Provider2BattleData.FighterData();
            player.setName((String) playerMap.getOrDefault("name", "Player"));
            player.setHealth(getIntValue(playerMap, "health", 100));
            player.setAttack(getIntValue(playerMap, "attack", 20));
            player.setDefense(getIntValue(playerMap, "defense", 10));
            player.setSpeed(getIntValue(playerMap, "speed", 10));
            battleData.setPlayer(player);
        }

        Map<String, Object> enemyMap = (Map<String, Object>) data.get("enemy");
        if (enemyMap != null) {
            Provider2BattleData.FighterData enemy = new Provider2BattleData.FighterData();
            enemy.setName((String) enemyMap.getOrDefault("name", "Enemy"));
            enemy.setHealth(getIntValue(enemyMap, "health", 100));
            enemy.setAttack(getIntValue(enemyMap, "attack", 20));
            enemy.setDefense(getIntValue(enemyMap, "defense", 10));
            enemy.setSpeed(getIntValue(enemyMap, "speed", 10));
            battleData.setEnemy(enemy);
        }

        return new Provider2BattleAdapter(battleData);
    }

    /**
     * Helper para extraer valores int del Map de forma segura
     */
    private int getIntValue(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    @Override
    public Character getPlayer() {
        return delegate.getPlayer();
    }

    @Override
    public Character getEnemy() {
        return delegate.getEnemy();
    }

    @Override
    public String getProviderName() {
        return delegate.getProviderName() + " (via MapBasedAdapter)";
    }
}

