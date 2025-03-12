package com.grp6;

import java.util.ArrayList;
import java.util.List;

/**
 * Technique : Paires pointantes (Pointing Pairs)
 * Objectif : Réduire le nombre d’annotations dans une ligne ou une colonne.
 * Détection :
 * - Si un chiffre donné **n’apparaît que dans une seule ligne ou une seule colonne d’un carré (bloc 3x3)**,
 *   alors ce chiffre **doit forcément être placé dans ce carré**.
 * - On peut donc **supprimer ce chiffre des annotations des autres cases de cette ligne/colonne en dehors du carré**.
 * 
 * Exemple :
 * - Si le chiffre 2 n'apparaît que exactement 2 fois dans une seule ligne d'un bloc 3x3, alors on peut supprimer les annotations 2 des autres cellules de cette ligne.
 * 
 * @author Taise De Thèse
 * @version 1.0
 * @since 2025-03-12
 */
public class PointingPairs implements InterfaceTech {

     

     private List<int[]> getCandidatePositions(Cell[][] subgrid, int candidate) {
    List<int[]> positions = new ArrayList<>();
    for (int i = 0; i < Grid.NB_SUBGRID; i++) {
        for (int j = 0; j < Grid.NB_SUBGRID; j++) {
            if (subgrid[i][j].isEditable()&&subgrid[i][j].getAnnotations()[candidate - 1]) {
                positions.add(new int[]{i, j});
            }
        }
    }
         return positions;
    }

        private boolean hasPointingPair(Cell[][] subgrid, int candidate) {
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
    

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection des paires pointantes
        for (int i = 0; i < Grid.NB_SUBGRID; i++) 
            for (int j = 0; j < Grid.NB_SUBGRID; j++) {
                Cell[][] subgrid = grille.getSubGrid(i, j);
                for (int annotation = 1; annotation <= Grid.NB_NUM; annotation++) 
                    // Si l'annotation est aussi double dans la sous-grille
                    if (hasPointingPair(subgrid,annotation)) {
                        return true;
                    }
                }
         


        return false;
    }

    @Override
    public void applique(Grid grille) {
        // Parcourir tous les blocs 3x3
        for (int blockRow = 0; blockRow < Grid.NB_SUBGRID; blockRow++) {
            for (int blockCol = 0; blockCol < Grid.NB_SUBGRID; blockCol++) {
                Cell[][] subgrid = grille.getSubGrid(blockRow*Grid.NB_SUBGRID,Grid.NB_SUBGRID*blockCol);
                int startRow = blockRow * Grid.NB_SUBGRID;
                int startCol = blockCol * Grid.NB_SUBGRID;
    
                // Pour chaque candidat de 1 à 9
                for (int candidate = 1; candidate <= Grid.NB_NUM; candidate++) {
                    List<int[]> positions = getCandidatePositions(subgrid, candidate);
                    if (positions.size() == 2) {
                        int row0 = positions.get(0)[0];
                        int row1 = positions.get(1)[0];
                        int col0 = positions.get(0)[1];
                        int col1 = positions.get(1)[1];
    
                        // Pour faciliter la vérification, construisons les coordonnées globales des cellules de la paire
                        int globalRow0 = startRow + row0;
                        int globalCol0 = startCol + col0;
                        int globalRow1 = startRow + row1;
                        int globalCol1 = startCol + col1;
    
                        // Cas 1 : les deux cellules sont sur la même ligne dans le bloc
                        if (row0 == row1) {
                            int globalRow = startRow + row0;
                            for (int c = 0; c < Grid.NB_NUM; c++) {
                                // On traite uniquement les cellules hors du bloc courant
                                if (c < startCol || c >= startCol + Grid.NB_SUBGRID) {
                                    // Si la cellule ne fait pas partie de la paire (vérification par rapport aux coordonnées globales)
                                    Cell cell = grille.getCell(globalRow, c);
                                    if (!((globalRow == globalRow0 && c == globalCol0) ||
                                          (globalRow == globalRow1 && c == globalCol1))) {
                                        if (cell.isEditable() && cell.getAnnotations()[candidate - 1]) {
                                            cell.removeAnnotation(candidate);
                                            System.out.println("Suppression de " + candidate +
                                                    " en (" + globalRow + "," + c + ") par paire pointante (ligne)");
                                        }
                                    }
                                }
                            }
                        }
                        // Cas 2 : les deux cellules sont sur la même colonne dans le bloc
                        if (col0 == col1) {
                            int globalCol = startCol + col0;
                            for (int r = 0; r < Grid.NB_NUM; r++) {
                                if (r < startRow || r >= startRow + Grid.NB_SUBGRID) {
                                    Cell cell = grille.getCell(r, globalCol);
                                    if (!((r == globalRow0 && globalCol == globalCol0) ||
                                          (r == globalRow1 && globalCol == globalCol1))) {
                                        if (cell.isEditable() && cell.getAnnotations()[candidate - 1]) {
                                            cell.removeAnnotation(candidate);
                                            System.out.println("Suppression de " + candidate +
                                                    " en (" + r + "," + globalCol + ") par paire pointante (colonne) ");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    




        public static void main(String[] args) {
            // Exemple de données pour la grille 9x9 :
            // Une valeur non nulle représente une cellule fixe (départ de la grille),
            // 0 représente une cellule vide (flexCell) qui sera remplie par le joueur et contiendra des annotations.
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
            grille.getCell(0, 1).addAnnotation(2);
            grille.getCell(0, 4).addAnnotation(2);
            grille.getCell(0, 5).addAnnotation(2);
            grille.printAnnotationsGrid();;
    
            // Vérifier les annotations d'une cellule flex (par exemple la cellule (0,2) qui est vide)
            Cell cellFlex = grille.getCell(0, 2);
            System.out.print("Annotations de la cellule (0,2) : ");
            for (int i = 0; i < 9; i++) {
                if (cellFlex.getAnnotations()[i]) {
                    System.out.print((i + 1) + " ");
                }
            }
            System.out.println("\n");
    
            // Création d'une instance de la technique des paires pointantes
            PointingPairs pointingPairs = new PointingPairs();
    
            // Détection
            boolean detectee = pointingPairs.detect(grille);
            System.out.println("Paires pointantes détectées ? " + detectee);
    
            // Application de la technique
            pointingPairs.applique(grille);
    
            System.out.println("\nGrille après application des paires pointantes :");
            grille.printAnnotationsGrid();;;
        }
    
}
