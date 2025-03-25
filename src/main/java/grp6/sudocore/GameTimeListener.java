package grp6.sudocore;

/**
 * Interface permettant de mettre a jour le temps passe dans le jeu.
 * 
 * @author DE THESE Taise
 * @see Game
 */
public interface GameTimeListener {

    /**
     * Met a jour le temps passe dans le jeu.
     * @param elapsedTime Le temps ecoule dans le jeu.
     */
    void onTimeUpdated(String elapsedTime);
}

