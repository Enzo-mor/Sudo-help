package grp6.syshelp;

import java.util.ArrayList;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de LastPossible.
 * @see InterfaceTech
 * @author ROQUAIN Louison /GRAMMONT Dylan
 */
public class LastPossible implements InterfaceTech {
    
    /**
     * Méthode pour vérifier si une grille peut donner un coup.
     * @param grid Grille à vérifier
     * @return Aide sur la grille
     */
    public Help getHelp(Grid grid) {
        Help help = new Help(getClass().getSimpleName());
        int compteur=0;
        // Vérifier les blocs 3x3
        for(int i = 0; i < 9; i += 3) {
            for(int j = 0; j < 9; j += 3) {
                                        System.out.println("TEST");

                ArrayList<Integer> present = new ArrayList<>();

                // Marquer les chiffres présents
                for(int r = i; r < i + 3; r++) {
                    for(int c = j; c < j + 3; c++) {
                        if(!grid.getCell(r, c).isEmpty()) {
                            present.add(grid.getCell(r, c).getNumber());
                        }
                    }
                }
                Cell[] ligne=grid.getLine(i);
                Cell[] col=grid.getColumn(j);
                for(int y = 0;y<grid.NB_NUM;y++){
                    if(!present.contains(ligne[y].getNumber())){
                        present.add(ligne[y].getNumber());
                    }
                    if(!present.contains(col[y].getNumber())){
                        present.add(col[y].getNumber());
                    }                
                }
                for (Integer it : present){
                    System.out.println("test : "+ it);
                }
                int count = 0;
                int numManquant=0;
                // Trouver les chiffres manquants et vérifier où ils peuvent aller
                for(int num = 1; num <= 9; num++) {
                    if(!present.contains(num)) {
                        count++;
                        numManquant=num;
                    }
                }
                // Si une seule case est possible, on retourne le coup
                if(count == 1) {
                    int idx = (i / 3) * 3 + (j / 3);
                    help.addSquare(idx);
                    //help.addLine(possibleRow);
                    //help.addColumn(possibleCol);

                    help.setMessage(1, "Tu peux placer un " + numManquant + ". Regarde bien tes lignes et colonnes !");
                    help.setMessage(3, "LastPossible peut être utilisé ici.");

                    return help;
                }
            }
        }
        return null; // Aucun coup trouvé
    }

    /**
     * Vérifie si un chiffre peut être placé dans une case sans enfreindre les règles du Sudoku.
     * @param grid Grille de Sudoku
     * @param row Ligne de la case
     * @param col Colonne de la case
     * @param num Chiffre à vérifier
     * @return Vrai si le chiffre peut être placé, faux sinon
     */
    private boolean isValid(Grid grid, int row, int col, int num) {
        // Vérifier la ligne
        for(int c = 0; c < 9; c++) {
            if(grid.getCell(row, c).getNumber() == num) {
                return false;
            }
        }
        // Vérifier la colonne
        for(int r = 0; r < 9; r++) {
            if(grid.getCell(r, col).getNumber() == num) {
                return false;
            }
        }
        // Vérifier le carré 3x3
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
