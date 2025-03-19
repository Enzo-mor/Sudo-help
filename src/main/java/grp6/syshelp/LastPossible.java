package grp6.syshelp;
import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class LastPossible implements InterfaceTech{
    private static int SOLO = 1;
    
    
    public Help getHelp(Grid grille){
        for(int j=0; j<Grid.NB_NUM; j++){
            int[] num =grille.numToPosForSubGrid(j);
            Cell[] car_cells=grille.getFlatSubGrid(num[0],num[1]);
            List<Cell> cellules = new ArrayList<>();
            
            for(int i= 0;i<9;i++) cellules.add(car_cells[i]);

            
            //On récupère toute les annotations de chaque cellule
            List<boolean[]> annoteSolo =
                cellules.parallelStream()
                    .map(Cell::getAnnotationsBool)
                    .collect(toList());
            
            //On compte le nombre d'occurrences qu'une annotation est présente dans le subGrid
            int[] occurrences= new int[9];

            annoteSolo.stream()
                .forEach(b -> IntStream.range(0, b.length)
                    .filter(ind -> b[ind])
                    .forEach(ind -> occurrences[ind]++)
                );
            
            for(int i=0;i<Grid.NB_NUM;i++)
                if(occurrences[i]==SOLO){
                    Help h =new Help(this.getClass().getSimpleName());
                    h.addSquare(j%3,j/3);

                    aide.setMessage(1,"Fait attention au "+i);
                    aide.setMessage(2,"Fait attention aux Carré (3*3) "+leCarre(j));
                    aide.setMessage(3,"Regarde LA"+);
                    return h;
                }
        }
        return null;
    }


    /**
     * Retourne la phrase correspondant à la zone de 3×3 cellules.
     * @param indCarre la position du carré : [[1,2,3], [4,5,6], [7,8,9]]
     * @return La phrase correspondante de la zone 3×3.
     */
    private String leCarre(int indCarre) {
        switch (indCarre) {
            case 1: return " en haut à gauche";
            case 2: return " en haut au centre";
            case 3: return " en haut à droite";
            case 4: return " au milieu à gauche";
            case 5: return " au milieu au centre";
            case 6: return " au milieu à droite";
            case 7: return " en bas à gauche";
            case 8: return " en bas au centre";
            case 9: return " en bas à droite";
            default: return "Indice invalide, doit être entre 1 et 9";
        }
    }

    public static void main(String[] args) {
        int[] dataCarre={0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,8,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,8,0,0,0,0,
                         0,6,0,0,5,0,0,0,0,
                         9,1,0,0,0,0,0,0,0};
        Grid grilleCarre =new Grid(dataCarre);
        AutoAnnotation.generate(grilleCarre);
        System.out.println(grilleCarre.toString());

        LastPossible v = new LastPossible();
        System.out.println(v.getHelp(grilleCarre));
    }

}
