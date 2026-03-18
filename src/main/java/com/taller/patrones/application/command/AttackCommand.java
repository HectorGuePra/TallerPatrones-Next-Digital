package com.taller.patrones.application.command;

import com.taller.patrones.domain.Attack;
import com.taller.patrones.domain.Battle;
import com.taller.patrones.domain.Character;

/**
 * Comando concreto: ejecutar un ataque y permitir deshacerlo.
 */
public class AttackCommand implements BattleCommand {

    private final Battle battle;
    private final Character attacker;
    private final Character defender;
    private final Attack attack;
    private final int damage;

    private final int defenderHpBefore;
    private final String turnBefore;
    private final boolean finishedBefore;
    private final int lastDamageBefore;
    private final String lastDamageTargetBefore;
    private final int logSizeBefore;

    private boolean executed;

    public AttackCommand(Battle battle, Character attacker, Character defender, Attack attack, int damage) {
        this.battle = battle;
        this.attacker = attacker;
        this.defender = defender;
        this.attack = attack;
        this.damage = damage;

        this.defenderHpBefore = defender.getCurrentHp();
        this.turnBefore = battle.getCurrentTurn();
        this.finishedBefore = battle.isFinished();
        this.lastDamageBefore = battle.getLastDamage();
        this.lastDamageTargetBefore = battle.getLastDamageTarget();
        this.logSizeBefore = battle.getBattleLog().size();
        this.executed = false;
    }

    @Override
    public void execute() {
        if (executed) {
            return;
        }

        defender.takeDamage(damage);
        String target = defender == battle.getPlayer() ? "player" : "enemy";
        battle.setLastDamage(damage, target);
        battle.log(attacker.getName() + " usa " + attack.getName() + " y hace " + damage + " de da\u00f1o a " + defender.getName());

        battle.switchTurn();
        if (!defender.isAlive()) {
            battle.finish(attacker.getName());
        }

        executed = true;
    }

    @Override
    public void undo() {
        if (!executed) {
            return;
        }

        defender.restoreHp(defenderHpBefore);
        battle.restoreState(turnBefore, finishedBefore, lastDamageBefore, lastDamageTargetBefore);
        battle.truncateLog(logSizeBefore);

        executed = false;
    }
}

