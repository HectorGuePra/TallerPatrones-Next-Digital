package com.taller.patrones.infrastructure.combat;
import com.taller.patrones.domain.Character;


public class CriticalDamageStrategy implements DamageStrategy {
    @Override
    public int calculate(Character attacker, Character defender, int basePower) {
        int raw = (attacker.getAttack() * basePower) / 100;
        int damage = Math.max(1, raw - defender.getDefense());

        // 20% de probabilidad (0.2)
        if (Math.random() < 0.20) {
            System.out.println("¡CRÍTICO!");
            return (int) (damage * 1.5);
        }
        return damage;
    }
}
