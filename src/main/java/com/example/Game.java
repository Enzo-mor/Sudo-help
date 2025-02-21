package com.example;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

/***
 * <p>cette classe represente une partie d'un jeu de sudoku
 * une partie est composé d'une grille de depart et d'un profile
 * elle permet de gerer les actions effectuées par le joueur
 * et de sauvegarder les actions effectuées
 * elle permet aussi de gerer le timer du jeu 
 * elle permet de gerer le score du jeu
 * elle permet de gerer le taux de progression du jeu
 * elle permet de gerer l'historique des actions effectuées</p>
 * <p>De plus , pour commencer une partie il faut  imperativement appeler la methode startGame
 * pour mettre en pause une partie il faut imperativement appeler la methode pauseGame
 * pour reprendre une partie il faut imperativement appeler la methode ResumeGame
 * et pour terminer(arreter) une partie il faut imperativement appeler la methode stopGame</p>
 * 
 * @author Taise de Thèse
 * @version 1.0
 * 
 * @see Grid
 * @see Profile
 * @see Action
 */
public class Game {


  //================ variable d'instance===========
    /**
     *  represente le profil du joeur
     */
    private Profile profile;

    /**
     *  represente id du jeu
     */
    private final long id;


    /**
     *  represente la grille de depart
     */
    private final Grid grid;

    /**
     * represente la liste des actions effectuées 
     */
    private List<Action> actions;

    /**
     *  represente la date de creation du jeux
     */
     private final String createdDate;

    /**
     *  represente score du jeu
     */
       private int score;

     /***
      *  represente la date de la dernière modification
      */
      private String lastModifDate ;

     /**
      *  represente le taux de progression du jeux en pourcentage
      */
      private double progressRate;
    
      //==========variable d'instance pour le timer du jeu
      /**
       * represente le temps ecoulé  secondes
       */
      private long elapsedTime; 

      /**
       *  represente le timer du jeu(processus qui permettra le comptage)
       */
      private ScheduledExecutorService timer;

      /**
       *  represente l'inteface d'ecoute du timer
       */
      private GameTimeListener timeListener;

      /**
       * represente l'etat du jeu   en pause ou non
       */
      private boolean isPaused;

      /***
       * represente l'index courant de la liste des actions
       */
      private int currentIndex;

      /**
       *  represente l'historique des actions effectuées
       */
      private String histoActions=new String();


    /**
     * constructeur de la classe Game
     * @param grid respresente la grille du jeu
     * @param profile represente le profile du jeu
     * 
     * @throws leve une exeception en cas d'erreur de connection à la base de donnée
     */
    public Game(Grid grid,Profile profile)throws SQLException{
        this.id=DBManager.getLastIdGame()+1;
        this.grid=grid;
        this.profile=profile;
        this.actions=new LinkedList<Action>();
        this.createdDate=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        this.lastModifDate=new String(this.createdDate);
        calculateProgressRate();
        this.elapsedTime = 0; 
        currentIndex=-1;
        isPaused=false;
        this.timer = Executors.newScheduledThreadPool(1);
    }
    /**
     * cette methode permet de recommencer une partie en renitialisant la grille
     * et en supprimant les actions effectuées
     */
    void restartGame(){
        this.actions.clear();
        this.histoActions="";
        this.lastModifDate=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        calculateProgressRate();
        this.elapsedTime = 0; 
        currentIndex=-1;
        score=0;
        this.grid.resetGrid();
        isPaused=false;
        startGame();
    }
    /**
     * cette methode retourne id du jeux
     * @return
     */
    public long getId() {
        return id;
    }

     /**
      * constructeur de la classe Game qui sera utliser pour charger un jeu depuis la base de données
      *
       @param id reprente l'id du jeu
      * @param grid represente la grille du jeu 
      * @param profile represente le profile du jeu
      * @param createdDate represente la date de creation du jeu
      * @param lastModifDate    represente la date de la derniere modification du jeu
      * @param progressRate represente le taux de progression du jeu
      * @param elapsedTime represente le temps ecoulé
      */

