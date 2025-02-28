package com.grp6;

public class LastCell implements InterfaceTech  {
    @Override
    public boolean detect(Grid grille) {

        for(int i = 0; i < Grid.NB_NUM;i++){
                Column colonne = new Column(i,grille);
                Line ligne = new Line(i,grille);
                Square carre = new Square(i/3,i%3,grille);
                if(!colonne.complete() || !ligne.complete() || !carre.complete()){ 
                    if (colonne.emptyCell().size() == 1 || ligne.emptyCell().size() == 1 || carre.emptyCell().size() == 1){
                        return true;
                }
            }
        }
        return false;   
    }
    @Override
    public void applique(Grid grille){
        
    }
    public static void main(String[] args){
         //grille  (pour les collones )
        int[] data={
        2,5,9,4,7,3,6,1,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Grid grille = new Grid(data);
        System.out.println(grille.toString());
        LastCell lastCell = new LastCell();
        System.out.println(lastCell.detect(grille));

         //grille  (pour les lignes )
        int[] dataLigne={
            2,0,0,0,0,0,0,0,0,
            1,0,0,0,0,0,0,0,0,
            3,0,0,0,0,0,0,0,0,
            4,0,0,0,0,0,0,0,0,
            5,0,0,0,0,0,0,0,0,
            6,0,0,0,0,0,0,0,0,
            7,0,0,0,0,0,0,0,0,
            8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Grid grilleLigne = new Grid(dataLigne);
        System.out.println(grilleLigne.toString());
        LastCell LastLigne = new LastCell();
        System.out.println(LastLigne.detect(grilleLigne));

        //grille  (pour les carrés )
        int[] dataCarre={2,5,9,0,0,0,0,0,0,6,4,0,0,0,9,3,5,7,3,7,8,0,0,0,9,2,4,5,2,4,0,9,8,1,3,0,9,3,0,5,0,1,4,8,2,8,1,6,3,4,2,5,0,9,4,6,3,2,1,7,8,9,5,7,8,5,9,3,4,2,6,1,1,9,2,6,8,5,7,4,3};
        Grid grilleCarre =new Grid(dataCarre);
        System.out.println(grilleCarre.toString());

        LastCell LastCarre = new LastCell();
        System.out.println(LastCarre.detect(grilleCarre));




        


    }
}
