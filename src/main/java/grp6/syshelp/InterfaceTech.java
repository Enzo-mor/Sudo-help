package grp6.syshelp;

import grp6.sudocore.*;

public interface InterfaceTech {
    
    public boolean detect(Grid grille);

    //Applique la technique dans une copie de la grille du joueur
    public void applique(Grid grilleJoueur);
}
