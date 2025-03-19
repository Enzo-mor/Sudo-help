package grp6.syshelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

public class LastNumber implements InterfaceTech {
    
    @Override
    public Help getHelp(Grid grille) {
        Help aideColl = new Help(getClass().getSimpleName());
        Help aideLine = new Help(getClass().getSimpleName());
        Help aideSquare = new Help(getClass().getSimpleName());

        
        ArrayList<Integer> listeColl = new ArrayList<>();
        ArrayList<Integer> listeLine = new ArrayList<>();
        ArrayList<Integer> listeSquare = new ArrayList<>();

        int[]  d;

        for(int i=0; i<Grid.NB_NUM; i++){
            for(int j=0;j<Grid.NB_NUM;j++){
                if(grille.getCell(i, j).isEmpty()){
                    
                    List<Cell> col_cells=grille.fullCell(Grid.Shape.COLUMN,j);
                    List<Cell> lin_cells=grille.fullCell(Grid.Shape.LINE,i);
                    List<Cell> car_cells=grille.fullCell(Grid.Shape.SQUARE,i);

                    for(Cell c:col_cells){

                
                        if(!listeColl.contains(c.getNumber()) ){
                            listeColl.add(c.getNumber());

                            d = c.getPosition();
                            aideColl.addPos(d[0], d[1]);
                            aideColl.addColumn(i);
                            aideColl.setMessage(1, "Fait attention au"+findLastNumber(grille.getColumn(i)));
                            aideColl.setMessage(2, "Fait attention aux colonnes");
                            aideColl.setMessage(3, "Regarde ici");

                        }

                    }

                    for(Cell c:lin_cells){

                        if(!listeLine.contains(c.getNumber()) ){
                            listeLine.add(c.getNumber());

                            d = c.getPosition();
                            aideLine.addPos(d[0], d[1]);
                            aideLine.addLine(i);
                            aideLine.setMessage(1, "Fait attention au"+findLastNumber(grille.getLine(i)));
                            aideLine.setMessage(2, "Fait attention aux lignes");
                            aideLine.setMessage(3, "Regarde ici");
                        }
                    }

                    for(Cell c:car_cells){

                        if(!listeSquare.contains(c.getNumber()) ){
                            listeSquare.add(c.getNumber());
                            int[] temp = grille.numToPosForSubGrid(i);
                            d = c.getPosition();
                            aideSquare.addPos(d[0], d[1]);
                            aideSquare.addSquare(i);
                            aideSquare.setMessage(1, "Fait attention au"+grille.getFlatSubGrid(temp[0],temp[1]));
                            aideSquare.setMessage(2, "Fait attention aux carrés");
                            aideSquare.setMessage(3, "Regarde ici");

                        }
                    }
        
                    if(listeColl.size() == Grid.NB_NUM-1){
                        return aideColl;
                    }
                    if (listeLine.size() == Grid.NB_NUM-1){
                        return aideLine;
                    }
                    if (listeSquare.size() == Grid.NB_NUM-1){
                        return aideSquare;
                    }
                    
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
