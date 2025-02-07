package com.grp6;

public class Column  implements SousGrille{
    
    private Cell[] colonne;


    /**
     * Constructeur de la classe Column
     * @param i
     * @param grille
     */
    public Column(int j , Grid grille){
        if(!Grid.isValidIndex(j)) {
            throw new IllegalArgumentException("Indice de colonne invalide: " + j);
        }

        Cell[] column = new Cell[Grid.NB_NUM];
        for(int i=0; i<Grid.NB_NUM; i++) {
            column[i] = grille.getCell(i, j);
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
            if(this.colonne[i].isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * Permet de savoir combien de cellule vide reste-t-il
     * @return Le nombre de cellules vides restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public Cell[] emptyCell(){
        Cell[] cellVide = new Cell[9];
        for(int i = 0; i < 9; i++){
            if(colonne[i].isEmpty()){
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
    public Cell[] fullCell(){
        Cell[] cellPlein = new Cell[9];
        for(int i = 0; i < 9; i++){
            if(!colonne[i].isEmpty()){
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
        int j = 0;
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(colonne[i].isEmpty()){

                pos[i][j] = colonne[i].getPosition()[0];
                pos[i][j] = colonne[i].getPosition()[1];
                j++;
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
        int j =0;
        int[][] pos = new int[9][9];
        for(int i = 0; i < 9; i++){
            if(!colonne[i].isEmpty()){
                pos[i][j] = colonne[i].getPosition()[0];
                pos[i][j] = colonne[i].getPosition()[1];
                j++;
            }
            }
            return pos;
        }
    }