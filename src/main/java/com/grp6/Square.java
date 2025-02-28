package com.grp6;

public class Square implements SousGrille  {
    

    private Cell[][] carre;


    /**
     * Constructeur de la classe line
     * @param i
     * @param grille
     */
    public Square(int i, int j, Grid grille){
        this.carre= grille.getSubGrid(i*3, j*3);
    }

    /**
     * Permet de savoir si la ligne est complète
     * @return true si la ligne est complète, false sinon [boolean]
     * @param une ligne de cellule [Cell[]] 
     */
    public boolean complete(){
        return true;
    }

    /**
     * Permet de savoir combien de cellule vide reste-t-il
     * @return Le nombre de cellules vides restantes [Cell[]]
     * @param une ligne de cellule [Cell[]] 
     */
    public Cell[] emptyCell(){
        return null;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [Cell[]]
     * @param une ligne de cellule [Cell[]] 
     */
    public Cell[] fullCell(){
        return null;
    }

    /**
     * Permet de savoir la position des cellules vides
     * @return La position des cellules vides [int[][]]
     * @param une ligne de cellule [Cell[]] 
     */
    public int[][] emptyCellPos(){
        return null;
    }

    /**
     * Permet de savoir la position des cellules pleines
     * @return La position des cellules pleines [int[][]]
     * @param une ligne de cellule [Cell[]] 
     */
    public int[][] fullCellPos(){
        return null;
    }

    public String toString(){
        String s ="";
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++){
                s += carre[i][j].getNumber();
                if(j == 2)
                    s+="\n";
            }
        return s;
    }

    public static void main(String[] args) {
        int[] data={2,5,9,4,7,3,6,1,8,6,4,1,8,2,9,3,5,7,3,7,8,1,5,6,9,2,4,5,2,4,7,9,8,1,3,6,9,3,7,5,6,1,4,8,2,8,1,6,3,4,2,5,7,9,4,6,3,2,1,7,8,9,5,7,8,5,9,3,4,2,6,1,1,9,2,6,8,5,7,4,3};
        Grid g =new Grid(data);
        System.out.println(g.toString());
        Square hooo= new Square(0, 0,g);
        Square hooo1= new Square(1, 2,g);

        System.out.println(hooo.toString());
        System.out.println(hooo1.toString());
    }

}
