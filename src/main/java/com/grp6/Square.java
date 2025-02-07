package com.grp6;

public class Square implements SousGrille  {
    

    private Cell[] carre;
    /**
     * Permet de savoir si la sous-grille est complète
     * @return true si la colonne est complète, false sinon [boolean]
     * @param une colonne de cellule [Cell[]] 
     */
    public boolean complete(){
        return false;
    }

    /**
     * Permet d'obtenir un tableau des cellules vides
     * @return Le nombre de cellules vides restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public Cell[] emptyCell(){
        return carre;
    }

    /**
     * Permet d'obtenir un tableau des cellules pleines  
     * @return Le nombre de cellules pleines restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public Cell[] fullCell(){
        return carre;
    }

     /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]]
     * @param une colonne de cellule [Cell[]] 
     */
    public int[][] emptyCellPos(){
        int[][] pos = new int[9][9];
        return pos;
    }


    /**
     * Permet de savoir la position des cellules pleines
     * @return La position des cellules pleines [int[][]]
     * @param une colonne de cellule [Cell[]] 
     */
    public int[][] fullCellPos(){
        int[][] pos = new int[9][9];
        return pos;
    }

    

}
