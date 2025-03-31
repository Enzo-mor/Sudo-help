package grp6.syshelp;

import java.util.Arrays;
import java.util.List;

import grp6.sudocore.Cell;

/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de PointingTriples.
 * @author NGANGA YABIE Taise de These
 */
public class PointingTriples extends PointingPairs {

    /**
     * Constructeur de la classe PointingTriples
     */
    public PointingTriples(){}

    /**
     * Verifie si une technique de PointingTriples est applicable pour un sous-grille en fonction d'un candidat.
     * La technique de Pointing se produit lorsque le candidat n'apparait que sur une ligne ou une colonne dans un sous-grille.
     * 
     * @param subgrid La sous-grille de Sudoku dans laquelle chercher le candidat
     * @param candidate Le candidat (chiffre) pour lequel nous vérifions l'existence de la technique de PointingTriples
     * @return true si la technique de PointingTriples est applicable, sinon false
     */
    @Override
    protected boolean hasPointing(Cell[][] subgrid, int candidate) {
        List<int[]> positions = getCandidatePositions(subgrid, candidate);
        if (positions.size() == 3) {
            int[] first = positions.get(0);
            int[] second = positions.get(1);
            int[] third = positions.get(2);
            if (first[0] == second[0] && second[0] == third[0]) {
                return true;
            }
            if (first[1] == second[1] && second[1] == third[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne le nom de la technique appliquee (dans ce cas, "Triples pointantes").
     * 
     * @return Le nom de la technique
     */
    @Override
    public String getName() {
        return "Triples pointantes";
    }

    /**
     * Verifie si un candidat est valide dans une ligne en fonction de ses annotations.
     * La technique est valide si un candidat est present sur plus de trois cases modifiables dans la ligne.
     * 
     * @param candidate Le candidat (chiffre) a verifier
     * @param LineCell La ligne de cellules a examiner
     * @return true si le candidat est valide, sinon false
     */
    @Override
    protected Boolean isValidNumberCandidate(int candidate, Cell[] LineCell) {
        return Arrays.asList(LineCell).stream()
            .filter(cell -> cell.isEditable() && cell.getAnnotationsBool()[candidate - 1])
            .count() > 3;
    }


}
