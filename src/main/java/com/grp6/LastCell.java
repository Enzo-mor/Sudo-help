package com.grp6;

public class LastCell implements InterfaceTech  {
 
    public boolean detect(Grid grille) {

        for(int i = 0; i < Grid.NB_NUM;i++){
                Column colonne = new Column(i,grille);
                Line ligne = new Line(i,grille);
                if(!colonne.complete()){
                    if (colonne.emptyCell().length == 1 || ligne.emptyCell().length == 1){
                        return true;
                }
            }
        }
        return false;   
    }

    public static void main(String[] args){
        int[] data={
        2,5,9,4,7,3,6,1,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Grid grille = new Grid(data);
        System.out.println(grille.toString());
        LastCell lastCell = new LastCell();
        System.out.println(lastCell.detect(grille));


        


    }
}
