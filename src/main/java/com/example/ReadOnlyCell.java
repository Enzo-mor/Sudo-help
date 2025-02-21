package com.example;

import java.util.List;

/**
 * cette Interface qui permet l'accès aux cellules en lecture seule
 * @author Taise de Thèse
 * @version 1.0
 */
public interface ReadOnlyCell {

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

    
}
