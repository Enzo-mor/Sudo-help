package grp6.syshelp;
/**
 * Exception levee lorsqu'on tente de mettre un chiffre sur une cellule
 * non modifiable.
 * @author NGANGA YABIE Taise de These
 */
public class NoPutNumberOnCellExeception extends RuntimeException {
    /**
     * Constructeur de la classe NoPutNumberOnCellException
     * @param message message
     */
    public NoPutNumberOnCellExeception(String message) {
        super(message);
    }
    
}
