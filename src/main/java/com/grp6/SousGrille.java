package com.grp6;
/* ====== Importation des libreries java ====== */
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;


public interface SousGrille  {
    
    /**
     * Permet de savoir si la sous-grille est complète
     * @return true si la colonne est complète, false sinon [boolean]
     */
    public boolean complete();

    /**
     * Permet d'obtenir un tableau des cellules vides
     * @return Le nombre de cellules vides restantes [cell[]]
     */
    public List<Cell> emptyCell();

    /**
     * Permet d'obtenir un tableau des cellules pleines  
     * @return Le nombre de cellules pleines restantes [cell[]]
     */
    public List<Cell> fullCell();


     /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]]
     */
    public int[][] emptyCellPos();


    /**
     * Permet de savoir la position des cellules pleines
     * @return La position des cellules pleines [int[][]]
     */
    public int[][] fullCellPos();

}
