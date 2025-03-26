package grp6.syshelp;

import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de HiddenTriple.
 * @see InterfaceTech
 * @see Help
 * @see Cell
 * @see Grid
 * @see HiddenPairs
 * @author Taïse De Thèse
 * @version 1.0     
 * @since 2025-03-25
 */
public class HiddenTriples extends HiddenPairs {

    protected int getHiddenLength() {
        return 3;
    }

    /**
     * Permet de récupérer le nom de la technique
     * @return String le nom de la technique
     */
    public String getName() {
        return "Triplets cachés";
    }


    protected int[] getCandidate(Cell cell1,Cell cell2){
           List<Integer>candidates= cell1.getAnnotations();
           candidates.retainAll(cell2.getAnnotations());
           if(candidates.size()==3){
               return new int[]{candidates.get(0),candidates.get(1),candidates.get(2)};
           }
           return null;
    }
    /**
     * Permet de savoir si une cellule a des annotations cachées
     * @param cell la cellule à vérifier
     * @return boolean true si la cellule a des annotations cachées, false sinon
     */
    protected Boolean hasPossibleCellHidden(Cell cell){
        return cell.isEditable() && cell.getAnnotations().size() >= 3;
    }

    public static void main(String[] args) {
        int[] data = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        

        Grid grille = new Grid(data);
        System.out.println("Grille initiale :");
        grille.getCell(0, 0).addAnnotation(2);
        grille.getCell(0, 0).addAnnotation(4);
        grille.getCell(0, 0).addAnnotation(5);
        grille.getCell(0, 1).addAnnotation(2);
        
        grille.getCell(0, 1).addAnnotation(4);
        grille.getCell(0, 1).addAnnotation(5);

        grille.getCell(0, 2).addAnnotation(2);
        grille.getCell(0, 2).addAnnotation(4);
        grille.getCell(0, 2).addAnnotation(5);
        grille.getCell(0, 3).addAnnotation(8);
        //grille.getCell(0, 2).addAnnotation(4);

       // grille.printAnnotationsGrid();

       
        System.out.println("\n");

        // Création d'une instance de la technique des paires pointantes
        HiddenTriples triple = new HiddenTriples();

        // Détection
        System.out.println("triple pointantes  détectées ? " + triple.getHelp(grille));
      

    }
}
