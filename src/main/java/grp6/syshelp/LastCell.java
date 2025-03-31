package grp6.syshelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe represente la technique LastCell.
 * 
 * Elle permet de trouver la derniere cellule vide d'une ligne, colonne ou carre.
 * @author GRAMMONT Dylan
 * @see InterfaceTech Contenant les methodes des techniques
 * 
 */
public class LastCell implements InterfaceTech {

    /**
     * Constructeur de la classe LastCell
     */
    public LastCell(){}

    @Override
    /**
     * Analyse la grille de Sudoku et fournit une aide si une case vide peut etre remplie 
     * dans une ligne, une colonne ou un carre contenant deja 8 chiffres.
     *
     * @param grille La grille de Sudoku a analyser.
     * @return Un objet {@code Help} contenant une suggestion de placement, ou {@code null} si aucune aide n'est possible.
     */
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        for (int i = 0; i < Grid.NB_NUM; i++) {
            // Verifie les colonnes
            if (grille.numberOfFullCell(Grid.Shape.COLUMN, i) == 8) {
                Cell emptyCell = findEmptyCell(grille.getColumn(i));
                if (emptyCell != null) {
                    aide.addColumn(i);
                    aide.setMessage(1, "Tu peux placer un " + findLastNumber(grille.getColumn(i)));
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
            // Verifie les lignes
            else if (grille.numberOfFullCell(Grid.Shape.LINE, i) == 8) {
                Cell emptyCell = findEmptyCell(grille.getLine(i));
                if (emptyCell != null) {
                    aide.addLine(i);
                    aide.setMessage(1, "Tu peux placer un " + findLastNumber(grille.getLine(i)));
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
            // Verifie les carres
            else if (grille.numberOfFullCell(Grid.Shape.SQUARE, i) == 8) {
                int[] temp = grille.numToPosForSubGrid(i);
                Cell emptyCell = findEmptyCell(grille.getFlatSubGrid(temp[0], temp[1]));
                if (emptyCell != null) {
                    aide.addSquare(i);
                    aide.setMessage(1, "Tu peux placer un " + findLastNumber(grille.getFlatSubGrid(temp[0], temp[1])));
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
        }
        return null; // Aucun coup trouve
    }

    /**
     * Recherche une cellule vide dans un tableau de cellules (ligne, colonne ou carre).
     *
     * @param cells Tableau de cellules a analyser.
     * @return La premiere cellule vide trouvee, ou {@code null} si aucune n'est vide.
     */
    private Cell findEmptyCell(Cell[] cells) {
        for (Cell cell : cells) {
            if (cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Determine le chiffre manquant dans un tableau de cellules (ligne, colonne ou carre).
     *
     * @param cells Tableau de cellules a analyser.
     * @return Le chiffre qui manque dans la sequence, compris entre 1 et 9.
     */
    private int findLastNumber(Cell[] cells) {
        List<Integer> list = new ArrayList<>(Collections.nCopies(10, 0));
        for (Cell cell : cells) {
            int c = cell.getNumber();
            list.set(c, list.get(c) + 1);
        }
        for (int i = 1; i <= 9; i++) { // Sudoku utilise les chiffres de 1 a 9
            if (list.get(i) == 0) return i;
        }
        return 0; // Ne devrait jamais arriver si l'entree est valide
    }

}    