package grp6.sudocore;

/**
 * Interface permettant d'ecouter les mises à jour  du temps passé dans le jeu.
 * 
 * @author DE THESE Taise
 * @see Game
 */
public interface GameTimeListener {

    /**
     * Methode appelee lorsqu'une mise à jour du temps est effectuee.
     * @param elapsedTime Le temps ecoule dans le jeu.
     */
    void onTimeUpdated(String elapsedTime);
}

