package grp6.sudocore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de resoudre des sudokus.
 * @author K. POUSSE
 * @version 1.0
 */
public final class SudokuSolver {

    /**
     * Resu la liste des cellules donnée en parametre et renvoie 
     * la solution sans modifier l'originale
     * @param cells Liste de cellules
     * @return Solution de la liste
     */
    public static List<Cell> solveCells(List<Cell> cells) {
        List<Cell> res = new ArrayList<>();
        // Dupplique la liste
        for(Cell c: cells) {
            res.add(c.clone());
        }

        solve(res);

        return res;
    }

    /**
     * Résu une liste de cellules (la la liste donnée sera modifiée)
     * @param cells Liste à résoudre
     * @return Si une solution existe
     */
    private static boolean solve(List<Cell> cells) {
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
    
                int idx = Grid.NB_NUM * i + j;
    
                // Si la cellule est vide et modifiable
                if(cells.get(idx).isEmpty() && cells.get(idx).isEditable()) {
                    for(int num = 1; num <= Grid.NB_NUM; num++) {
    
                        if(isValid(cells, i, j, num)) {
                            cells.get(idx).setNumber(num);
                            if(solve(cells)) {
                                return true;
                            }
                            // Vérification avant d'appeler clear
                            if(cells.get(idx).isEditable()) {
                                cells.get(idx).clear();
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Verifie si une cellule est valide
     * @param cells Liste des cellules
     * @param row Numéro de la ligne
     * @param col Numéro de la colonne
     * @param num Chiffre dans la cellule
     * @return Vrai si la cellule est vrai sinon Faux
     */
    private static boolean isValid(List<Cell> cells, int row, int col, int num) {
        // Vérifie la ligne
        for(int j=0; j<Grid.NB_NUM; j++) {
            if(cells.get(Grid.NB_NUM*row+j).getNumber() == num) {
                return false;
            }
        }

        // Vérifie la colonne
        for(int i=0; i<Grid.NB_NUM; i++) {
            if(cells.get(Grid.NB_NUM*i+col).getNumber() == num) {
                return false;
            }
        }

        // Vérifie le carré 3x3
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;

        for(int i=boxRowStart; i<boxRowStart+3; i++) {
            for(int j=boxColStart; j<boxColStart+3; j++) {
                int idx = Grid.NB_NUM*i+j;
                if(cells.get(idx).getNumber() == num) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Résu une grille (change la grille d'origine)
     * @param grid Grille à résoudre
     */
    public static void solveGrid(Grid grid) {
        List<Cell> cells = new ArrayList<>();
        // Créer des FlexCell, et non des FixCell, pour pouvoir les modifier
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                int num = grid.getCell(i, j).getNumber();
                // Utilisation de FlexCell au lieu de FixCell pour les cellules modifiables
                Cell cell = new FlexCell(i, j);
                cell.setNumber(num);
                cells.add(cell); // Notez que vous devez créer des FlexCell ici
            }
        }
        solve(cells);
    }
    
    /**
     * Tests le temps d'execution des methodes de resolution
     * @throws SQLException Erreur liée à la BdD
     */
    public static void test() throws SQLException {
        DBManager.init();
        long totalTime = 0;
        int n = 36;

        for(int id=1; id<=n; id++) {
            Grid g = DBManager.getGrid(id);
            System.out.println("\n ========== " + "Grille n°" + id + " ========== ");
            System.out.println("Grille à résoudre:");
            System.out.println(g);

            long startTime = System.currentTimeMillis();
            solveGrid(g);
            long endTime = System.currentTimeMillis();

            System.out.println("Grille résolue:");
            System.out.println(Grid.toString(g.getSolvedCells()));

            long time = endTime - startTime;
            totalTime += time;

            System.out.println("Temps de résolution de la grille n°" + id + ": " + time + " ms");

        }

        double averageTime =(double) totalTime / n;
        System.out.println("\nTemps total de résolution de toutes les grilles: " + totalTime + " ms");
        System.out.println("Temps moyen de résolution pour chaques grilles: " + averageTime + " ms");
    }
}
