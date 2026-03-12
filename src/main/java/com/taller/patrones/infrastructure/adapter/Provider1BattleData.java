package com.taller.patrones.infrastructure.adapter;

/**
 * DTO para datos externos del Proveedor 1.
 * Formato: fighter1_name, fighter1_hp, fighter1_atk, fighter2_name, fighter2_hp, fighter2_atk
 */
public class Provider1BattleData {
    private String fighter1_name;
    private int fighter1_hp;
    private int fighter1_atk;
    private int fighter1_def;
    private int fighter1_spd;

    private String fighter2_name;
    private int fighter2_hp;
    private int fighter2_atk;
    private int fighter2_def;
    private int fighter2_spd;

    // Constructors
    public Provider1BattleData() {}

    public Provider1BattleData(String fighter1_name, int fighter1_hp, int fighter1_atk, int fighter1_def, int fighter1_spd,
                                String fighter2_name, int fighter2_hp, int fighter2_atk, int fighter2_def, int fighter2_spd) {
        this.fighter1_name = fighter1_name;
        this.fighter1_hp = fighter1_hp;
        this.fighter1_atk = fighter1_atk;
        this.fighter1_def = fighter1_def;
        this.fighter1_spd = fighter1_spd;
        this.fighter2_name = fighter2_name;
        this.fighter2_hp = fighter2_hp;
        this.fighter2_atk = fighter2_atk;
        this.fighter2_def = fighter2_def;
        this.fighter2_spd = fighter2_spd;
    }

    // Getters and Setters
    public String getFighter1_name() { return fighter1_name; }
    public void setFighter1_name(String fighter1_name) { this.fighter1_name = fighter1_name; }

    public int getFighter1_hp() { return fighter1_hp; }
    public void setFighter1_hp(int fighter1_hp) { this.fighter1_hp = fighter1_hp; }

    public int getFighter1_atk() { return fighter1_atk; }
    public void setFighter1_atk(int fighter1_atk) { this.fighter1_atk = fighter1_atk; }

    public int getFighter1_def() { return fighter1_def; }
    public void setFighter1_def(int fighter1_def) { this.fighter1_def = fighter1_def; }

    public int getFighter1_spd() { return fighter1_spd; }
    public void setFighter1_spd(int fighter1_spd) { this.fighter1_spd = fighter1_spd; }

    public String getFighter2_name() { return fighter2_name; }
    public void setFighter2_name(String fighter2_name) { this.fighter2_name = fighter2_name; }

    public int getFighter2_hp() { return fighter2_hp; }
    public void setFighter2_hp(int fighter2_hp) { this.fighter2_hp = fighter2_hp; }

    public int getFighter2_atk() { return fighter2_atk; }
    public void setFighter2_atk(int fighter2_atk) { this.fighter2_atk = fighter2_atk; }

    public int getFighter2_def() { return fighter2_def; }
    public void setFighter2_def(int fighter2_def) { this.fighter2_def = fighter2_def; }

    public int getFighter2_spd() { return fighter2_spd; }
    public void setFighter2_spd(int fighter2_spd) { this.fighter2_spd = fighter2_spd; }
}

