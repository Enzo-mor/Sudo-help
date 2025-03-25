package grp6.sudocore;

/**
 * Represente un joueur dans un jeu avec son nom et son score.
 * 
 * @author RASSON Emma
 */
public class Player {
    private String name;
    private int score;

    /**
     * Constructeur permettant de creer un joueur avec un nom et un score.
     * 
     * @param name Le nom du joueur.
     * @param score Le score du joueur.
     */
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Retourne le nom du joueur.
     * 
     * @return Le nom du joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le score du joueur.
     * 
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }
}
