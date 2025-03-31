package grp6.intergraph;

import grp6.sudocore.Game;
import grp6.sudocore.SudoTypes.GameState;

/**
 * Cette classe represente un jeu de Sudoku avec des informations sur son etat, son temps record et son score.
 * Elle permet de gerer les differentes donnees relatives a un jeu de Sudoku, telles que le meilleur temps,
 * le score obtenu et l'etat actuel du jeu.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see Game
 * @see GameState
 */
public class Sudoku {

    /** 
     * Identifiant unique du jeu
     */
    private final int id;

    /** 
     * Nom du jeu
     */
    private final String name;

    /** 
     * Meilleur temps pour ce jeu
     */
    private long bestTime;

    /** 
     * Score obtenu dans ce jeu
     */
    private int score;

    /**
     * Etat actuel du jeu
     */
    private GameState status;

    /**
     * Instance du jeu associee a ce Sudoku
     */
    private Game game;

    /**
     * Constructeur de la classe Sudoku.
     * 
     * @param id Identifiant unique du jeu [int]
     * @param name Nom du jeu [String]
     * @param bestTime Meilleur temps enregistre pour ce jeu [long]
     * @param score Score obtenu [int]
     * @param status Etat du jeu [GameState]
     */
    public Sudoku(int id, String name, long bestTime, int score, GameState status) {
        this.id=id;
        this.name = name;
        this.bestTime = bestTime;
        this.score = score;
        this.status = status;
        this.game=null;
    }

    /**
     * Retourne l'identifiant unique du jeu.
     * 
     * @return L'identifiant unique du jeu [int]
     */
    public int getId() {
        return id;
    }

    /**
     * Renvoie le nom du jeu.
     * 
     * @return Le nom du jeu [String]
     */
    public String getName() { 
        return name;
    }

    /**
     * Retourne le meilleur temps enregistre.
     * 
     * @return Le meilleur temps [Long]
     */
    public Long getBestTime() { 
        return bestTime;
    }

    /**
     * Retourne le score obtenu.
     * 
     * @return Le score du jeu [int]
     */
    public int getScore() { 
        return score;
    }

    /**
     * Retourne l'etat actuel du jeu.
     * 
     * @return L'etat du jeu [GameState]
     */
    public GameState getStatus() { 
        return status;
    }

    /**
     * Verifie si une instance de Game est associee a ce Sudoku.
     * 
     * @return true si une partie existe, false sinon [boolean]
     */
    public boolean gameExists(){
        return this.game!=null;
    }

    /**
     * Retourne l'instance du jeu associee.
     * 
     * @return L'instance de Game ou null si elle n'existe pas [Game]
     */
    public Game getGame(){
        return game;
    }

    /**
     * Modifie les informations du jeu.
     * 
     * @param time Nouveau meilleur temps [long]
     * @param score Nouveau score [int]
     * @param state Nouvel etat du jeu [GameState]
     * @param game Nouvelle instance de Game associee [Game]
     */
    public void modifyInfo(long time, int score, GameState state, Game game) {
        this.bestTime=time;
        this.score=score;
        this.status=state;
        this.game=game;
    }

    /**
     * Retourne le nom du jeu sous forme de chaîne de caracteres.
     * 
     * @return Le nom du jeu [String]
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Met a jour l'etat du jeu.
     * 
     * @param status Le nouvel etat du jeu [GameState]
     */
    public void setStatus(GameState status) {
        this.status = status;
    }
}

