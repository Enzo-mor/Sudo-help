package grp6.syshelp;
/**
 * Exception levée lorsqu'on tente de mettre un chiffre sur une cellule
 * non modifiable.
 * @author NGANGA YABIE Taïse de These
 * @version 1.0
 * @see Cell
 */
public class NoPutNumberOnCellExeception extends RuntimeException {
    public NoPutNumberOnCellExeception(String message) {
        super(message);
    }
    
}
