package grp6.syshelp;

import grp6.sudocore.*;

public class LastCell implements InterfaceTech  {
    @Override
    public boolean detect(Grid grille) {

        for(int i = 0; i < Grid.NB_NUM;i++){
            if(grille.numberOfFullCell(Grid.Shape.COLUMN,i) == 8 || grille.numberOfFullCell(Grid.Shape.LINE,i) == 8 || grille.numberOfFullCell(Grid.Shape.SQUARE,i) == 8){
                return true;
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
        2,5,9,4,7,3,0,1,0,
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
            8,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0};
        Grid grilleLigne = new Grid(dataLigne);
        System.out.println(grilleLigne.toString());
        LastCell LastLigne = new LastCell();
        System.out.println(LastLigne.detect(grilleLigne));

        //grille  (pour les carrés )
        int[] dataCarre={
            1,6,0,0,0,0,0,0,0,
            2,5,8,0,0,0,0,0,0,
            3,4,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0};Grid grilleCarre =new Grid(dataCarre);
        System.out.println(grilleCarre.toString());

        LastCell LastCarre = new LastCell();
        System.out.println(LastCarre.detect(grilleCarre));

    }
}
