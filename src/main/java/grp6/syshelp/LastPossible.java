package grp6.syshelp;

import grp6.sudocore.*;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class LastPossible implements InterfaceTech{
    private static int SOLO = 1;
    
    @Override
    public boolean detect(Grid grille){
        for(int j=0; j<9; j++){
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
                    .filter(i -> b[i])
                    .forEach(i -> occurrences[i]++)
                );
            
            for(Integer i: occurrences)
                if(i==SOLO) return true;
        }
        return false;
    }

    public void applique(Grid grilleJoueur){

    }

    public static void main(String[] args) {
        int[] dataCarre={0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,8,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,8,0,0,0,0,
                         0,6,0,0,0,0,0,0,0,
                         9,1,0,0,0,0,0,0,0};
        Grid grilleCarre =new Grid(dataCarre);
        AutoAnnotation.generate(grilleCarre);
        System.out.println(grilleCarre.toString());

        LastPossible v = new LastPossible();
        System.out.println(v.detect(grilleCarre));
    }

}
