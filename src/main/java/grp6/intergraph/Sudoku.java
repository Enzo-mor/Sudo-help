package grp6.intergraph;

import grp6.sudocore.Game;
import grp6.sudocore.SudoTypes.GameState;

/**
 * Représente un jeu de Sudoku avec des informations sur son état, son temps record et son score.
 * 
 * @author PERRON
 * @author RASSON
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
     * État actuel du jeu
     */
    private GameState status;

    /**
     * Instance du jeu associée à ce Sudoku
     */
    private Game game;

    /**
     * Constructeur de la classe Sudoku.
     * 
     * @param id        Identifiant unique du jeu
     * @param name      Nom du jeu
     * @param bestTime  Meilleur temps enregistré pour ce jeu
     * @param score     Score obtenu
     * @param status    État du jeu
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
     * @return L'identifiant unique du jeu
     */
    public int getId() {
        return id;
    }

    /**
     * Renvoie le nom du jeu.
     * 
     * @return Le nom du jeu.
     */
    public String getName() { 
        return name;
    }

    /**
     * Retourne le meilleur temps enregistré.
     * 
     * @return Le meilleur temps
     */
    public Long getBestTime() { 
        return bestTime;
    }

    /**
     * Retourne le score obtenu.
     * 
     * @return Le score du jeu
     */
    public int getScore() { 
        return score;
    }

    /**
     * Retourne l'état actuel du jeu.
     * 
     * @return L'état du jeu
     */
    public GameState getStatus() { 
        return status;
    }

    /**
     * Vérifie si une instance de Game est associé à ce Sudoku.
     * 
     * @return true si une partie existe, false sinon.
     */
    public boolean gameExists(){
        return this.game!=null;
    }

    /**
     * Retourne l'instance du jeu associée.
     * 
     * @return L'instance de Game ou null si elle n'existe pas.
     */
    public Game getGame(){
        return game;
    }

    /**
     * Modifie les informations du jeu.
     * 
     * @param time  Nouveau meilleur temps
     * @param score Nouveau score
     * @param state Nouvel état du jeu
     * @param game  Nouvelle instance de Game associé
     */
    public void modifyInfo(long time, int score, GameState state, Game game) {
        this.bestTime=time;
        this.score=score;
        this.status=state;
        this.game=game;
    }

    /**
     * Retourne le nom du jeu sous forme de chaîne de caractères.
     * 
     * @return Le nom du jeu
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Met à jour l'état du jeu.
     * 
     * @param status    Le nouvel état du jeu
     */
    public void setStatus(GameState status) {
        this.status = status;
    }
}