     protected Game( long id,Grid grid,Profile profile, String createdDate, String lastModifDate, double progressRate, long elapsedTime){
        this.id=id;
        this.grid=grid;
        this.profile=profile;
        this.createdDate=createdDate;
        this.lastModifDate=lastModifDate;
        this.progressRate=progressRate;
        this.elapsedTime=elapsedTime;
        isPaused=false;
        this.timer = Executors.newScheduledThreadPool(1);
       
    }

    /**
     * cette methode permet d'appliquer les actions à une partie du jeu auquel elle est associée 
     * et il n'existe pas d'actions déjà effectuées 
     * 
     * @throws NoCompatibleActionGameException leve une exception si l'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu
     * @throws NoPutNumberOnCellExeception leve une exception si l'action doit etre appliqué sur une cellule qui ne peut pas etre modifié
     * @throws IllegalStateException leve une exception si des actions ont deja été effectuées
     * @param actions represente la liste des actions à appliquer
    
     */
    protected void applyActions(List<Action> actions) throws NoCompatibleActionGameException,NoPutNumberOnCellExeception,IllegalStateException{
         if(this.actions!=null)
         throw new IllegalStateException("impossible d'appliquer les actions car des actions ont deja été effectuées");
         int index=1;
        for(Action a:actions){
            if(this!=a.game){
                throw new NoCompatibleActionGameException("l'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu");
            }
            a.doAction();
             histoActions+=(index==actions.size())?"Action "+index+" : "+a.toString()+" (position actuel)\n": "Action "+index+" : "+a.toString()+"\n";
        }
        this.actions=actions;
        currentIndex=actions.size()-1;
    }

    /**
     * cette methode permet de commencer le jeu
     * @throws Exception leve une exception en cas d'erreur
     */
    public void startGame(){
        if (timer == null || timer.isShutdown()) {
            this.timer = Executors.newScheduledThreadPool(1); 
        }
        startTimer();
        
        
       }
      

