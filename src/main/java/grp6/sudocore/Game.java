package grp6.sudocore;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import grp6.sudocore.SudoTypes.GameState;
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
public final class Game {


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
    protected final Grid grid;

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
      private volatile long elapsedTime; 

      /**
       *  represente le timer du jeu(processus qui permettra le comptage)
       */
      private ScheduledExecutorService timer;

      /**
       *  represente l'inteface d'ecoute du timer
       */
      private GameTimeListener timeListener;


      /***
       * represente l'index courant de la liste des actions
       */
      private int currentIndex;

      /**
       *  represente l'historique des actions effectuées
       */
      private String histoActions=new String();

        /**
         * represente l'etat du jeu
         */
      private GameState gameState;

    //=================constructeur================

    /**
     * constructeur de la classe Game
     * @param grid respresente la grille du jeu
     * @param profile represente le profile du jeu
     * 
     * @throws leve une exeception en cas d'erreur de connection à la base de donnée
     */
    public Game(Grid grid,Profile profile)throws SQLException{
        this.id=DBManager.getLastIdGame()+1;
        this.grid=grid.clone();

        this.profile=profile;
        this.actions=new LinkedList<Action>();
        this.createdDate=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        this.lastModifDate=new String(this.createdDate);
        calculateProgressRate();
        this.elapsedTime = 0; 
        currentIndex=-1;
        gameState=GameState.NOT_STARTED;
        score=0;
        this.timer = Executors.newScheduledThreadPool(1);



    }
    /**
     * cette methode permet de recommencer une partie en renitialisant la grille
     * et en supprimant les actions effectuées
     */
    public void restartGame(){
        this.actions.clear();
        this.histoActions="";
        this.lastModifDate=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        calculateProgressRate();
        this.elapsedTime = 0; 
        currentIndex=-1;
        score=0;
        this.grid.resetGrid();
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
        gameState=GameState.NOT_STARTED;
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
        try {

            if(gameState==GameState.IN_PROGRESS){
                throw new IllegalStateException("le jeu est déjà en cours");
            }

            if (timer == null || timer.isShutdown()) {
                this.timer = Executors.newScheduledThreadPool(1); 
            }
            gameState=GameState.IN_PROGRESS;
            startTimer();
            
        } catch (IllegalStateException e) {
              System.err.println(e.getMessage());
        }
         catch (IllegalArgumentException e) {
             System.err.println("impossible de lancer le timer : "+e.getMessage());
         }
        
        
        
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
     *  cette methode retourne le temps ecoulé 
     * @return
     */
    public synchronized long getElapsedTime() {
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
                elapsedTime++; // Augmente le temps écoulé
                if (timeListener != null) {
                    String formattedTime = getSringtElapsedTime();
                    Platform.runLater(() -> timeListener.onTimeUpdated(formattedTime));
                }
        }, 0, 1, TimeUnit.SECONDS); 
    }
    

    /**
     * methode permettant de mettre en pause le jeu
     * @throws IllegalStateException leve une exception si le jeu n'est pas encore demarré ou le jeu a déja été terminé
     */
     public void pauseGame() throws IllegalStateException{
      try {
         if(gameState==GameState.NOT_STARTED){
                throw new IllegalStateException("le jeu n'est pas encore demarré");
          }
          if(gameState==GameState.FINISHED){
                throw new IllegalStateException("le jeu a déja été terminé");
          } 
          
         if(gameState==GameState.PAUSED){
                throw new IllegalStateException("le jeu est déjà en pause");
          }
             if (timer != null && !timer.isShutdown()) {
               timer.shutdown(); 
            if (!timer.awaitTermination(3, TimeUnit.SECONDS)) {
                timer.shutdownNow(); // Force l'arrêt après 4 secondes attente
            }
          gameState=GameState.PAUSED;
          timer.shutdown();
        } 
        }catch (Exception e) {
        // TODO: handle exception
        System.err.println(e.getMessage());
      }
          
        
    }

    /**
     *  methode permettant de reprendre le jeu
     * @throws IllegalStateException leve une exception si le jeu n'est pas encore demarré ou le jeu a déja été terminé
     */
    public void resumeGame() throws IllegalStateException{
        try {
            if(gameState==GameState.NOT_STARTED){
                throw new IllegalStateException("le jeu n'est pas encore demarré");
            }
            if(gameState==GameState.FINISHED){
                throw new IllegalStateException("le jeu a déja été terminé");
            } 
            if(gameState==GameState.IN_PROGRESS){
                throw new IllegalStateException("le jeu est déjà en cours");
            }
            startTimer();
            gameState=GameState.IN_PROGRESS;
        } catch (Exception e) {
           System.err.println(e.getMessage());
        }
    }

    /**
     * methode permettant d'arrêter le timer du jeu
     * cette methode devra etre  obligatoirement appelé à la fin du jeu
     * 
     * @throws SQLException leve une exception en cas d'erreur de connection à la base de donnée
     * @throws InterruptedException leve une exception en cas d'erreur d'interruption du timer
     */
    public void stopGame() throws SQLException,InterruptedException {

        try {

            if (timer != null && !timer.isShutdown()) {
                timer.shutdown(); // Arrête proprement
                if (!timer.awaitTermination(4, TimeUnit.SECONDS)) {
                    timer.shutdownNow(); // Force l'arrêt après 4 secondes attente
                }
            }
            saveGame();
            
        } catch (InterruptedException e) {
            // TODO: handle exception
             System.err.println("le timer est déjà arrêté "+e.getMessage());
             saveGame();
        }
        catch(SQLException e){
            System.err.println("Erreur de sauvegarde du jeu  "+e.getMessage());
        }
       
        
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
     *  cette methode retourne  une copie de la grille du jeu
     *  permet donc d'acceder à toutes les methode en lecture seules de la grilles
     * 
     * @return
     */
    public Grid getGrid() {
        return grid.clone();
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
    public Game executeAction(Action action) throws IllegalStateException,IllegalArgumentException{

        try {
            
        if(this!=action.game){
            throw new IllegalArgumentException("l'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu");
        }
        if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
            throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
        }
        //on supprime les actions qui sont après l'index courant
        while (actions.size() > currentIndex + 1) {
            histoActions+="supression de l'action "+(currentIndex+ (actions.size()-currentIndex)+1)+" : "+actions.getLast()+"\n";
            actions.removeLast();
           

        }
         action.doAction();
         actions.add(action);
         histoActions+="Action "+(currentIndex+2)+" : "+action.toString()+"\n";
         currentIndex++;
        
        if (currentIndex > 0) {
            int indexTemp = currentIndex - 1;
            if(!actions.get(indexTemp).getCorrect()){
                action.setCorrect(false);
            }
            else{
                action.setCorrect(grid.isCorrectCell(action.getRow(),action.getColumn()));
            }
        }
        else{
            System.out.println(grid.isCorrectCell(action.getRow(),action.getColumn()));
            action.setCorrect(grid.isCorrectCell(action.getRow(),action.getColumn()));
        }

        updateGame();

         return this;

        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
        }

    }

    /**
     * Vérifie si une action peut être annulée (undo).
    * @return true si undo est possible, sinon false.
    */
    public boolean canUndo() {
        return currentIndex >= 0;  
    }

    /**
    * Vérifie si une action annulée peut être refaite (redo).
    * @return true si redo est possible, sinon false.
    */
    public boolean canRedo() {
        return currentIndex < actions.size() - 1; 
    }


    /**
    * Cette méthode permet d'annuler la dernière action effectuée.
    *
    * @throws IllegalStateException si aucune action n'a été effectuée ou si le jeu est en pause ou si aucune action n'a été effectué precedement.
    */
    public void undoAction() throws IllegalStateException {

        try {

            if (!canUndo()) {
                throw new IllegalStateException("Impossible d'annuler l'action car aucune action n'a été effectuée.");
             }
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
            }
    
    
            // Vérifie si la dernière action est une ActionCell
            if (actions.get(currentIndex) instanceof ActionCell) {
                // Si oui, on l'annule et on s'arrête immédiatement
                actions.get(currentIndex).undoAction();
                histoActions += "Annulation de l'action " + (currentIndex + 1) + " : " + actions.get(currentIndex) + "\n";
                currentIndex--;

                System.out.println("Je suis undo : \n" + histoActions);
                return;
            }
    
            // Sinon, on annule toutes les actions précédentes jusqu'à la première ActionCell trouvée
            while (canUndo()) {
                 Action action = actions.get(currentIndex);
                 action.undoAction();
                 histoActions += "Annulation de l'action " + (currentIndex + 1) + " : " + action + "\n";
                currentIndex--;
            
                // Si on trouve une ActionCell, on s'arrête immédiatement
                if (action instanceof ActionCell) {
                    break;
                }
             }
    
            
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }
    }

    /**
    * Cette méthode permet de rétablir les actions annulées.
    * Si l'action suivante est une `ActionCell`, elle seule est refaite.
    * Sinon, toutes les actions suivantes sont refaites jusqu'à la première `ActionCell` trouvée.
    *
    * @throws IllegalStateException si aucune action n'a été annulée ou si le jeu est en pause.
    */
    public void redoAction() throws IllegalStateException {

        try {
            if (!canRedo()) {
                throw new IllegalStateException("Impossible de refaire l'action car aucune action n'a été annulée.");
            }
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
            }
    
            // Vérifier si l'action suivante est une ActionCell
            if (actions.get(currentIndex + 1) instanceof ActionCell) {
             // Si oui, on l'exécute et on s'arrête immédiatement
                currentIndex++;
                actions.get(currentIndex).doAction();
                histoActions += "Refaire de l'action " + (currentIndex + 1) + " : " + actions.get(currentIndex) + "\n";
                
                System.out.println("Je suis redo : \n" + histoActions);
                
                return;
            }
    
            // Sinon, on refait toutes les actions suivantes jusqu'à la première ActionCell trouvée
            while (canRedo()) {
                currentIndex++;
                Action action = actions.get(currentIndex);
                action.doAction();
                histoActions += "Refaire de l'action " + (currentIndex + 1) + " : " + action + "\n";
                
                // Si on trouve une ActionCell, on s'arrête immédiatement
                if (action instanceof ActionCell) {
                    break;
                }
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }
        

    
    }



     /**
      * methode permettant de sauvegarder le jeu
      * @throws SQLException leve une exception en cas d'erreur de connection à la base de donnée
      */
     private void saveGame() throws SQLException{

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
     * @throws IllegalStateException leve une exception si le jeu se trouve dans un eta incompatible à cette methode
     * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
     *
     * @return la même instance du jeu  après appliquation de  la modification
     * 
     */
    public Game addNumber(int x,int y,int value) throws IllegalStateException,NoEditableCellExeception{

        try {
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
            }
    
                Action a=new NumberCellAction(this, x, y, value, grid.getCell(x,y).getNumber());
                
                return executeAction(a);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
        }
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
     * @throws IllegalStateException leve une exception si le jeu se trouve dans un eta incompatible à cette methode
     *  @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
     */
    public  Game addAnnotation(int x,int y, int value) throws IllegalStateException,NoEditableCellExeception{

        try {
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
            } 
            Action a=new AnnotationCellAction(this, x, y, value);
           
            return executeAction(a);
            
        } catch (Exception e) {
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
        }

       
    }

  /**
   * methode permettant de supprimer un nombre dans une cellule
   * @param x represente la position x de la case dans la grille
    * @param y represente la position Y de la case dans la grille
    * @param value represente l'annoatation à ajouter
     * 
     * @return la même instance du jeu  après appliquation de  la modification
     * 
     * @throws IllegalStateException leve une exception si le jeu se trouve dans un eta incompatible à cette methode
     * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
   */
    public Game removeNumber(int x, int y) throws IllegalStateException,NoEditableCellExeception{

          try {
             return addNumber(x, y, 0);
          } catch (Exception e) {
            // TODO: handle exception
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
          }
    }
    /**
     * cette methode permet de supprimer une annotation  à une case du jeu
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annoatation à supprimer
     * @return la même instance du jeu  après appliquation de  la modification
     * @throws IllegalStateException
     * @throws NoEditableCellExeception
     */
    public Game removeAnnotation(int x, int y, int value) throws IllegalStateException,NoEditableCellExeception{
        try {
            return executeAction(new AnnotationRemoveCellAction(this, x, y, value));
        } catch (Exception e) { 
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
        }
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
     * @return le  pourcentage des cases réussits
     */
    public double getProgressRate() {
        return progressRate;
    }

    /**
     * cette methode permet de retourner l'etat du jeu
     * @return
     */
    public GameState getGameState() {
        return gameState;
    }

    public Action getLastAction() {
        System.out.println("Current : " + currentIndex);
        if(currentIndex >= 0)
            return actions.get(currentIndex);
        return null;
    }

    /**
     * Permet d'évaluer la grille
     * @return Liste des erreurs ([ligne, colonne])
     */
    public List<int[]> evaluate() {
        return grid.evaluate();
    }

    public static void main(String[] args) {

        try{
            Game g = new Game(DBManager.getGrid(2), new Profile("jaques"));
            g.startGame();
            
           
             DBManager.deleteAllGamesForProfile("jaques");
            g.stopGame();
            
        }catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
         }
    }
} 
