package grp6.syshelp;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe représente la technique NakedSingleton.
 * 
 * Elle permet de trouver les singletons nus(1 chiffre apparaissant uniquement dans une case d'une unité(ligne colones carré)). 
 * @author MOREAU Enzo
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class NakedSingleton implements InterfaceTech {

    /**
     * Constructeur de la classe NakedSingleton
     */
    public NakedSingleton(){}

    /**
     * Analyse la grille pour determiner si un coup peut etre joue en utilisant la technique NakedSingleton.
     * @param grille La grille de Sudoku a analyser
     * @return Une aide indiquant le coup possible, ou null si aucun coup n'est trouve
     */
    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());
        for (Cell cell:grille) {
            System.out.println("Coo : "+cell.getPosition()[0]+" "+cell.getPosition()[1]);
            // Vérifie les colonnes
            if (cell.isEmpty() && cell.OnlyOneAnnotation() && cell.isEditable()) {

                int[] pos = cell.getPosition();
                System.out.println(pos[0]+" "+pos[1]);
                aide.addPos(pos[0], pos[1]);
                int rand = (int) (3*Math.random());
                int x =cell.getPosition()[0];
                int y = cell.getPosition()[1];
                switch (rand) {
                    case 0 ->{
                        //aide.addColumn(x);
                    }
                    case 1 ->{
                        //aide.addLine(y);
                    }
                    default->{
                        //aide.addSquare(x+grille.NB_NUM*y);
                    }
                        
                }
                System.out.println("x : "+x+" y "+y );
                aide.addPos(x, y);
                aide.setMessage(1, "Remplis tes annotations");
                
                aide.setMessage(3, "cherche les "+takeTheAnnotation(cell));
                return aide;
            }

        }

        return null; // Aucun singleton nu trouvé
    }

    /**
     * Permet de recuperer l'annotation de la cellule
     * @param c
     * @return
     */
    private int takeTheAnnotation(Cell c){
        for(int i=0;i<9;i++)if(c.getAnnotationsBool()[i])return i+1;
        return 0;
    }
}
