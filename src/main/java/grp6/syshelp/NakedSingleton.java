package grp6.syshelp;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe représente la technique NakedSingleton.
 * 
 * Elle permet de trouver les singletons nus(1 chiffre apparaissant uniquement dans une case d'une unité(ligne colones carré)). 
 * @author 
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class NakedSingleton implements InterfaceTech {

    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        for (int i = 0; i < Grid.NB_NUM; i++) {
            // Vérifie les colonnes
            for (Cell cell : grille.getColumn(i)) {
                if (cell.isEmpty() && cell.OnlyOneAnnotation()) {
                    //int[] pos = cell.getPosition();
                  //  aide.addPos(pos[0], pos[1]);
                    aide.addColumn(i);
                    aide.setMessage(1, "Remplis tes annotations");
                    
                    aide.setMessage(3, "cherche les "+takeTheAnnotation(cell));
                    return aide;
                }
            }

            // Vérifie les lignes
            for (Cell cell : grille.getLine(i)) {
                if (cell.isEmpty() && cell.OnlyOneAnnotation()) {
                    //int[] pos = cell.getPosition();
                   // aide.addPos(pos[0], pos[1]);
                    aide.addLine(i);
                    aide.setMessage(1, "Remplis tes annotations");
                    
                    aide.setMessage(3, "cherche les "+takeTheAnnotation(cell));
                    return aide;
                }
            }

            // Vérifie les carrés
            int[] temp = grille.numToPosForSubGrid(i);
            for (Cell cell : grille.getFlatSubGrid(temp[0], temp[1])) {
                if (cell.isEmpty() && cell.OnlyOneAnnotation()) {
                   // int[] pos = cell.getPosition();
                    //aide.addPos(pos[0], pos[1]);
                    aide.addSquare(i);
                    aide.setMessage(1, "Remplis tes annotations");
                    
                    aide.setMessage(3, "cherche les "+takeTheAnnotation(cell));
                    return aide;
                }
            }
        }

        return null; // Aucun singleton nu trouvé
    }

    /**
     * Permet de récupérer l'annotation de la cellule
     * @param c
     * @return
     */
    private int takeTheAnnotation(Cell c){
        for(int i=0;i<9;i++)if(c.getAnnotationsBool()[i])return i+1;
        return 0;
    }
}
