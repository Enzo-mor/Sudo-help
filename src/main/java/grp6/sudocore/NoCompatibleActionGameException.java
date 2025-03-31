package grp6.sudocore;

/**
 * Exception levee lorsqu'une action n'est pas conforme au jeu
 * en cours. Par exemple, si l'instance du jeu en cours n'est pas la meme que celle de l'action.
 * 
 * @author NGANGA YABIE Taise de These
 * @see Action Interface permettant de definir des actions dans le jeu.
 * @see Game Classe representant un jeu de Sudoku.
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

