package com.example;

/**
 * Inteface  permettant de mettre à jour le temps 
 * passé dans le jeux jeu
 */
public interface GameTimeListener {

    /**
     * methode permettant de mettre à jour le temps passé dans le jeu
     * @param elapsedTime represente le temps passé dans le jeu
     */
    void onTimeUpdated(String elapsedTime);
}
