package com.grp6;
import java.util.ArrayList;
import java.util.List;


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
    public List<Cell> emptyCell(){
        List<Cell> cellVide = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            if(colonne[i].isEmpty()){
                cellVide.add(colonne[i]);
            }
        }
        return cellVide;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public List<Cell> fullCell(){
        List<Cell> cellPlein = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            if(!colonne[i].isEmpty()){
                cellPlein.add(colonne[i]);
            }
        }
        return cellPlein;
    }


    /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]] le premier tableau corespond aux coordonnées x/y et le deuxieme tableau corespond aux nombre de cellules
     * @param void 
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
        
    public String toString(){
          String s ="";
          for(int i=0;i<9;i++){
              s+= colonne[i].getNumber();
              s+= "\n"; 
          }
          return s;
      }
    
     public static void main(String[] args) {
         int[] data={2,5,9,4,7,3,6,1,8,6,4,1,8,2,9,3,5,7,3,7,8,1,5,6,9,2,4,5,2,4,7,9,8,1,3,6,9,3,7,5,6,1,4,8,2,8,1,6,3,4,2,5,7,9,4,6,3,2,1,7,8,9,5,7,8,5,9,3,4,2,6,1,1,9,2,6,8,5,7,4,3};
         Grid g =new Grid(data);
         System.out.println(g.toString());
         Column colonne1= new Column(1,g);
         Column colonne2= new Column(2,g);

         System.out.println(colonne1.toString());
         System.out.println(colonne2.toString());
     }
}