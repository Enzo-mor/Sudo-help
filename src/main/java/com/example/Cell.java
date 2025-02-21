package com.example;

import java.util.List;

/**
 * Cette interface permet de modéliser une cellule d'une grille
 * de Sudoku.
 * @author Kilian POUSSE
 * @version 1.1
 */
public interface Cell extends ReadOnlyCell {

   

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
