package com.example;

import com.google.gson.JsonObject;

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
     * @return String retourne la chaine representant le type  d'action 
     */
    public String actionType(); 
    /**
     * cette methode permet de serialiser une action sous forme de Json
     * 
     * @return  un String sous forme de json
     */
    public String jsonEncode();

    /***
     * cette methode retourne le Json object utilie pour la serialisation
     * @return JsonObject
     */
    public JsonObject serialise(); 
     
    
}
