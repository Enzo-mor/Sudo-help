package grp6.syshelp;

import java.util.ArrayList;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

/**
 * Classe fournissant une aide pour resoudre un Sudoku en utilisant la technique LastPossible.
 * Cette methode detecte si une case ne peut contenir qu'un seul numero possible.
 * @see InterfaceTech
 * @author ROQUAIN Louison
 * @author GRAMMONT Dylan
 */
public class LastPossible implements InterfaceTech {

    /**
     * Constructeur de la classe LastPossible
     */
    public LastPossible(){}
    
    /**
     * Analyse la grille pour determiner si un coup peut etre joue en utilisant la technique LastPossible.
     * @param grid La grille de Sudoku a analyser
     * @return Une aide indiquant le coup possible, ou null si aucun coup n'est trouve
     */
    public Help getHelp(Grid grid) {
        Help help = new Help(getClass().getSimpleName());
        
        // Parcours des blocs 3x3 de la grille
        for(int i = 0; i < 9; i += 3) {
            for(int j = 0; j < 9; j += 3) {
                
                ArrayList<Integer> present = new ArrayList<>();
                
                // Identification des nombres presents dans le carre 3x3
                for(int r = i; r < i + 3; r++) {
                    for(int c = j; c < j + 3; c++) {
                        if(!grid.getCell(r, c).isEmpty()) {
                            present.add(grid.getCell(r, c).getNumber());
                        }
                    }
                }
                
                // Ajout des nombres presents dans la ligne et la colonne correspondantes
                Cell[] ligne = grid.getLine(i);
                Cell[] col = grid.getColumn(j);
                for(int y = 0; y < grid.NB_NUM; y++) {
                    if(!present.contains(ligne[y].getNumber())) {
                        present.add(ligne[y].getNumber());
                    }
                    if(!present.contains(col[y].getNumber())) {
                        present.add(col[y].getNumber());
                    }                
                }
                
                // Identification du numero manquant
                int count = 0;
                int numManquant = 0;
                for(int num = 1; num <= 9; num++) {
                    if(!present.contains(num)) {
                        count++;
                        numManquant = num;
                    }
                }
                
                // Si un seul numero est manquant, alors on propose ce coup
                if(count == 1) {
                    int idx = (i / 3) * 3 + (j / 3);
                    help.addSquare(idx);
                    help.setMessage(1, "Tu peux placer un " + numManquant + ". Regarde bien tes lignes et colonnes !");
                    help.setMessage(3, "LastPossible peut etre utilise ici.");
                    return help;
                }
            }
        }
        return null;
    }

    /**
     * Verifie si un chiffre peut etre place dans une case sans violer les regles du Sudoku.
     * @param grid La grille de Sudoku
     * @param row La ligne de la case a verifier
     * @param col La colonne de la case a verifier
     * @param num Le chiffre a verifier
     * @return true si le chiffre peut etre place, false sinon
     */
    private boolean isValid(Grid grid, int row, int col, int num) {
        // Verification de la ligne
        for(int c = 0; c < 9; c++) {
            if(grid.getCell(row, c).getNumber() == num) {
                return false;
            }
        }
        // Verification de la colonne
        for(int r = 0; r < 9; r++) {
            if(grid.getCell(r, col).getNumber() == num) {
                return false;
            }
        }
        // Verification du carre 3x3
        int i = (row / 3) * 3;
        int j = (col / 3) * 3;
        for(int r = i; r < i + 3; r++) {
            for(int c = j; c < j + 3; c++) {
                if(grid.getCell(r, c).getNumber() == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Methode principale pour tester l'algorithme LastPossible sur une grille de Sudoku.
     * 
     * @param args Arguments de la ligne de commande (non utilises).
     */
    public static void main(String[] args) {
        int[] dataCarre = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 8, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            5, 4, 3, 0, 8, 0, 0, 0, 0,
            0, 6, 2, 0, 0, 0, 0, 0, 0,
            9, 1, 7, 0, 0, 0, 0, 0, 0
        };

        Grid grilleCarre = new Grid(dataCarre);
        AutoAnnotation.generate(grilleCarre);
        System.out.println(grilleCarre.toString());
        System.out.println(grilleCarre.getCell(7, 0).getAnnotations());
        LastPossible v = new LastPossible();
        System.out.println(v.getHelp(grilleCarre));
    }
}
