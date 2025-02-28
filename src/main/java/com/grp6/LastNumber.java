package com.grp6;
import java.util.ArrayList;
import java.util.List;

public class LastNumber implements InterfaceTech  {

    @Override
    public boolean detect(Grid grille){
        ArrayList<Integer> liste = new ArrayList<>();
        

        for(int i=0; i<9; i++){
            for(int j=0;j<9;j++){
                if(grille.getCell(i, j).isEmpty()){
                    Column colonne = new Column(j, grille);
                    Line ligne = new Line(i, grille);
                    Square carre = new Square(i/3, j%3, grille);
                    
                    List<Cell> col_cells=colonne.fullCell();
                    List<Cell> lin_cells=ligne.fullCell();
                    List<Cell> car_cells=carre.fullCell();
                    for(Cell c:col_cells){
                        if(!liste.contains(c.getNumber()) ){
                            liste.add(c.getNumber());
                        }
                    }
                    for(Cell c:lin_cells){
                        if(!liste.contains(c.getNumber()) ){
                            liste.add(c.getNumber());
                        }
                    }
                    for(Cell c:car_cells){
                        if(!liste.contains(c.getNumber()) ){
                            liste.add(c.getNumber());
                        }
                    }
        
                    if(liste.size() == Grid.NB_NUM-1){
                        return true;
                    }
        
                    
                }
            }
        }
        
        return false;
    }

    @Override
    public void applique(Grid grilleJoueur){
        
    }

    
    public static void main(String[] args) {
        int[] data={2,4,6,0,0,0,0,0,0,0,0,0,3,0,6,0,7,4,3,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0};
        Grid g =new Grid(data);
        LastNumber test =new LastNumber();
        System.out.println(g.toString());
        System.out.println(test.detect(g));


    }

}
