package grp6.intergraph;

import grp6.sudocore.Game;
import grp6.sudocore.SudoTypes.GameState;

public class Sudoku {
    private final int id;
    private String name;
    private long bestTime;
    private int score;
    private GameState status;
    private Game game;

    public Sudoku(int id, String name, long bestTime, int score, GameState status) {
        this.id=id;
        this.name = name;
        this.bestTime = bestTime;
        this.score = score;
        this.status = status;
        this.game=null;
    }

    public String getName() { 
        return name;
    }

    public Long getBestTime() { 
        return bestTime;
    }

    public int getScore() { 
        return score;
    }

    public GameState getStatus() { 
        return status;
    }

    public int getId() {
        return id;
    }

    public boolean gameExists(){
        return this.game!=null;
    }

    public Game getGame(){
        return game;
    }

    public void modifyInfo(long time, int score, GameState state, Game game) {
        this.bestTime=time;
        this.score=score;
        this.status=state;
        this.game=game;
    }

    public String toString(){
        return name;
    }

    public void setStatus(GameState status) {
        this.status = status;
    }
}

