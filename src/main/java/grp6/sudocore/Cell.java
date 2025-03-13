package grp6.sudocore;

import java.util.List;

/**
 * Cette interface permet de representer une cellule modifiable d'une grille
 * de Sudoku.
 * @author Kilian POUSSE
 * @version 1.1
 */
public interface Cell {
      /**
     * Recupérer le chiffre de la cellule
     * @return Entier représentant le chiffre (0 si vide) [int]
     */
    public int getNumber();

    /** 
     * Récupérer les annotations de la cellule
     * @return Liste des annotations 
     */
    public List<Integer> getAnnotations();


    public Integer getLastAnnotation();

     /**
     * Savoir si une celluce est vide ou non
     * @return 'true' si la cellule est vide, sinon 'false' [booleen]
     */

     
    /** 
     * Transforme la cellule en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    public String toString();

    
     /**
     * Savoir si une cellule est modifiable
     * @return Vrai si la cellule peut etre modifiée
     */
    public boolean isEditable();

  /**
   * cette methode verifie si une cellule est vide 
   * @return
   */
    public boolean isEmpty();   

    /**
     * Mettre un chiffre dans une cellule
     * @param number Chiffre à mettre dans la cellule [int]
     */
    public void setNumber(int number);

    /**
     * Ajouter un annotation à la cellule
     * @param number Chiffre de l'annotation à ajouter [int]
     */
    public void addAnnotation(int number);


    /**
     * Enlever une annotation d'une cellule
     * @param number Chiffre de l'annotation à retirer [int]
     */
    public void removeAnnotation(int number);

    /** 
     * Néttoyer la cellule (la vider)
     */
    public void clear();

    /**
     * Clone une cellule flexible
     * @return Une nouvelle instance de Cell (clone de la cellule)
     */
    public Cell clone();


   
    

   

     
}
