package com.grp6;

public class Column  extends SousGrille{
    
    private Cell[] colonne;


    /**
     * Constructeur de la classe Column
     * @param i
     * @param grille
     */
    public column(int j , Grid grille){
        if(!isValidIndex(j)) {
            throw new IllegalArgumentException("Indice de colonne invalide: " + j);
        }

        Cell[] column = new Cell[NB_NUM];
        for(int i=0; i<NB_NUM; i++) {
            column[i] = this.getCell(i, j);
        }
        this.colonne = column;
    }

    /**
     * Permet de savoir si la colonne est complète
     * @return true si la colonne est complète, false sinon [boolean]
     * @param une colonne de cellule [Cell[]] 
     */
    public boolean complete(){
        for(int i = 0; i < 9; i++){
            if(this.cell[i].getValue().isEmpty()){
                return false;
            }
        }
    }

    /**
     * Permet de savoir combien de cellule vide reste-t-il
     * @return Le nombre de cellules vides restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public cell[] emptyCell(){
        cell[] cellVide = new cell[9];
        for(int i = 0; i < 9; i++){
            if(colonne[i].getValue().isEmpty()){
                cellVide[i] = colonne[i];
            }
        }
        return cellVide;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public cell[] fullCell(){
        cell[] cellPlein = new cell[9];
        for(int i = 0; i < 9; i++){
            if(!colonne[i].getValue().isEmpty()){
                cellPlein[i] = colonne[i];
            }
        }
        return cellPlein;
    }


    /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]]
     * @param une colonne de cellule [Cell[]] 
     */
    public int[][] emptyCellPos(){
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(colonne[i].getValue().isEmpty()){
                pos[i] = colonne[i].getPosition();
            }
        }
        return pos;
    }

    /**
     * Permet de savoir la position des cellules pleines
     * @return La position des cellules pleines [int[][]]
     * @param une colonne de cellule [Cell[]] 
     */
    public int[][] fullCellPos(){
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(!colonne[i].getValue().isEmpty()){
                pos[i] = colonne[i].getPosition();
            }
        }
        return pos;
    }
}
