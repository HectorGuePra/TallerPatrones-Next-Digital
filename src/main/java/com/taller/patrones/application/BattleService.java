package com.taller.patrones.application;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;
import com.taller.patrones.infrastructure.adapter.ExternalBattleDataAdapter;
import com.taller.patrones.infrastructure.combat.AttackFactory;
import com.taller.patrones.infrastructure.combat.AttackFactoryProvider;
import com.taller.patrones.infrastructure.combat.CombatEngine;
import com.taller.patrones.infrastructure.persistence.BattleRepository;

import java.util.List;
import java.util.UUID;

/**
 * Caso de uso: gestionar batallas.
 * <p>
 * Usa el patrón Singleton para acceder al repositorio compartido.
 */
public class BattleService {

    private final CombatEngine combatEngine = new CombatEngine();
    // Usa la instancia única del repositorio (patrón Singleton)
    private final BattleRepository battleRepository = BattleRepository.getInstance();

    public static final List<String> PLAYER_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL", "ICE_BEAM", "POISON_STING", "THUNDER","METEORO");
    public static final List<String> ENEMY_ATTACKS = List.of("TACKLE", "SLASH", "FIREBALL");

    public BattleStartResult startBattle(String playerName, String enemyName) {
        // Usando el patrón Builder: código más legible y mantenible
        Character player = new Character.Builder(playerName != null ? playerName : "Héroe")
                .maxHp(150)
                .attack(25)
                .defense(15)
                .speed(20)
                .characterClass("WARRIOR")
                .equipment("Espada Básica")
                .build();

        Character enemy = new Character.Builder(enemyName != null ? enemyName : "Dragón")
                .maxHp(120)
                .attack(30)
                .defense(10)
                .speed(15)
                .characterClass("MONSTER")
                .equipment("Garras Afiladas")
                .build();

        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);

        return new BattleStartResult(battleId, battle);
    }

    public Battle getBattle(String battleId) {
        return battleRepository.findById(battleId);
    }

    public void executePlayerAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || !battle.isPlayerTurn()) return;

        AttackFactory factory= AttackFactoryProvider.getFactory(attackName);
        Attack attack = combatEngine.createAttack(factory);

        int damage = combatEngine.calculateDamage(battle.getPlayer(), battle.getEnemy(), attack);
        applyDamage(battle, battle.getPlayer(), battle.getEnemy(), damage, attack);
    }

    public void executeEnemyAttack(String battleId, String attackName) {
        Battle battle = battleRepository.findById(battleId);
        if (battle == null || battle.isFinished() || battle.isPlayerTurn()) return;

        AttackFactory factory= AttackFactoryProvider.getFactory(attackName != null ? attackName : "GOLPE");
        Attack attack = combatEngine.createAttack(factory);

        int damage = combatEngine.calculateDamage(battle.getEnemy(), battle.getPlayer(), attack);
        applyDamage(battle, battle.getEnemy(), battle.getPlayer(), damage, attack);
    }

    private void applyDamage(Battle battle, Character attacker, Character defender, int damage, Attack attack) {
        defender.takeDamage(damage);
        String target = defender == battle.getPlayer() ? "player" : "enemy";
        battle.setLastDamage(damage, target);
        battle.log(attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de daño a " + defender.getName());
        battle.switchTurn();
        if (!defender.isAlive()) {
            battle.finish(attacker.getName());
        }
    }

    public BattleStartResult startBattleFromExternal(String fighter1Name, int fighter1Hp, int fighter1Atk,
                                                     String fighter2Name, int fighter2Hp, int fighter2Atk) {
        // Builder hace más clara la conversión de datos externos a nuestro dominio
        Character player = new Character.Builder(fighter1Name)
                .maxHp(fighter1Hp)
                .attack(fighter1Atk)
                .defense(10)
                .speed(10)
                .characterClass("EXTERNAL")
                .build();

        Character enemy = new Character.Builder(fighter2Name)
                .maxHp(fighter2Hp)
                .attack(fighter2Atk)
                .defense(10)
                .speed(10)
                .characterClass("EXTERNAL")
                .build();

        Battle battle = new Battle(player, enemy);
        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);
        return new BattleStartResult(battleId, battle);
    }

    /**
     * Inicia una batalla desde datos externos usando el patrón Adapter.
     * El adapter se encarga de convertir el formato externo a nuestro dominio.
     *
     * @param adapter Adapter que convierte datos externos a Character
     * @return Resultado con battleId y Battle
     */
    public BattleStartResult startBattleFromAdapter(ExternalBattleDataAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter no puede ser null");
        }

        // El adapter se encarga de toda la conversión - código limpio y desacoplado
        Character player = adapter.getPlayer();
        Character enemy = adapter.getEnemy();

        Battle battle = new Battle(player, enemy);
        battle.log("Batalla iniciada desde " + adapter.getProviderName());

        String battleId = UUID.randomUUID().toString();
        battleRepository.save(battleId, battle);

        return new BattleStartResult(battleId, battle);
    }

    public record BattleStartResult(String battleId, Battle battle) {}
}
