package grp6.syshelp;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import grp6.sudocore.Cell;
import grp6.sudocore.FlexCell;
import grp6.sudocore.Grid;

/**
 * Classe permettant la generation automatique des annotations
 * d'une grille
 * @author Kilian POUSSE
 */

public class AutoAnnotation {

    /**
     * Generation des annotation automatiquement
     * @param grid Grille a modifier
     */
    public static void generate(Grid grid) {
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                Cell cell = grid.getCell(i, j);
                if(cell instanceof FlexCell)
                    generate(grid, cell, i, j);
            }
        }
    }

    /**
     * Generation d'une case d'une grille
     * @param grid  Grille a modifier
     * @param cell  Case a modifier
     * @param i Identifiant de la ligne
     * @param j Identifiant de la colonne
     */
    public static void generate(Grid grid, Cell cell, int i, int j) {
        List<Integer> possibleValues = getIntersection(
            onLine(grid, i), 
            onColumn(grid, j), 
            onSubGrid(grid, i, j)
        );

        // Appliquer les annotations à la cellule
        for(int num : possibleValues) {
            cell.addAnnotation(num);
        }
    }

    /**
     * Intersection des listes d'annotations
     * @param list1 Liste des annotations des lignes
     * @param list2 Liste des annotations des colonnes
     * @param list3 Liste des annotations de la sous-grilles
     * @return Liste des annotations finales
     */
    private static List<Integer> getIntersection(List<Integer> list1, List<Integer> list2, List<Integer> list3) {
        return list1.stream()
            .filter(list2::contains)
            .filter(list3::contains)
            .collect(Collectors.toList());
    }

    /**
     * Generation des annotation par apport à la ligne
     * @param grid  Grille a modifier
     * @param i Identifiant de la ligne
     * @return Liste des annotations des lignes
     */
    private static List<Integer> onLine(Grid grid, int i) {
        return getMissingNumbers(grid.getLine(i));
    }

    /**
     * Generation des annotation par apport à la colonne
     * @param grid  Grille a modifier
     * @param j Identifiant de la colonne
     * @return Liste des annotations des colonnes
     */
    private static List<Integer> onColumn(Grid grid, int j) {
        return getMissingNumbers(grid.getColumn(j));
    }

    /**
     * Generation des annotation par apport à la sous-grille
     * @param grid Grille a modifier
     * @param i Identifiant de la ligne
     * @param j Identifiant de la colonne
     * @return Liste des annotations de la sous-grille
     */
    private static List<Integer> onSubGrid(Grid grid, int i, int j) {
        return getMissingNumbers(grid.getFlatSubGrid(i, j));
    }

    /**
     * Verifi si il manque des nombres dans un tableau de cases
     * @param cells tableau de cases
     * @return
     */
    private static List<Integer> getMissingNumbers(Cell[] cells) {
        Set<Integer> existingNumbers = Arrays.stream(cells)
            .map(Cell::getNumber)
            .collect(Collectors.toSet());

        return IntStream.rangeClosed(1, 9)
            .filter(n -> !existingNumbers.contains(n))
            .boxed()
            .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int[] data = {
            0, 0, 0,  0, 8, 7,  3, 0, 9,
            0, 0, 0,  9, 0, 6,  0, 0, 0,
            0, 4, 5,  0, 0, 0,  0, 0, 0,

            0, 0, 4,  8, 0, 0,  6, 0, 5,
            2, 8, 0,  0, 0, 0,  0, 9, 1,
            5, 0, 6,  0, 0, 1,  7, 0, 0,

            0, 0, 0,  0, 0, 0,  5, 6, 0,
            0, 0, 0,  3, 0, 8,  0, 0, 0,
            4, 0, 8,  5, 6, 0,  0, 0, 0
        };

        Grid grid = new Grid(data);
        System.out.println(grid);

        AutoAnnotation.generate(grid);

        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 9; y++) {
                boolean[] ann = grid.getCell(x, y).getAnnotationsBool();
                System.out.print("\n--> " + x + "," + y + ": ");
                for(int i = 0; i < Grid.NB_NUM; i++) {
                    if(ann[i]) System.out.print(i + 1 + " ");
                }
            }
        }
    }
}
