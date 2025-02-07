package com.grp6;

public class Line implements SousGrille  {

    private Cell[] ligne;


    /**
     * Constructeur de la classe line
     * @param i
     * @param grille
     */
    public Line(int i , Grid grille){
        if(!Grid.isValidIndex(i)) {
            throw new IllegalArgumentException("Indice de ligne invalide: " + i);
        }

        Cell[] line = new Cell[Grid.NB_NUM];
        for(int j=0; j<Grid.NB_NUM; j++) {
            line[j] = grille.getCell(i, j);
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
            if(this.ligne[i].isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Permet de savoir combien de cellule vide reste-t-il
     * @return Le nombre de cellules vides restantes [Cell[]]
     * @param une ligne de cellule [Cell[]] 
     */
    public Cell[] emptyCell(){
        Cell[] cellVide = new Cell[9];
        for(int i = 0; i < 9; i++){
            if(ligne[i].isEmpty()){
                cellVide[i] = ligne[i];
            }
        }
        return cellVide;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [Cell[]]
     * @param une ligne de cellule [Cell[]] 
     */
    public Cell[] fullCell(){
        Cell[] cellPlein = new Cell[9];
        for(int i = 0; i < 9; i++){
            if(!ligne[i].isEmpty()){
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
            if(ligne[i].isEmpty()){
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
            if(!ligne[i].isEmpty()){
                pos[i] = ligne[i].getPosition();
            }
        }
        return pos;
    }
    
}
