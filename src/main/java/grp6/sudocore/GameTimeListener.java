package grp6.sudocore;

/**
 * Interface permettant d'ecouter les mises a jour du temps passe dans le jeu.
 * 
 * @author NGANGA YABIE Taise de These
 * @see Game
 */
public interface GameTimeListener {

    /**
     * Methode appelee lorsqu'une mise a jour du temps est effectuee.
     * @param elapsedTime Le temps ecoule dans le jeu.
     */
    void onTimeUpdated(String elapsedTime);
}

