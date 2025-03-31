package grp6.sudocore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de resoudre des sudokus.
 * @author POUSSE Kilian
 */
public final class SudokuSolver {

    /**
     * Resout la liste des cellules donnee en parametre et renvoie 
     * la solution sans modifier l'originale.
     * @param cells Liste de cellules
     * @return Solution de la liste
     */
    public static List<Cell> solveCells(List<Cell> cells) {
        List<Cell> res = new ArrayList<>();
        // Duplication de la liste
        for(Cell c: cells) {
            res.add(c.clone());
        }

        solve(res);

        return res;
    }

    /**
     * Resout une liste de cellules (la liste donnee sera modifiee).
     * @param cells Liste a resoudre
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
                            // Verification avant d'appeler clear
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
     * Verifie si une cellule est valide.
     * @param cells Liste des cellules
     * @param row Numero de la ligne
     * @param col Numero de la colonne
     * @param num Chiffre dans la cellule
     * @return Vrai si la cellule est valide, sinon Faux
     */
    private static boolean isValid(List<Cell> cells, int row, int col, int num) {
        // Verifie la ligne
        for(int j=0; j<Grid.NB_NUM; j++) {
            if(cells.get(Grid.NB_NUM*row+j).getNumber() == num) {
                return false;
            }
        }

        // Verifie la colonne
        for(int i=0; i<Grid.NB_NUM; i++) {
            if(cells.get(Grid.NB_NUM*i+col).getNumber() == num) {
                return false;
            }
        }

        // Verifie le carre 3x3
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
     * Resout une grille (modifie la grille d'origine).
     * @param grid Grille a resoudre
     */
    public static void solveGrid(Grid grid) {
        List<Cell> cells = new ArrayList<>();
        // Creer des FlexCell, et non des FixCell, pour pouvoir les modifier
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                int num = grid.getCell(i, j).getNumber();
                // Utilisation de FlexCell au lieu de FixCell pour les cellules modifiables
                Cell cell = new FlexCell(i,j);
                cell.setNumber(num);
                cells.add(cell); // Notez que vous devez creer des FlexCell ici
            }
        }
        solve(cells);
    }

    /**
     * Teste le temps d'execution des methodes de resolution.
     * @throws SQLException Erreur liee a la BDD
     */
    public static void test() throws SQLException {
        DBManager.init();
        long totalTime = 0;
        int n = 36;

        for(int id=1; id<=n; id++) {
            Grid g = DBManager.getGrid(id);
            System.out.println("\n ========== " + "Grille n°" + id + " ========== ");
            System.out.println("Grille a resoudre:");
            System.out.println(g);

            long startTime = System.currentTimeMillis();
            solveGrid(g);
            long endTime = System.currentTimeMillis();

            System.out.println("Grille resolue:");
            System.out.println(Grid.toString(g.getSolvedCells()));

            long time = endTime - startTime;
            totalTime += time;

            System.out.println("Temps de resolution de la grille n°" + id + ": " + time + " ms");

        }

        double averageTime =(double) totalTime / n;
        System.out.println("\nTemps total de resolution de toutes les grilles: " + totalTime + " ms");
        System.out.println("Temps moyen de resolution pour chaque grille: " + averageTime + " ms");
    }
}
