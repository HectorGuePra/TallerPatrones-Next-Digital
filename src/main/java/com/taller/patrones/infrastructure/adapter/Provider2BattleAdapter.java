package com.taller.patrones.infrastructure.adapter;

import com.taller.patrones.domain.Character;

/**
 * Adapter para el Proveedor 2.
 * Convierte el formato: {player: {name, health, attack}, enemy: {name, health, attack}}
 * al modelo de dominio Character.
 */
public class Provider2BattleAdapter implements ExternalBattleDataAdapter {

    private final Provider2BattleData data;

    public Provider2BattleAdapter(Provider2BattleData data) {
        if (data == null) {
            throw new IllegalArgumentException("Provider2BattleData no puede ser null");
        }
        this.data = data;
    }

    @Override
    public Character getPlayer() {
        Provider2BattleData.FighterData playerData = data.getPlayer();
        if (playerData == null) {
            throw new IllegalArgumentException("Player data no puede ser null");
        }

        return new Character.Builder(
                playerData.getName() != null ? playerData.getName() : "Player"
        )
                .maxHp(playerData.getHealth() > 0 ? playerData.getHealth() : 100)
                .attack(playerData.getAttack() > 0 ? playerData.getAttack() : 10)
                .defense(playerData.getDefense() > 0 ? playerData.getDefense() : 10)
                .speed(playerData.getSpeed() > 0 ? playerData.getSpeed() : 10)
                .characterClass("EXTERNAL_PROVIDER_2")
                .build();
    }

    @Override
    public Character getEnemy() {
        Provider2BattleData.FighterData enemyData = data.getEnemy();
        if (enemyData == null) {
            throw new IllegalArgumentException("Enemy data no puede ser null");
        }

        return new Character.Builder(
                enemyData.getName() != null ? enemyData.getName() : "Enemy"
        )
                .maxHp(enemyData.getHealth() > 0 ? enemyData.getHealth() : 100)
                .attack(enemyData.getAttack() > 0 ? enemyData.getAttack() : 10)
                .defense(enemyData.getDefense() > 0 ? enemyData.getDefense() : 10)
                .speed(enemyData.getSpeed() > 0 ? enemyData.getSpeed() : 10)
                .characterClass("EXTERNAL_PROVIDER_2")
                .build();
    }

    @Override
    public String getProviderName() {
        return "Provider2 (player.health, enemy.health)";
    }
}

