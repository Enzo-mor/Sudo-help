package com.example;

import com.google.gson.JsonObject;
/**
 * cette classe permet d'effectuer les diffrentes actions ou interactions  avec le jeu de sudoku
 * elle permet de faire l'action et de l'annuler
 * elle permet aussi de serialiser et deserialiser les actions
 * 
 * @version 1.0
 * @see Game
 * @see ActionManagerApply
 */
public  abstract class Action {
    /**
     * represente le jeu sur lequel les actions seront appliquées
     */
    final protected Game game;
    /**
     * constructeur des actions
     * @param game represente le jeu sur lequel les actions seront appliquées
     */
    public Action(Game game){
        this.game = game;
    }
    /**
     *  methode permettant faire l' action
     */
     protected abstract void doAction();

    /**
     *  methode permettant d'annuler l' action
     */
    protected abstract void undoAction();

    /**
     *  methode permettant de retourner le type action
     * @return String retourne le type  enumeré  de l'action 
     */
    public abstract SudoTypes.ActionType actionType(); 
    /**
     * cette methode permet de serialiser une action sous forme de Json
     * 
     * @return  un String sous forme de json
     */
    public  abstract String jsonEncode();

    /***
     * cette methode retourne le Json object utilie pour la serialisation
     * @return JsonObject
     */
    public  abstract JsonObject serialise(); 

    /**
     * cette methode permet de retourner une chaine representant l'action
     * @return
     */
    public abstract String toString();
     
    
}
 