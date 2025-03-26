package grp6.syshelp;

import java.util.Arrays;
import java.util.List;

import grp6.sudocore.Cell;

/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de PointingTriples.
 * @author Taïse De Thèse
 * @version 1.0
 * @since 2025-03-19
 */
public class PointingTriples extends PointingPairs {

     
    @Override
    protected boolean hasPointing(Cell[][] subgrid, int candidate) {
        List<int[]> positions = getCandidatePositions(subgrid, candidate);
        if (positions.size()==3) {
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

    public String getName(){
        return "Triples pointantes";
    }
    @Override
    protected Boolean isValidNumberCandidate(int candidate,Cell[]LineCell){
      return Arrays.asList(LineCell).stream().filter(cell->cell.isEditable()&&cell.getAnnotationsBool()[candidate-1]).count()>3;

    }

}
