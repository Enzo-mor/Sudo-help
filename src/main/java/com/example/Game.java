package com.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/***
 * cette classe represente la partie d'un jeu
 * une partie est composé d'une grille de depart et d'un profile
 * @author Taise de Thèse
 * @version 1.0
 */
public class Game {

    /**
     *  represente le profil du joeur
     */
    private Profile profile;

    /**
     *  represente la grille de depart
     */
    private Grid grid;

    /**
     * represente la liste des actions effectuées 
     */
    private List<Action> actions;


    /**
     *  represente la date de creation du jeux
     */
     private String createdDate;

    /**
     *  represente score du jeu
     */
       private int score;

     /***
      *  represente la date de la dernière modification
      */
      private String lastModifDate ;


    /**
     * constructeur de la classe Game
     * @param grid respresente la grille de depart 
     */
    public Game(Grid grid,Profile profile){
        this.grid=grid;
        this.profile=profile;
        this.actions=new ArrayList<Action>();
        this.createdDate=new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.FRANCE).format(new Date());
        this.lastModifDate=new String(this.createdDate);
    }

    public Profile getProfile() {
        return profile;
    }

    public Grid getGrid() {
        return grid;
    }
   /**
    * cette methode permet d'ajouter une action à une partie du jeu
    * cette methode sauvegarde automatique l'action  qui est ajouté dans une partie
    * @param e  represente l'action à effectuer dans le jeux
    * <p> l'action a effectué devra etre construite avec la grille instance du jeux
    * obtenu par la methode getGrid()
    *</p> 
    *<pre>
     {@code
      //Exemple
      //creation d'une partie de jeux
      Game g=new Game(DBManager.getGrid(2),new Profile("pierre"));
      //creation de l'action pour modifier la cellule
      Action a=new NumberCellAction(g.getGrid(),1,2,4,g.getGrid().getCell(1,2).getNumber());
      //ajout de l'action au jeu
      g.addAction(a);
      }
    * </pre>
    * @see Action
    */
   public void addAction(Action e){
    actions.add(e);
   }
   /**
    * 
    * @return le score de la partie
    */
    public int getScore() {
        return score;
    }
    public void addValue(int x,int y,int value){

        Action a=new NumberCellAction(grid, x, y, value, grid.getCell(x,y).getNumber());
         a.doAction();
         addAction(a);
         updateDate();
    }

    public void addAnnotation(int x,int y, int value){
        Action a=new AnnotationCellAction(grid, x, y, value);
        a.doAction();
        addAction(a);
        updateDate();
    }

    private void updateDate(){
     lastModifDate=new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.FRANCE).format(new Date());
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public String getLastModifDate() {
        return lastModifDate;
    }
    
    public static void main(String[] args) {
        Game g=new Game(null, null);
        System.out.println(g.createdDate);
    }
} 
