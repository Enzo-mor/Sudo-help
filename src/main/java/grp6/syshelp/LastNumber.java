package grp6.syshelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

public class LastNumber implements InterfaceTech {
    
    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());
        
        ArrayList<Integer> liste = new ArrayList<>();
        

        for(int i=0; i<Grid.NB_NUM; i++){
            for(int j=0;j<Grid.NB_NUM;j++){
                if(grille.getCell(i, j).isEmpty()){
                    
                    List<Cell> col_cells=grille.fullCell(Grid.Shape.COLUMN,j);
                    List<Cell> lin_cells=grille.fullCell(Grid.Shape.LINE,i);
                    List<Cell> car_cells=grille.fullCell(Grid.Shape.SQUARE,i);
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
                        return aide;
                    }
        
                    
                }
            }
        }

        return aide;

    }
}
