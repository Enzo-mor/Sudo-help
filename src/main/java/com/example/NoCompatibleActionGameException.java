package com.example;

/**
 * Exception levée lorsqu'une action n'est pas conforme au jeu
 * en cours. par exemple si l'instance du jeu encours n'est pas la meme que celle de l'action
 * @see Action
 * @see Game
 * @author Taise de Thèse
 * @version 1.0
 */
public class NoCompatibleActionGameException extends RuntimeException {
    public NoCompatibleActionGameException(String message) {
        super(message);
    }
    
}
