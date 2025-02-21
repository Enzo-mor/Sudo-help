package com.example;

/**
 * Inteface permettant de mettre à jour le temps 
 * passé du jeu
 */
public interface GameTimeListener {
    void onTimeUpdated(String elapsedTime);
}
