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
 * Cette classe gere une partie de sudoku. Une partie est composee d'une grille
 * de depart et d'un profil. Elle permet de gerer les actions effectuees par le joueur,
 * de sauvegarder les actions, de gerer le timer, le score, le taux de progression,
 * et l'historique des actions.
 * 
 * Pour commencer une partie, il faut appeler la methode startGame. Pour mettre en pause 
 * une partie, il faut appeler pauseGame. Pour reprendre une partie, il faut appeler 
 * resumeGame, et pour terminer une partie, il faut appeler stopGame.
 * 
 * @author DE THESE Taise
 * 
 * @see Grid
 * @see Profile
 * @see Action
 * @see ActionCell
 * @see NumberCellAction
 * @see AnnotationCellAction
 * @see AnnotationRemoveCellAction
 * @see GameTimeListener
 * @see DBManager
 * @see SudoTypes
 * @see GameState
 */
public final class Game {

    // ================== Variables d'instance =================
    /**
     * Profil du joueur
     */
    private Profile profile;

    /**
     * Identifiant du jeu
     */
    private final long id;

    /**
     * Grille du jeu
     */
    protected final Grid grid;

    /**
     * Liste des actions effectuees
     */
    private List<Action> actions;

    /**
     * Date de creation du jeu
     */
    private final String createdDate;

    /**
     * Score du jeu
     */
    private int score;

    /**
     * Date de la derniere modification
     */
    private String lastModifDate;

    /**
     * Taux de progression du jeu en pourcentage
     */
    private double progressRate;

    // ================== Variables pour le timer du jeu =================
    /**
     * Temps ecoule en secondes
     */
    private volatile long elapsedTime;

    /**
     * Timer du jeu (permet le comptage)
     */
    private ScheduledExecutorService timer;

    /**
     * Interface d'ecoute du timer
     */
    private GameTimeListener timeListener;

    /**
     * Index courant dans la liste des actions
     */
    private int currentIndex;

    /**
     * Historique des actions effectuees
     */
    private String histoActions = new String();

    /**
     * Etat du jeu
     */
    private GameState gameState;

// ================== Constructeurs ==================

    /**
     * Constructeur de la classe Game
     * @param grid Grille du jeu
     * @param profile Profil du joueur
     * @throws SQLException Si une erreur se produit lors de la connexion à la base de donnees
     */
    public Game(Grid grid, Profile profile) throws SQLException {
        this.id = DBManager.getLastIdGame() + 1;
        this.grid = grid.clone();
        this.profile = profile;
        this.actions = new LinkedList<Action>();
        this.createdDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        this.lastModifDate = new String(this.createdDate);
        calculateProgressRate();
        this.elapsedTime = 0;
        currentIndex = -1;
        this.gameState = GameState.NOT_STARTED;
        score = 1000;
        this.timer = Executors.newScheduledThreadPool(1);
    }

