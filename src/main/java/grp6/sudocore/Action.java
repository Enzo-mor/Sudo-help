package grp6.sudocore;

import com.google.gson.JsonObject;

/**
 * Cette classe permet d'effectuer les differentes actions ou interactions avec le jeu de sudoku.
 * Toutes les actions du jeu heritent de cette classe et les actions appliquees sur les cellules du jeu ne concerneront que les cellules flexibles ou editables.
 * Elle permet de faire l'action et de l'annuler ainsi que de serialiser et deserialiser les actions pour le stockage dans la base de donnees.
 * 
 * @author DE THESE Taise
 * @author POUSSE Kilian
 * @see Game
 * @see ActionManagerApply
 */
public abstract class Action {
    
    /**
     * Represente le jeu sur lequel les actions seront appliquees.
     */
    final protected Game game; 
    
    /**
     * Constructeur des actions.
     * 
     * @param game represente le jeu sur lequel les actions seront appliquees.
     */
    public Action(Game game){
        this.game = game;
    }

    /**
     * Methode permettant d'effectuer l'action sur la grille de Sudoku.
     * Cette methode sera implementee dans les sous-classes pour definir l'action a realiser.
     */
    protected abstract void doAction();

    /**
     * Methode permettant d'annuler l'action effectuee sur la grille de Sudoku.
     * Cette methode sera implementee dans les sous-classes pour definir l'annulation de l'action.
     */
    protected abstract void undoAction();

    /**
     * Methode permettant de retourner le type de l'action.
     * Cette methode retourne un type enumere representant l'action effectuee.
     * 
     * @return String retourne le type enumere de l'action.
     */
    public abstract SudoTypes.ActionType actionType(); 

    /**
     * Methode permettant de serialiser une action sous forme de JSON.
     * Cette methode est utilisee pour stocker les actions dans la base de donnees.
     * 
     * @return String representation JSON de l'action.
     */
    public abstract String jsonEncode();

    /***
     * Cette methode retourne le JsonObject utilise pour la serialisation.
     * Elle est utile pour la conversion de l'action en format JSON pour le stockage.
     * 
     * @return JsonObject contenant les donnees de l'action.
     */
    public abstract JsonObject serialise(); 

    /**
     * Cette methode permet de retourner une chaine de caracteres representant l'action.
     * La representation est utile pour afficher les actions sous forme lisible pour l'utilisateur.
     * 
     * @return Chaine de caracteres representant l'action.
     */
    @Override
    public abstract String toString();

    /**
     * Retourne la ligne affectee par l'action (si applicable).
     * Cette methode est utilisée dans les sous-classes pour retourner la ligne de l'action.
     * 
     * @return Entier representant la ligne de la cellule modifiee par l'action. Par defaut, -1.
     */
    public int getRow(){
        return -1;
    }

    /**
     * Retourne la colonne affectee par l'action (si applicable).
     * Cette methode est utilisée dans les sous-classes pour retourner la colonne de l'action.
     * 
     * @return Entier representant la colonne de la cellule modifiee par l'action. Par defaut, -1.
     */
    public int getColumn(){
      return -1;
    }

    /**
     * Retourne l'ancien numero (si applicable) avant l'action.
     * Cette methode est utilisée dans les sous-classes pour obtenir la valeur d'avant l'action.
     * 
     * @return Entier representant l'ancien numero. Par defaut, -1.
     */
    public int getOldNumber(){
        return -1;
    }

    /**
     * Retourne le numero applique apres l'action (si applicable).
     * Cette methode est utilisée dans les sous-classes pour obtenir la nouvelle valeur appliquee.
     * 
     * @return Entier representant le numero applique apres l'action. Par defaut, -1.
     */
    public int getRedoNumber(){
        return -1;
    }

    /**
     * Retourne le numero actuel applique (si applicable).
     * Cette methode est utilisée dans les sous-classes pour obtenir le numero actuel.
     * 
     * @return Entier representant le numero actuel. Par defaut, -1.
     */
    public int getNumber(){
        return -1;
    }

    /**
     * Retourne l'ancienne annotation (si applicable) avant l'action.
     * Cette methode est utilisée dans les sous-classes pour obtenir l'annotation d'avant l'action.
     * 
     * @return Entier representant l'ancienne annotation. Par defaut, -1.
     */
    public int getOldAnnotation() {
        return -1;
    }

    /**
     * Retourne l'annotation appliquee apres l'action (si applicable).
     * Cette methode est utilisée dans les sous-classes pour obtenir la nouvelle annotation appliquee.
     * 
     * @return Entier representant l'annotation appliquee apres l'action. Par defaut, -1.
     */
    public int getRedoAnnotation() {
        return -1;
    }

    /**
     * Retourne l'annotation actuelle appliquee (si applicable).
     * Cette methode est utilisée dans les sous-classes pour obtenir l'annotation actuelle.
     * 
     * @return Entier representant l'annotation actuelle. Par defaut, -1.
     */
    public int getAnnotation() {
        return -1;
    }

    /**
     * Cette methode permet de definir le numero applique a une cellule.
     * Elle est utilisée pour changer le numero dans certaines actions.
     * 
     * @param nb Numero a appliquer.
     */
    public void setNumber(int nb) {}
}
