package grp6.sudocore;
/**
 * Exception levee lorsqu'on tente d'assigner un chiffre a une cellule
 * non modifiable.
 * 
 * @author DE THESE Taise
 * @see Cell Interface des cellules dans la grille.
 */
public class NoPutNumberOnCellExeception extends RuntimeException {

    /**
     * Constructeur de l'exception.
     * 
     * @param message Message detaillant la cause de l'exception.
     */
    public NoPutNumberOnCellExeception(String message) {
        super(message);
    }
}