    /**
     * Redemarre une partie en reinitialisant la grille et en supprimant les actions effectuees
     */
    public void restartGame() {
        this.actions.clear();
        this.histoActions = "";
        this.lastModifDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
        calculateProgressRate();
        this.elapsedTime = 0;
        currentIndex = -1;
        score = 0;
        this.grid.resetGrid();
        startGame();
        try {
            saveGame();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'identifiant du jeu
     * @return Identifiant du jeu
     */
    public long getId() {
        return id;
    }

    /**
     * Constructeur de la classe Game utilise pour charger un jeu depuis la base de donnees
     * @param id Identifiant du jeu
     * @param grid Grille du jeu
     * @param profile Profil du joueur
     * @param createdDate Date de creation du jeu
     * @param lastModifDate Date de la derniere modification du jeu
     * @param score Score du jeu
     * @param elapsedTime Temps ecoule
     * @param gameState Etat du jeu
     * @param progressRate Taux de progression du jeu
     */
    protected Game(long id, Grid grid, Profile profile, String createdDate, String lastModifDate, int score,
                    long elapsedTime, String gameState, double progressRate) {
        this.id = id;
        this.grid = grid;
        this.profile = profile;
        this.createdDate = createdDate;
        this.lastModifDate = lastModifDate;
        this.score = score;
        this.elapsedTime = elapsedTime;
        this.progressRate = progressRate;
        switch (gameState) {
            case "IN_PROGRESS":
                this.gameState = GameState.IN_PROGRESS;
                break;
            case "PAUSED":
                this.gameState = GameState.PAUSED;
                break;
            case "FINISHED":
                this.gameState = GameState.FINISHED;
                break;
            default:
                this.gameState = GameState.NOT_STARTED;
        }
        this.timer = Executors.newScheduledThreadPool(1);
    }
  
    /**
     * Applique les actions a la partie du jeu associee
     * @param actions Liste des actions a appliquer
     * @throws NoCompatibleActionGameException Si l'action n'est pas compatible avec le jeu
     * @throws NoPutNumberOnCellExeception Si l'action doit etre appliquee sur une cellule non modifiable
     * @throws IllegalStateException Si des actions ont deja ete effectuees
     */
    protected void applyActions(List<Action> actions) throws NoCompatibleActionGameException,
            NoPutNumberOnCellExeception, IllegalStateException {
        if (this.actions != null)
            throw new IllegalStateException("Impossible d'appliquer les actions car des actions ont deja ete effectuees");
        int index = 1;
        for (Action a : actions) {
            if (this != a.game) {
                throw new NoCompatibleActionGameException("L'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu");
            }
            a.doAction();
            histoActions += (index == actions.size()) ? "Action " + index + " : " + a.toString() + " (position actuelle)\n" : "Action " + index + " : " + a.toString() + "\n";
        }
        this.actions = actions;
        currentIndex = actions.size() - 1;
    }
  
    /**
     * Demarre le jeu
     * @throws Exception Si une erreur se produit
     */
    public void startGame() {
        try {
            if (gameState == GameState.IN_PROGRESS) {
                throw new IllegalStateException("Le jeu est deja en cours");
            }
            if (timer == null || timer.isShutdown()) {
                this.timer = Executors.newScheduledThreadPool(1);
            }
            gameState = GameState.IN_PROGRESS;
            startTimer();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Impossible de lancer le timer : " + e.getMessage());
        }
    }
  
      

        /**
     * Retourne le temps ecoule au format HH:mm:ss.
     * 
     * @return Le temps ecoule sous forme de chaine de caracteres au format HH:mm:ss.
     */
    public String getStringElapsedTime() {
        long seconds = elapsedTime;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    /**
     * Retourne le temps ecoule.
     * @return Le temps ecoule en secondes.
     */
    public synchronized long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Demarre le timer du jeu et gere l'interaction avec l'interface utilisateur.
     */
    private void startTimer() {
        if (timer == null || timer.isShutdown()) {
            this.timer = Executors.newScheduledThreadPool(1);
        }
    
        timer.scheduleAtFixedRate(() -> {
                elapsedTime++; // Augmente le temps ecoule
                if (timeListener != null) {
                    String formattedTime = getStringElapsedTime();
                    Platform.runLater(() -> timeListener.onTimeUpdated(formattedTime));
                }
        }, 0, 1, TimeUnit.SECONDS); 
    }
    

    /**
     * Met en pause le jeu.
     * @throws IllegalStateException Si le jeu n'est pas encore demarre ou deja termine.
     */
    public void pauseGame() throws IllegalStateException {
        try {
            if (gameState == GameState.NOT_STARTED) {
                throw new IllegalStateException("Le jeu n'est pas encore demarre");
            }
            if (gameState == GameState.FINISHED) {
                throw new IllegalStateException("Le jeu a deja ete termine");
            }
            if (gameState == GameState.PAUSED) {
                throw new IllegalStateException("Le jeu est deja en pause");
            }
            if (timer != null && !timer.isShutdown()) {
                timer.shutdown();
                if (!timer.awaitTermination(3, TimeUnit.SECONDS)) {
                    timer.shutdownNow(); // Force l'arret apres 4 secondes d'attente
                }
            }
            gameState = GameState.PAUSED;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Reprend le jeu apres une pause.
     * @throws IllegalStateException Si le jeu n'est pas encore demarre ou deja termine.
     */
    public void resumeGame() throws IllegalStateException {
        try {
            if (gameState == GameState.NOT_STARTED) {
                throw new IllegalStateException("Le jeu n'est pas encore demarre");
            }
            if (gameState == GameState.FINISHED) {
                throw new IllegalStateException("Le jeu a deja ete termine");
            }
            gameState = GameState.IN_PROGRESS;
            startTimer();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Arrete le timer du jeu. Cette operation doit etre effectuee a la fin du jeu.
     * 
     * @throws SQLException Si une erreur de connexion a la base de donnees se produit.
     * @throws InterruptedException Si une erreur d'interruption du timer se produit.
     */
    public void stopGame() throws SQLException, InterruptedException {
        try {
            if (timer != null && !timer.isShutdown()) {
                timer.shutdown(); // Arret propre
                if (!timer.awaitTermination(4, TimeUnit.SECONDS)) {
                    timer.shutdownNow(); // Force l'arret apres 4 secondes d'attente
                }
            }

            if (gameState != GameState.FINISHED) {
                gameState = GameState.IN_PROGRESS;
            }

            saveGame();
        } catch (InterruptedException e) {
            System.err.println("Le timer est deja arrete " + e.getMessage());
            gameState = GameState.IN_PROGRESS;
            saveGame();
        } catch (SQLException e) {
            System.err.println("Erreur de sauvegarde du jeu " + e.getMessage());
        }
    }

    /**
     * Met a jour le listener du temps. Cette operation doit etre effectuee avant de demarrer le timer.
     * @param listener L'interface d'ecoute du temps.
     */
    public void setGameTimeListener(GameTimeListener listener) {
        this.timeListener = listener;
    }

    /**
     * Retourne le profil du jeu.
     * @return Le profil du jeu.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Retourne une copie de la grille du jeu.
     * Cela permet d'acceder a toutes les methodes en lecture seule de la grille.
     * 
     * @return Une copie de la grille du jeu.
     */
    public Grid getGrid() {
        return grid.clone();
    }

    /**
     * Execute une action dans le jeu. L'action est unique pour chaque instance de jeu.
     * L'action sera automatiquement sauvegardee et appliquee.
     *
     * @param e L'action a effectuer dans le jeu.
     * @return L'instance du jeu apres modification.
     * @throws IllegalStateException Si le jeu est en pause.
     * @throws IllegalArgumentException Si l'action n'est pas compatible avec le jeu.
     */
    public Game executeAction(Action action) throws IllegalStateException, IllegalArgumentException {
        try {
            if (this != action.game) {
                throw new IllegalArgumentException("L'action n'est pas compatible avec le jeu, ou ne concerne pas ce jeu");
            }
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED || gameState == GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut etre effectuee car le jeu est en pause ou n'a pas encore ete demarre ou est deja termine.");
            }

            deleteActionsAfterCurrent();

            action.doAction();
            actions.add(action);
            histoActions += "Action " + (currentIndex + 2) + " : " + action.toString() + "\n";
            currentIndex++;

            updateGame();

            // Verifie si la grille est complete et correcte
            if (grid.isFinished()) {
                gameState = GameState.FINISHED;
            }

            saveGame();

            return this;
        } catch (Exception e) {
            System.err.println("Aucune action n'a ete effectuee : " + e.getMessage());
            return this;
        }
    }

    /**
     * Verifie si une action peut etre annulee.
     * @return true si undo est possible, sinon false.
     */
    public boolean canUndo() {
        return currentIndex >= 0;  
    }

    /**
     * Verifie si une action annulee peut etre refaite.
     * @return true si redo est possible, sinon false.
     */
    public boolean canRedo() {
        return currentIndex < actions.size() - 1; 
    }

    /**
     * Annule la derniere action effectuee.
     * 
     * @throws IllegalStateException Si aucune action n'a ete effectuee ou si le jeu est en pause.
     */
    public void undoAction() throws IllegalStateException {
        try {
            if (!canUndo()) {
                throw new IllegalStateException("Impossible d'annuler l'action car aucune action n'a ete effectuee.");
            }
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED || gameState == GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut etre effectuee car le jeu est en pause ou n'a pas encore ete demarre ou est deja termine.");
            }
    
            // Verifie si la derniere action est une ActionCell
            if (actions.get(currentIndex) instanceof ActionCell) {
                // Si oui, on l'annule et on s'arrete immediatement
                actions.get(currentIndex).undoAction();
                histoActions += "Annulation de l'action " + (currentIndex + 1) + " : " + actions.get(currentIndex) + "\n";
                currentIndex--;

                return;
            }
    
            // Sinon, on annule toutes les actions precedentes jusqu'a la premiere ActionCell trouvee
            while (canUndo()) {
                 Action action = actions.get(currentIndex);
                 action.undoAction();
                 histoActions += "Annulation de l'action " + (currentIndex + 1) + " : " + action + "\n";
                currentIndex--;
            
                // Si on trouve une ActionCell, on s'arrete immediatement
                if (action instanceof ActionCell) {
                    break;
                }
             }
    
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Cette methode permet de retablir les actions annulees.
     * Si l'action suivante est une `ActionCell`, elle seule est refaite.
     * Sinon, toutes les actions suivantes sont refaites jusqu'a la premiere `ActionCell` trouvee.
     *
     * @throws IllegalStateException Si aucune action n'a ete annulee ou si le jeu est en pause.
     */
    public void redoAction() throws IllegalStateException {

        if (!canRedo()) {
            throw new IllegalStateException("Impossible de refaire l'action car aucune action n'a ete annulee.");
        }
        if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED || gameState == GameState.PAUSED) {
            throw new IllegalStateException("Aucune action ne peut etre effectuee car le jeu est en pause ou n'a pas encore ete demarre ou est deja termine.");
        }

        // Verifier si l'action suivante est une ActionCell
        if (actions.get(currentIndex + 1) instanceof ActionCell) {
            // Si oui, on l'execute et on s'arrete immediatement
            currentIndex++;
            actions.get(currentIndex).doAction();
            histoActions += "Refaire de l'action " + (currentIndex + 1) + " : " + actions.get(currentIndex) + "\n";

            return;
        }

        // Sinon, on refait toutes les actions suivantes jusqu'a la premiere ActionCell trouvee
        while (canRedo()) {
            currentIndex++;
            Action action = actions.get(currentIndex);
            action.doAction();
            histoActions += "Refaire de l'action " + (currentIndex + 1) + " : " + action + "\n";
            
            // Si on trouve une ActionCell, on s'arrete immediatement
            if (action instanceof ActionCell) {
                break;
            }
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
    
                Action a = new NumberCellAction(this, x, y, value, grid.getCell(x,y).getNumber());
                
                return executeAction(a);
        } catch (Exception e) {
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
     * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
     */
    public  Game addAnnotation(int x,int y, int value) throws IllegalStateException,NoEditableCellExeception{

        try {
            if (gameState == GameState.NOT_STARTED || gameState == GameState.FINISHED||gameState==GameState.PAUSED) {
                throw new IllegalStateException("Aucune action ne peut être effectuée car le jeu est en pause ou n'a pas encore été démarré ou est est déjà terminé.");
            } 
            Action a = new AnnotationCellAction(this, x, y, value, grid.getCell(x,y).getLastAnnotation());
           
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
            return executeAction(new AnnotationRemoveCellAction(this, x, y, value, value));
        } catch (Exception e) { 
            System.err.println("aucune action n'a été effectué :"+e.getMessage());
            return this;
        }
    }

        /**
     * Met a jour la date de modification du jeu.
     */
    private void updateDate() {
        lastModifDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
    }

    /**
     * Permet d'obtenir la date de creation du jeu.
     * 
     * @return La chaine correspondant a la date de creation, par exemple "dd-MMM-yyyy HH:mm".
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Permet d'obtenir la date de la derniere modification du jeu.
     * 
     * @return La chaine correspondant a la date de la derniere modification, par exemple "dd-MMM-yyyy HH:mm".
     */
    public String getLastModifDate() {
        return lastModifDate;
    }

    /**
     * Met a jour le jeu et effectue une sauvegarde.
     */
    private void updateGame() {
        updateDate();
        calculateProgressRate();
    }

    /**
     * Retourne toutes les actions effectuees sous forme de JSON.
     * @return Liste des actions serializees en JSON.
     */
    public String JsonEncodeActionsGame() {
        return ActionManagerApply.serializeList(actions);
    }

    /**
     * Calcule le taux de progression du jeu.
     * Le taux est calcule en fonction du nombre de cases correctes sur le nombre total de cases flexibles.
     */
    private void calculateProgressRate() {
        progressRate = (((double) grid.nbCorrectCells()) / ((double) grid.getNumberFlexCell())) * 100;
        progressRate = Math.round(progressRate * 100.0) / 100.0;
    }

    /**
     * Reduit le score de la partie en fonction du mode.
     * @param mode Indique si le joueur perd des points pour avoir verifie sa grille ou utilise l'aide.
     */
    public void decreaseScore(String mode) {
        if (mode.equals("check")) {
            this.score = this.score - 50;
        } else {
            this.score = this.score - 20;
        }
    }

    /**
     * Retourne le taux de progression de la partie en pourcentage.
     * Le taux de progression represente le pourcentage de cases resolues dans la grille.
     * 
     * @return Le pourcentage des cases correctes.
     */
    public double getProgressRate() {
        return progressRate;
    }

    /**
     * Retourne l'etat actuel du jeu.
     * @return L'etat actuel du jeu.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Permet de definir l'etat du jeu.
     * @param gameState Nouvel etat du jeu.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Retourne la derniere action effectuee.
     * @return La derniere action si elle existe, sinon null.
     */
    public Action getLastAction() {
        if (currentIndex >= 0)
            return actions.get(currentIndex);
        return null;
    }

    /**
     * Permet d'evaluer la grille en listant les erreurs sous forme de tableaux [ligne, colonne].
     * @return Liste des erreurs dans la grille.
     */
    public List<int[]> evaluate() {
        return grid.evaluate();
    }

    /**
     * Supprime toutes les actions effectuees apres l'action courante.
     */
    public void deleteActionsAfterCurrent() {
        while (actions.size() > currentIndex + 1) {
            histoActions += "Suppression de l'action " + (currentIndex + (actions.size() - currentIndex) + 1) + " : " + actions.getLast() + "\n";
            actions.removeLast();
        }
    }

    /**
     * Supprime toutes les actions effectuees sur une cellule specifique.
     * @param x La ligne de la cellule.
     * @param y La colonne de la cellule.
     */
    public void deleteActionsOfCell(int x, int y) {
        int size = actions.size();
        for (int i = 0; i < size; i++) {
            Action a = actions.get(i);
            if (a instanceof NumberCellAction nca && nca.getRow() == x && nca.getColumn() == y) {
                nca.undoAction();
            }
            if (a instanceof AnnotationCellAction aca && aca.getRow() == x && aca.getColumn() == y) {
                aca.undoAction();
            }
        }
    }

    /**
     * Retourne l'index actuel de l'action.
     * @return L'index de l'action courante.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Retourne l'action a l'index specifie.
     * @param index L'index de l'action.
     * @return L'action a l'index specifie.
     */
    public Action getAction(int index) {
        return actions.get(index);
    }

    /**
     * Retourne la liste de toutes les actions effectuees.
     * @return La liste des actions.
     */
    public List<Action> getActions() {
        return actions;
    }

    /**
     * Vide la liste des actions.
     */
    public void clearActions() {
        actions.clear();
    }


    public static void main(String[] args) {

        try{
            Game g = new Game(DBManager.getGrid(2), new Profile("jaques"));
            g.startGame();
            
           
             DBManager.deleteAllGamesForProfile("jaques");
            g.stopGame();
            
        }catch (Exception e) {
            System.err.println(e.getMessage());
         }
    }
} 
