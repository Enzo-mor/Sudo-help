package com.example;

public interface Action {
    /**
     *  methode permettant faire l' action
     */
    public void doAction();

    /**
     *  methode permettant d'annuler l' action
     */
    public void undoAction();

    /**
     *  methode permettant de retourner le type action
     * @return String retourne la chaine represantant le type  d'action 
     */
    public String actionType();
}
