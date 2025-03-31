package grp6.syshelp;

import grp6.sudocore.Grid;

/**
 * Cette classe permet d'obtenir une aide sur la grille de Sudoku en se basant sur 
 * la technique du LastNumber.
 * @see InterfaceTech
 * @author ROQUAIN Louison
 * @author GRAMMONT Dylan
 */
public class LastNumber implements InterfaceTech {

    /**
     * Constructeur de la classe LastNumber
     */
    public LastNumber(){}

    /**
     * Analyse la grille et identifie si un chiffre peut etre place dans un carre 3x3
     * sans ambiguite.
     *
     * @param grid Grille de Sudoku a analyser.
     * @return Un objet {@code Help} contenant une suggestion de placement, ou {@code null} si aucune aide n'est possible.
     */
    @Override
    public Help getHelp(Grid grid) {
        Help help = new Help(getClass().getSimpleName());

        // Verifier les blocs 3x3
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                boolean[] present = new boolean[10];

                // Marquer les chiffres presents
                for (int r = i; r < i + 3; r++) {
                    for (int c = j; c < j + 3; c++) {
                        if (!grid.getCell(r, c).isEmpty()) {
                            present[grid.getCell(r, c).getNumber()] = true;
                        }
                    }
                }

                // Trouver les chiffres manquants et verifier ou ils peuvent aller
                for (int num = 1; num <= 9; num++) {
                    if (!present[num]) {
                        int possibleRow = -1;
                        int possibleCol = -1;
                        int count = 0;

                        for (int r = i; r < i + 3; r++) {
                            for (int c = j; c < j + 3; c++) {
                                if (grid.getCell(r, c).isEmpty() && isValid(grid, r, c, num)) {
                                    count++;
                                    possibleRow = r;
                                    possibleCol = c;
                                }
                            }
                        }

                        // Si une seule case est possible, on retourne le coup
                        if (count == 1) {
                            int idx = (i / 3) * 3 + (j / 3);
                            help.addSquare(idx);
                            help.addLine(possibleRow);
                            help.addColumn(possibleCol);

                            help.setMessage(1, "Tu peux placer un " + num + ". Regarde bien tes lignes et colonnes !");
                            help.setMessage(3, "LastNumber peut etre utilise ici.");

                            return help;
                        }
                    }
                }
            }
        }
        return null; // Aucun coup trouve
    }

    /**
     * Verifie si un chiffre peut etre place dans une case sans enfreindre les regles du Sudoku.
     *
     * @param grid Grille de Sudoku.
     * @param row Ligne de la case.
     * @param col Colonne de la case.
     * @param num Chiffre a verifier.
     * @return {@code true} si le chiffre peut etre place, {@code false} sinon.
     */
    private boolean isValid(Grid grid, int row, int col, int num) {
        // Verifier la ligne
        for (int c = 0; c < 9; c++) {
            if (grid.getCell(row, c).getNumber() == num) {
                return false;
            }
        }
        // Verifier la colonne
        for (int r = 0; r < 9; r++) {
            if (grid.getCell(r, col).getNumber() == num) {
                return false;
            }
        }
        // Verifier le carre 3x3
        int i = (row / 3) * 3;
        int j = (col / 3) * 3;
        for (int r = i; r < i + 3; r++) {
            for (int c = j; c < j + 3; c++) {
                if (grid.getCell(r, c).getNumber() == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Methode principale pour tester l'algorithme LastNumber sur une grille de Sudoku.
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
        LastNumber v = new LastNumber();
        System.out.println(v.getHelp(grilleCarre));
    }
}
