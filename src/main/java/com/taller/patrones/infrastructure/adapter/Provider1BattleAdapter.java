package com.taller.patrones.infrastructure.adapter;

import com.taller.patrones.domain.Character;

/**
 * Adapter para el Proveedor 1.
 * Convierte el formato: fighter1_name, fighter1_hp, fighter1_atk, etc.
 * al modelo de dominio Character.
 */
public class Provider1BattleAdapter implements ExternalBattleDataAdapter {

    private final Provider1BattleData data;

    public Provider1BattleAdapter(Provider1BattleData data) {
        if (data == null) {
            throw new IllegalArgumentException("Provider1BattleData no puede ser null");
        }
        this.data = data;
    }

    @Override
    public Character getPlayer() {
        return new Character.Builder(
                data.getFighter1_name() != null ? data.getFighter1_name() : "Fighter 1"
        )
                .maxHp(data.getFighter1_hp() > 0 ? data.getFighter1_hp() : 100)
                .attack(data.getFighter1_atk() > 0 ? data.getFighter1_atk() : 10)
                .defense(data.getFighter1_def() > 0 ? data.getFighter1_def() : 10)
                .speed(data.getFighter1_spd() > 0 ? data.getFighter1_spd() : 10)
                .characterClass("EXTERNAL_PROVIDER_1")
                .build();
    }

    @Override
    public Character getEnemy() {
        return new Character.Builder(
                data.getFighter2_name() != null ? data.getFighter2_name() : "Fighter 2"
        )
                .maxHp(data.getFighter2_hp() > 0 ? data.getFighter2_hp() : 100)
                .attack(data.getFighter2_atk() > 0 ? data.getFighter2_atk() : 10)
                .defense(data.getFighter2_def() > 0 ? data.getFighter2_def() : 10)
                .speed(data.getFighter2_spd() > 0 ? data.getFighter2_spd() : 10)
                .characterClass("EXTERNAL_PROVIDER_1")
                .build();
    }

    @Override
    public String getProviderName() {
        return "Provider1 (fighter1_*, fighter2_*)";
    }
}

