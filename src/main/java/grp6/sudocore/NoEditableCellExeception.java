package grp6.sudocore;

/**
 * Exception levée lorsque la cellule attendue n'est pas modifiable
 * (non modifiable).
 * 
 * @author NGANGA YABIE Taïse de These
 * @see Cell Interface des cellules dans la grille.
 */
public class NoEditableCellExeception extends RuntimeException {

    /**
     * Constructeur de l'exception.
     * 
     * @param message Message détaillant la cause de l'exception.
     */
    public NoEditableCellExeception(String message) {
        super(message);
    }
}