    /**
     * cette methode permet de retourner le temps ecoulé
     * 
     * @return Retourne le temps écoulé au format HH:mm:ss.
     */
    public String getSringtElapsedTime() {
        long seconds = elapsedTime;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    /**
     *  cette methode retourne le temps 
     *  ecoulé 
     * @return
     */
    public long getElapsedTime() {
        return elapsedTime;
    }
  
    /**
     * methode permettant de demarrer le timer du jeu
     * et permet d'interragir avec l'interface UI
     */
    private void startTimer() {
        if (timer == null || timer.isShutdown()) {
            this.timer = Executors.newScheduledThreadPool(1);
        }
    
        timer.scheduleAtFixedRate(() -> {
            if (!isPaused) {
                elapsedTime++; // Augmente le temps écoulé
                if (timeListener != null) {
                    String formattedTime = getSringtElapsedTime();
                    Platform.runLater(() -> timeListener.onTimeUpdated(formattedTime));
                }
            }
        }, 0, 1, TimeUnit.SECONDS); 
    }
    

    /**
     * methode permettant de mettre en pause le jeu
     * @throws IllegalStateException leve une exception si le jeu n'est pas encore demarré ou le jeu a déja été terminé
     */
     public void pauseGame() throws IllegalStateException{

        if(timer==null|| timer.isShutdown()) {
            throw new IllegalStateException("impossible de mettre en pause le jeu car le jeu n'est pas encore demarré ou le jeu a déja été terminé");
        }
        isPaused = true;        
        
    }

    /**
     *  methode permettant de reprendre le jeu
     * @throws IllegalStateException leve une exception si le jeu n'est pas encore demarré ou le jeu a déja été terminé
     */
    public void ResumeGame() {
       if(timer==null|| timer.isShutdown()) {
            throw new IllegalStateException("impossible de reprendre le jeu car le jeu n'est pas encore demarré ou le jeu a déja été terminé");
        }

        isPaused = false;
    }

    /**
     * methode permettant d'arrêter le timer du jeu
     * cette methode devra etre  obligatoirement appelé à la fin du jeu
     * 
     * @throws Exception
     */
    public void stopGame() throws Exception {
        if (timer != null && !timer.isShutdown()) {
            timer.shutdown(); // Arrête proprement
            if (!timer.awaitTermination(3, TimeUnit.SECONDS)) {
                timer.shutdownNow(); // Forcer l'arrêt après attente
            }
        }
        updateDate();
        calculateProgressRate();
        saveGame();
    }
    

    /**
     * cette methode permet de mettre à jour le listener du temps
     * cette methode devra etre appelé avant de demarrer le timer
     * @param listener represente l'interface d'ecoute du temps
     */
     public void setGameTimeListener(GameTimeListener listener) {
        this.timeListener = listener;
    }

    /**
     * cette methode permet de retourner le profil du jeu
     * @return
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     *  cette methode retourne la grille du jeu
     *  permet donc d'acceder à toutes les methode en lecture seules de la grilles
     * 
     * @return
     */
    public Grid getGrid() {
        return grid;
    }


   /**
    * <p>cette methode permet d'executé une action pour une partie du jeu</p>
    * <p>une action est unique à la même instance de jeu auquel elle doit être appliqué </p>
    * cette methode sauvegarde automatique l'action  qui est ajouté dans une partie
    *
    * @param e  represente l'action à effectuer dans le jeux
    * <p> l'action a effectué devra etre construite avec la grille qui sera utlisé dans la partie
    *</p> 
    *<pre>
     {@code
      //Exemple
      //creation d'une partie de jeux avec la grille n°2 et un nouveau profil pierre
      Game g=new Game(DBManager.getGrid(2),new Profile("pierre"));
      //creation de l'action pour modifier la cellule
      Action a=new NumberCellAction(g,1,2,4,g.getGrid().getCell(1,2).getNumber());
      //execution de l'action
       g.excuteAction(a);
      }
    * </pre>
    * @see Action
    * @see NumberCellAction
    * @see AnnotationCellAction
    * @return la même instance du jeux  après appliquation de  la modification
    * @throws IllegalStateException leve une exception si le jeu est en pause
    * @throws IllegalArgumentException leve une exception si l'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu
    */
    public Game executeAction(Action e) throws IllegalStateException,IllegalArgumentException,Exception{

        if(this!=e.game){
            throw new IllegalArgumentException("l'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu");
        }
        if(isPaused){
            throw new IllegalStateException("auncune action ne peut etre effectuée car le jeu est en pause");
        }
        //on supprime les actions qui sont après l'index courant
        while (actions.size() > currentIndex + 1) {
            histoActions+="supression de l'action "+(currentIndex+ (actions.size()-currentIndex)+1)+" : "+actions.getLast()+"\n";
            actions.removeLast();
           

        }
         e.doAction();
         actions.add(e);
         histoActions+="Action "+(currentIndex+2)+" : "+e.toString()+"\n";
         currentIndex++;
         updateGame();
         return this;
    }

    /**
     * cette methode permet d'annuler la derniere action effectuée
     * 
     * @throws IllegalStateException leve une exception si aucune action n'a été effectuée ou si le jeu est en pause
     */
    public void undoAction() throws IllegalStateException{
        if (currentIndex < 0) 
           throw new IllegalStateException("impossible d'annuler l'action car aucune action n'a été effectuée");
        if(isPaused)
            throw new IllegalStateException("auncune action ne peut etre effectuée car le jeu est en pause ou n'a pas encore été demarré");

            actions.get(currentIndex).undoAction();
            histoActions+="Annulation de l'action "+(currentIndex+1)+" : "+actions.get(currentIndex)+"\n";
            currentIndex--;
    }

    /**
     * cette methode permet de refaire la derniere action annulée
     * 
     * @throws IllegalStateException leve une exception si aucune action n'a été annulée ou si le jeu est en pause
     */  
    public void redoAction() throws IllegalStateException{
        if (currentIndex >= actions.size() - 1) 
            throw new IllegalStateException("impossible de refaire l'action car aucune action n'a été annulée");
        if(isPaused)
            throw new IllegalStateException("auncune action ne peut etre effectuée car le jeu est en pause ou n'a pas encore été demarré");
            currentIndex++;
            histoActions+="Refaire de l'action "+(currentIndex+1)+" : "+actions.get(currentIndex)+"\n";
            actions.get(currentIndex).doAction();
            
    }


     /**
      * methode permettant de sauvegarder le jeu
      * @throws Exception
      */
     private void saveGame() throws Exception{

        DBManager.saveGame(this);
     }

   /**
    * 
    * @return le score de la partie
    */
    public int getScore() {
        return score;
    }

    /**
     * cette methode permet de retourner l'historique des actions effectuées
     * 
     * @return une chaine representant l'historique des actions effectuées
     */
    public String getHistoActions() {
        return histoActions;        
    }

    /**
     *  cette methode permet d'ajouter une valeur à une case du jeu 
     * 
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente la valeur à ajouter
     * @return la même instance du jeux  après appliquation de  la modification
     * @throws IllegalStateException leve une exception si le jeu est en pause
     * @return la même instance du jeu  après appliquation de  la modification
     * 
     */
    public Game addNumber(int x,int y,int value) throws IllegalStateException,Exception{


        if(isPaused){
            throw new IllegalStateException("auncune action ne peut etre effectuée car le jeu est en pause");
        }

            Action a=new NumberCellAction(this, x, y, value, grid.getCell(x,y).getNumber());
            a.doAction();
            actions.add(a);
            ++currentIndex;
            histoActions+="Action "+(currentIndex+1)+" : "+a.toString()+"\n";
            updateGame();
            return this;
    }

     /**
     *  cette methode permet d'ajouter une annotation  à une case du jeu 
     * 
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annoatation à ajouter
     * 
     * @return la même instance du jeu  après appliquation de  la modification
     * 
     * @throws IllegalStateException leve une exception si le jeu est en pause
     */
    public  Game addAnnotation(int x,int y, int value) throws IllegalStateException,Exception{

        if(isPaused){
            throw new IllegalStateException("auncune action ne peut etre effectuée car le jeu est en pause");
        } 
        Action a=new AnnotationCellAction(this, x, y, value);
        a.doAction();
        actions.add(a);
        ++currentIndex;
        histoActions+="Action "+(currentIndex+1)+" : "+a.toString()+"\n";
        updateGame();
        return this;
    }

    /**
     *  cette methode permet de mettre à jour la date de modiication du jeu
     */
    private void updateDate(){
     lastModifDate=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
    }

    /**
     * cette methode permet d'obtenir la date de creation de jeu
     * 
     * @return retourne la chaine correspondant 
     *  ex : "dd-MMM-yyyy HH:mm"
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * cette methode permet d'obtenir la date de la  dernierière modification  de jeu
     * 
     * @return retourne la chaine correspondant 
     *  ex : "dd-MMM-yyyy HH:mm"
     */
    public String getLastModifDate() {
        return lastModifDate;
    }

    /**
     *  cette methode permet de mettre à jour le jeu
     *  et de le sauvegarder
     * @throws Exeception
     */
    private void updateGame(){
        updateDate();
        calculateProgressRate();


    }


    /**
     * cette methode retourne toutes les actions effectués sous formes de json
     * @return
     */
    public String JsonEncodeActionsGame(){

        return ActionManagerApply.serializeList(actions);
    }

    /**
     * cette permet de calculer le taux de pogression du jeux
     */
    private void calculateProgressRate(){
       progressRate= (((double) (grid.getNumberFlexCell()-grid.evaluate().size()))/grid.getNumberFlexCell())*100;
    }

    /**
     *  cette methode retourne le taux de progression de la partie en pourcentage
     *  le taux de progression represente le pourcentage de case resolue dans la grille
     * 
     * @return le  pourcentage des cases réuissits
     */
    public double getProgressRate() {
        return progressRate;
    }



    public static void main(String[] args) {

        try{
            Game g = new Game(DBManager.getGrid(2), new Profile("jaques"));
            g.startGame();
            
            // Attendre et voir le timer avancer
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                System.out.println("Temps écoulé : " + g.getSringtElapsedTime());
            }
            
            g.stopGame();
            
        }catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
         }
    }
} 
