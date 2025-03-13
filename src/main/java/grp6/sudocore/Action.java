package grp6.sudocore;

import com.google.gson.JsonObject;
/**
 * cette classe permet d'effectuer les diffrentes actions ou interactions  avec le jeu de sudoku
 * toutes les actions du jeu heritent de cette classe et les actions appliquées sur les cellules du jeu ne concerneront que les cellules flexibles ou editable.
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

    public int getRow(){
        return -1;
    }
  
    public int getColumn(){
      return -1;
    }

    public int getOldNumber(){
        return -1;
    }

    public int getRedoNumber(){
        return -1;
    }

    public int getNumber(){
        return -1;
    }
     
    
}
 