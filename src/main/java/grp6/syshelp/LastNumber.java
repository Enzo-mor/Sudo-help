package grp6.syshelp;


import grp6.sudocore.Grid;


/**
 * Cette classe represente la technique du dernier chiffre possible.
 * Elle permet de savoir si dans une ligne, une colonne ou un carré on peut placer un chiffre.
 * @author DUBOIS Gabriel
 * @see InterfaceTech Contenant les méthodes des techniques
 */
public class LastNumber implements InterfaceTech {
    
    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());


        AutoAnnotation.generate(grille);
        for(int i=0; i<Grid.NB_NUM; i++){
            for(int j=0; j<Grid.NB_NUM; j++){      
                if(grille.getCell(i,j).getAnnotations().size()==1){
                    aide.addPos(i,j);
                    //aide.addLine(i);
                    //aide.addColumn(j);
                    //aide.addSquare(j/3);

                    aide.setMessage(1,"Tu peux placer un "+grille.getCell(i,j).getAnnotations().get(0));
                    
                    aide.setMessage(3,"regarde ici ");
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
    /*private int findLastNumber(Cell[] cells){
        List<Integer> list = new ArrayList<>(Collections.nCopies(10, 0));
        for (Cell cell : cells){
            int c = cell.getNumber();
            list.set(cell.getNumber(),list.get(c)+1);
        }
        for(int i=0;i<10;i++){
            if(list.get(i) == 0)return i;
        }
        return 0;
    }*/

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
