package com.taller.patrones.infrastructure.combat;
import com.taller.patrones.domain.Character;


public interface DamageStrategy {
    int calculate(Character attacker, Character defender, int basePower);
}