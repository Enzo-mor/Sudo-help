package grp6.syshelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/***
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de PointingPairs.
 * @author Taïse De Thèse
 * @version 1.0
 * @since 2025-03-19
 */
public class PointingPairs implements InterfaceTech{
    /**
     * cette methode permet de retourner les positions du candidat(l' annotation) dans une sous grille.
     * @param subgrid : la sous grille dans laquelle on cherche les candidats
     * @param candidate : le candidat qu'on cherche
     * @return les positions du candidat dans la sous grille
     */
    protected List<int[]> getCandidatePositions(Cell[][] subgrid, int candidate) {
    List<int[]> positions = new ArrayList<>();
    for (int i = 0; i < Grid.NB_SUBGRID; i++) {
        for (int j = 0; j < Grid.NB_SUBGRID; j++) {
            if (subgrid[i][j].isEditable()&&subgrid[i][j].getAnnotationsBool()[candidate - 1]) {
                positions.add(new int[]{i, j});
            }
        }
    }
         return positions;
    }
     /**
      * cette methode permet de verifier si la line ou colonne contient un nombre de candidat requis pour appliquer la technique
      * @param candidate : le candidat qu'on cherche
      * @param LineCell : la ligne ou colonne dans laquelle on cherche le candidat
      * @return true si la ligne ou colonne contient le nombre de candidat requis, false sinon
      */
    protected Boolean isValidNumberCandidate(int candidate,Cell[]LineCell){
      return Arrays.asList(LineCell).stream().filter(cell->cell.isEditable()&&cell.getAnnotationsBool()[candidate-1]).count()>2;

    }


    /**
     * cette methode permet de verifier si une sous grille contient un pointing pair par rappor à un candidat.
     * @param subgrid : la sous grille dans laquelle on cherche le pointing pair
     * @param candidate : le candidat qu'on cherche
     * @return true si la sous grille contient un pointing pair, false sinon
     */
    protected boolean hasPointing(Cell[][] subgrid, int candidate) {
        List<int[]> positions = getCandidatePositions(subgrid, candidate);
        if (positions.size() == 2) {
            int row0 = positions.get(0)[0];
            int row1 = positions.get(1)[0];
            int col0 = positions.get(0)[1];
            int col1 = positions.get(1)[1];
            return (row0 == row1) || (col0 == col1);
        }
        return false;
    }

     /**
      * cette methode permet de retourner le nom de la technique
      * @return
      */ 
    public String getName(){
        return "Pairs pointantes";
    }
    
    @Override
    public Help getHelp(Grid grille){
        Help help = new Help(getClass().getSimpleName());

        for (int i = 0; i < Grid.NB_SUBGRID; i++) 
        for (int j = 0; j < Grid.NB_SUBGRID; j++) {
            Cell[][] subgrid = grille.getSubGrid(i*Grid.NB_SUBGRID, Grid.NB_SUBGRID*j);
                 int startRow =i * Grid.NB_SUBGRID;
                int startCol = j * Grid.NB_SUBGRID;
            for (int annotation = 1; annotation <= Grid.NB_NUM; annotation++) {
                // Si l'annotation est aussi double dans la sous-grille
                
                if (hasPointing(subgrid,annotation)) {
                    List<int[]> positions = getCandidatePositions(subgrid, annotation);
                    int row0 = positions.get(0)[0];
                    int row1 = positions.get(1)[0];
                    int col0 = positions.get(0)[1];
                    int col1 = positions.get(1)[1];

                    int globalRow = startRow + row0;
                    help.addSquare(i, j);
                    help.setMessage(1,"veuillez faire attention aux annotations  "+annotation+" \ndans la sous grille"); 

                   // Cas 1 : les deux cellules sont sur la même ligne dans le bloc
                   if (row0 == row1&&isValidNumberCandidate(annotation, grille.getLine(globalRow))) {
                       help.addLine(globalRow);
                       help.setMessage(2,"veuillez faire attention aux annotations "+annotation+" \nsur la ligne "+globalRow+1);
                       help.setMessage(3,"veuillez  appliquer la technique "+this.getName() +"\n sur la ligne "+globalRow+1);
                       return help;
                   } 
                    

                else if(col0 == col1&&isValidNumberCandidate(annotation, grille.getColumn(startCol+col0))){
                    int globalCol = startCol + col0;
                    help.addColumn(globalCol);
                    help.setMessage(2,"veuillez faire attention aux annotations "+annotation+"\n sur la colonne "+(globalCol+1));
                    help.setMessage(3,"veuillez  appliquer la technique "+this.getName()+" \nsur la colonne "+(globalCol+1));
                    return help;



                }
            }
            }
            }
     
            return null;

   
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
        grille.getCell(2, 0).addAnnotation(2);
        grille.getCell(3, 0).addAnnotation(2);
        grille.getCell(1, 0).addAnnotation(2);
        grille.getCell(4, 0).addAnnotation(2);
        grille.printAnnotationsGrid();

       
        System.out.println("\n");

        // Création d'une instance de la technique des paires pointantes
        PointingPairs pointingTriple = new PointingTriples();

        // Détection
        System.out.println("triple pointantes  détectées ? " + pointingTriple.getHelp(grille));

       
     }
}
