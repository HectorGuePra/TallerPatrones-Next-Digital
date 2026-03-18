package com.taller.patrones.application.command;

/**
 * Contrato para acciones de batalla que se pueden ejecutar y deshacer.
 */
public interface BattleCommand {
    void execute();
    void undo();
}

