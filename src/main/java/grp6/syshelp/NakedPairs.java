package grp6.syshelp;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe represente la technique NakedPairs.
 * 
 * Elle permet de detecter les paires nues, c'est-a-dire deux chiffres apparaissant uniquement 
 * dans deux cases d'une meme unite (ligne, colonne ou region 3x3). 
 * Lorsque de telles paires sont trouvees, elles peuvent etre utilisees pour reduire les annotations
 * des autres cases de l'unite concerne.
 * 
 * @author GRAMMONT Dylan
 * @see InterfaceTech Contient les methodes des differentes techniques d'aide
 */
public class NakedPairs implements InterfaceTech {
    
    /**
     * Instance de la classe Help, initialisée avec le nom de la classe actuelle pour fournir une aide contextuelle.
     * Cette instance sera utilisée pour générer des messages d'aide ou des annotations pour les techniques de résolution du Sudoku.
     */
    private Help aide = new Help(getClass().getSimpleName());

    /**
     * Constructeur de la classe NakedPairs
     */
    public NakedPairs(){}

    /**
     * Extrait les deux nombres formant une paire d'un tableau de booleens.
     * 
     * @param tabBool Tableau de booleens representant les chiffres possibles dans une case
     * @return Un tableau contenant les deux chiffres formes par la paire
     */
    private int[] donnerPair(boolean[] tabBool) {
        int[] tab = new int[2];
        int j = 0;
        for (int i = 0; i < 9; i++) {
            if (tabBool[i]) {
                tab[j] = i;
                j++;
            }
        }
        return tab;
    }
    
    /**
     * Detecte les paires dans une region 3x3.
     * 
     * @param num Numero de la region a analyser
     * @param grille Grille de sudoku
     * @return true si une paire a ete trouvee et a conduit a une reduction des annotations, false sinon
     */
    private boolean detectPairsCarre(int num, Grid grille) {
        int[][] tabPair = new int[9][2];
        int cptPair = 0;
        
        int[] indiceCell = grille.numToPosForSubGrid(num);
        Cell[][] mat = grille.getSubGrid(indiceCell[0], indiceCell[1]);
        boolean[] tabBool;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int cptAnnotation = 0;
                tabBool = mat[i][j].getAnnotationsBool();
                for (boolean b : tabBool) {
                    if (b) cptAnnotation++;
                }
                if (cptAnnotation == 2) {
                    tabPair[cptPair] = donnerPair(tabBool);
                    cptPair++;
                }
            }
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i != j && tabPair[i][0] == tabPair[j][0] && tabPair[i][1] == tabPair[j][1] && tabPair[i][0] != 0) {
                    Cell solution = removePair(grille.getFlatSubGrid(indiceCell[0], indiceCell[1]), tabPair[j]);
                    if (solution != null && solution.getAnnotations().size() == 1) {
                        int number = solution.getAnnotations().get(0) - 1;
                        aide.addSquare(num);
                        aide.setMessage(1, "Une paire nue a ete trouvee, regarde l'annotation contenant : " + (number + 1));
                        aide.setMessage(3, "Paire detectee : " + (tabPair[j][0] + 1) + " et " + (tabPair[j][1] + 1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Detecte les paires nues dans une ligne ou une colonne.
     * 
     * @param num Numero de la ligne ou de la colonne a analyser
     * @param grille Grille de sudoku
     * @return true si une paire a ete trouvee et a conduit a une reduction des annotations, false sinon
     */
    private boolean detectPairs(int num, Grid grille) {
        int[][] tabPairLine = new int[9][2];
        int[][] tabPairCol = new int[9][2];
        int cptPair = 0;
        
        Cell[] line = grille.getLine(num);
        Cell[] col = grille.getColumn(num);
        
        for (int i = 0; i < 9; i++) {
            if (line[i].getAnnotations().size() == 2) {
                tabPairLine[cptPair] = donnerPair(line[i].getAnnotationsBool());
                cptPair++;
            }
            if (col[i].getAnnotations().size() == 2) {
                tabPairCol[cptPair] = donnerPair(col[i].getAnnotationsBool());
                cptPair++;
            }
        }
        
        for (int i = 0; i < grille.NB_NUM; i++) {
            for (int j = 0; j < grille.NB_NUM; j++) {
                if (i != j) {
                    if (tabPairLine[i][0] == tabPairLine[j][0] && tabPairLine[i][1] == tabPairLine[j][1]) {
                        Cell solution = removePair(grille.getLine(num), tabPairLine[j]);
                        if (solution != null && solution.getAnnotations().size() == 1) {
                            aide.addLine(num);
                            aide.setMessage(1, "Une paire nue a ete trouvee, regarde l'annotation contenant : " + (solution.getAnnotations().get(0)));
                            return true;
                        }
                    }
                    if (tabPairCol[i][0] == tabPairCol[j][0] && tabPairCol[i][1] == tabPairCol[j][1]) {
                        Cell solution = removePair(grille.getColumn(num), tabPairCol[j]);
                        if (solution != null && solution.getAnnotations().size() == 1) {
                            aide.addColumn(num);
                            aide.setMessage(1, "Une paire nue a ete trouvee, regarde l'annotation contenant : " + (solution.getAnnotations().get(0)));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Supprime les chiffres d'une paire des annotations des autres cases d'une unite.
     * 
     * @param tab Tableau de cellules de l'unite (ligne, colonne ou region)
     * @param pair Tableau contenant les chiffres de la paire detectee
     * @return La cellule modifiee si un seul chiffre reste, sinon null
     */
    private Cell removePair(Cell[] tab, int[] pair) {
        for (Cell cell : tab) {
            if (cell.getAnnotations().size() > 1) {
                cell.removeAnnotation(pair[0]);
                cell.removeAnnotation(pair[1]);
                if (cell.OnlyOneAnnotation()) {
                    return cell;
                }
            }
        }
        return null;
    }
    
    /**
     * Analyse la grille pour determiner si un coup peut etre joue en utilisant la technique NakedPairs.
     * @param grille La grille de Sudoku a analyser
     * @return Une aide indiquant le coup possible, ou null si aucun coup n'est trouve
     */
    @Override
    public Help getHelp(Grid grille) {
        for (int i = 0; i < 9; i++) {
            aide.resetPositions();
            if (detectPairsCarre(i, grille) || detectPairs(i, grille)) {
                return aide;
            }
        }
        return null;
    }
}
