package grp6.syshelp;

import grp6.sudocore.Grid;

public interface InterfaceTech {

    /**
     * Cette méthode permet d'obtenir une aide sur la grille de sudoku en se basant sur une technique de résolution.
     * @param grille
     * @return l'aide à apporter ou null si aucune aide n'est possible.
     */
    public Help getHelp(Grid grille);
}

