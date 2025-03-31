package grp6.syshelp;

import grp6.sudocore.Grid;

/**
 * Interface representant une technique d'aide a la resolution d'une grille de Sudoku.
 * Implementee par differentes classes fournissant des strategies specifiques d'assistance.
 */
public interface InterfaceTech {

    /**
     * Fournit une aide basee sur une technique de resolution du Sudoku.
     * Cette methode analyse la grille fournie et determine si une assistance peut etre apportee.
     *
     * @param grille la grille de Sudoku sur laquelle l'aide doit etre appliquee.
     * @return un objet {@code Help} contenant l'aide a apporter, ou {@code null} si aucune aide n'est possible.
     */
    Help getHelp(Grid grille);
}
