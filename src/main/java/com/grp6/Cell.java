package com.grp6;

/**
 * Cette interface permet de modéliser une cellule d'une grille
 * de Sudoku.
 */
public interface Cell {

    /**
     * Recupérer le chiffre de la cellule
     * @return Entier représentant le chiffre (0 si vide) [int]
     */
    public int getNumber();

    /**
     * Mettre un chiffre dans une cellule
     * @param number Chiffre à mettre dans la cellule [int]
     */
    public void setNumber(int number);

    /**
     * Ajouter un annotation à la cellule
     * @param number ChiffregetValuen(int number);

    /** 
     * Récupérer les annotations de la cellule
     * @return tableau des présences des annotations [booleen[]]
     */
    public boolean[] getAnnotations();

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
     * Savoir si une celluce est vide ou non
     * @return 'true' si la cellule est vide, sinon 'false' [booleen]
     */
    public boolean isEmpty();

    /** 
     * Transforme la cellule en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    public String toString();
}
