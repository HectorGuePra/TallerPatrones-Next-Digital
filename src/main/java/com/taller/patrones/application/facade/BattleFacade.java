package com.taller.patrones.application.facade;

import com.taller.patrones.application.BattleService;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.infrastructure.adapter.ExternalBattleDataAdapter;
import com.taller.patrones.infrastructure.observer.DamageObserver;

import java.util.List;

/**
 * Facade para exponer operaciones simples del subsistema de combate.
 */
public class BattleFacade {

    private final BattleService battleService = new BattleService();

    public void addDamageObserver(DamageObserver observer) {
        battleService.addDamageObserver(observer);
    }

    public BattleService.BattleStartResult startBattle(String playerName, String enemyName) {
        return battleService.startBattle(playerName, enemyName);
    }

    public BattleService.BattleStartResult startBattleFromAdapter(ExternalBattleDataAdapter adapter) {
        return battleService.startBattleFromAdapter(adapter);
    }

    public Battle getBattle(String battleId) {
        return battleService.getBattle(battleId);
    }

    public void executeAttack(String battleId, String attackName) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null || battle.isFinished()) {
            return;
        }

        if (battle.isPlayerTurn()) {
            battleService.executePlayerAttack(battleId, attackName);
        } else {
            battleService.executeEnemyAttack(battleId, attackName);
        }
    }

    public void executeEnemyTurn(String battleId) {
        Battle battle = battleService.getBattle(battleId);
        if (battle == null || battle.isPlayerTurn() || battle.isFinished()) {
            return;
        }

        List<String> enemyAttacks = BattleService.ENEMY_ATTACKS;
        String attack = enemyAttacks.get((int) (Math.random() * enemyAttacks.size()));
        battleService.executeEnemyAttack(battleId, attack);
    }

    public boolean undoLastAttack(String battleId) {
        return battleService.undoLastAttack(battleId);
    }
}

