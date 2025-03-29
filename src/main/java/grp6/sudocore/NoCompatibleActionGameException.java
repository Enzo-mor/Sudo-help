package grp6.sudocore;

/**
 * Exception levée lorsqu'une action n'est pas conforme au jeu
 * en cours. Par exemple, si l'instance du jeu en cours n'est pas la même que celle de l'action.
 * 
 * @author NGANGA YABIE Taïse de These
 * @see Action Interface permettant de définir des actions dans le jeu.
 * @see Game Classe représentant un jeu de Sudoku.
 */
public class NoCompatibleActionGameException extends RuntimeException {

    /**
     * Constructeur de l'exception.
     * 
     * @param message Message détaillant la cause de l'exception.
     */
    public NoCompatibleActionGameException(String message) {
        super(message);
    }
}

