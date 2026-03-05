package com.taller.patrones.infrastructure.combat;

import java.util.Map;
import java.util.HashMap;

public class AttackFactoryProvider {
    private static final Map<String, AttackFactory> factories = new HashMap<>();

    static {
        // Registramos las fábricas disponibles
        factories.put("TACKLE", new TackleFactory());
        factories.put("SLASH", new SlashFactory());
        factories.put("FIREBALL", new FireballFactory());
        factories.put("ICE_BEAM", new IceBeamFactory());
        factories.put("THUNDER", new ThunderFactory());
        factories.put("POISON_STING", new PoisonStingFactory());
        factories.put("METEORO", new MeteoroFactory());
    }

    public static AttackFactory getFactory(String name) {
        String n = name != null ? name.toUpperCase() : "";
        // Si no existe el ataque, devolvemos la fábrica del golpe básico
        return factories.getOrDefault(n, new GolpeFactory());
    }
}