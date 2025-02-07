package com.grp6;

public class LastNumber implements InterfaceTech  {

    public boolean detect(Grid grille,int i,int j){
        int listInt[];
        if(grille.getCell(i, j).isEmpty()){
            Column colonne = new Column(j, grille);
            Line ligne = new Line(i, grille);
            
            Cell[] cell_col = colonne.fullCell();
            Cell[] cell_lig = ligne.fullCell();
            for(int z=1;z<=Grid.NB_NUM;z++){
                if(cell_col[i].getNumber() == z || cell_lig[j].getNumber() == z ){
                    listInt[z]=z;
                }
            }
            if (listInt.length == Grid.NB_NUM -1) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] data={2,4,6,0,0,0,0,0,0,0,0,0,3,0,6,0,7,4,3,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0};
        Grid g =new Grid(data);
        LastNumber test =new LastNumber();
        System.out.println(g.toString());
        System.out.println(test.detect(g,0,1));


    }

}
