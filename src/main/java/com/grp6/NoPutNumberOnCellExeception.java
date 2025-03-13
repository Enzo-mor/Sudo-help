package grp6.sudocore;
/**
 * Exception levée lorsqu'on tente de mettre un chiffre sur une cellule
 * non modifiable.
 * @author Taise de Thèse
 * @version 1.0
 * @see ReadOnlyCell
 * @see Cell
 */
public class NoPutNumberOnCellExeception extends RuntimeException {
    public NoPutNumberOnCellExeception(String message) {
        super(message);
    }
    
}
