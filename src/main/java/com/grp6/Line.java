package com.grp6;

public class Line extends SousGrille  {

    private Cell[] ligne;


    /**
     * Constructeur de la classe line
     * @param i
     * @param grille
     */
    public line(int i , Grid grille){
        if(!isValidIndex(i)) {
            throw new IllegalArgumentException("Indice de ligne invalide: " + i);
        }

        Cell[] line = new Cell[NB_NUM];
        for(int j=0; j<NB_NUM; j++) {
            line[j] = this.getCell(i, j);
        }
        this.ligne = line;
    }

    /**
     * Permet de savoir si la ligne est complète
     * @return true si la ligne est complète, false sinon [boolean]
     * @param une ligne de cellule [Cell[]] 
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
     * @param une ligne de cellule [Cell[]] 
     */
    public cell[] emptyCell(){
        cell[] cellVide = new cell[9];
        for(int i = 0; i < 9; i++){
            if(ligne[i].getValue().isEmpty()){
                cellVide[i] = ligne[i];
            }
        }
        return cellVide;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [cell[]]
     * @param une ligne de cellule [Cell[]] 
     */
    public cell[] fullCell(){
        cell[] cellPlein = new cell[9];
        for(int i = 0; i < 9; i++){
            if(!ligne[i].getValue().isEmpty()){
                cellPlein[i] = ligne[i];
            }
        }
        return cellPlein;
    }


    /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]]
     * @param une ligne de cellule [Cell[]] 
     */
    public int[][] emptyCellPos(){
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(ligne[i].getValue().isEmpty()){
                pos[i] = ligne[i].getPosition();
            }
        }
        return pos;
    }

    /**
     * Permet de savoir la position des cellules pleines
     * @return La position des cellules pleines [int[][]]
     * @param une ligne de cellule [Cell[]] 
     */
    public int[][] fullCellPos(){
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(!ligne[i].getValue().isEmpty()){
                pos[i] = ligne[i].getPosition();
            }
        }
        return pos;
    }
    
}
