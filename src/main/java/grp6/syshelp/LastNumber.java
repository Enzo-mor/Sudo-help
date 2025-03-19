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


        AutoAnnotation.generate(grille);
        for(int i=0; i<Grid.NB_NUM; i++){
            for(int j=0; j<Grid.NB_NUM; j++){      
                if(grille.getCell(i,j).getAnnotations().size()==1){
                    aide.addPos(i,j);
                    aide.addLine(i);

                    aide.setMessage(1,"Fait attention au "+grille.getCell(i,j).getAnnotations().get(0));
                    aide.setMessage(2,"Fait attention aux ligne "+i);
                    aide.setMessage(3,"Regarde la cellule colonne : "+ j+" Bissous" );
                    return aide;
                }
            }
        }

        return null;

    }

    

     /**
     * Trouve le nombre manquant dans une listeColl de Cell.
     * @param cells listeColl des cellules (ligne, colonne ou carré)
     * @return Le number qui doit aller dans la cellule vide.
     */
    private int findLastNumber(Cell[] cells){
        List<Integer> list = new ArrayList<>(Collections.nCopies(10, 0));
        for (Cell cell : cells){
            int c = cell.getNumber();
            list.set(cell.getNumber(),list.get(c)+1);
        }
        for(int i=0;i<10;i++){
            if(list.get(i) == 0)return i;
        }
        return 0;
    }

    int[] data = {
        2,4,6,0,0,0,0,0,0,
        0,0,0,3,0,6,0,7,4,
        3,7,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,
        1,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,
        8,0,0,0,0,0,0,0,0,
        9,0,0,0,0,0,0,0,0
    };
}
