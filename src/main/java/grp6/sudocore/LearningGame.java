package grp6.sudocore;
import grp6.syshelp.Technique;

import java.util.*;
import java.sql.SQLException;

public class LearningGame {

    private Game game;

    private Grid solvedGrid;

    private  Technique tech;

    public LearningGame(Profile profile, Technique tech) throws  SQLException {
        this.solvedGrid = tech.getSolvedGrid();
        game = new Game(tech.getGrid(), profile);
        this.tech = tech;
    }

    /**
     * 
     * @return
     */
    public Profile getProfile() {
        return game.getProfile();
    }

    /**
     * Retourne l'identifiant du jeu
     * @return Identifiant du jeu
     */
    public long getId() {
        return game.getId();
    }

    /**
     * Relance une partie
     */
    public void restartGame() {
        game.restartGame();
    }

    /**
     * Applique les actions a la partie du jeu associee
     * @param actions Liste des actions a appliquer
     * @throws NoCompatibleActionGameException Si l'action n'est pas compatible avec le jeu
     * @throws NoPutNumberOnCellExeception Si l'action doit etre appliquee sur une cellule non modifiable
     * @throws IllegalStateException Si des actions ont deja ete effectuees
     */
    protected void applyActions(List<Action> actions) throws NoCompatibleActionGameException, NoPutNumberOnCellExeception, IllegalStateException {
        game.applyActions(actions);
    }

    /**
     * Demarre le jeu
     * @throws Exception Si une erreur se produit
     */
    public void startGame() {
        game.startGame();
    }

    /**
     * Retourne le temps ecoule au format HH:mm:ss.
     * 
     * @return Le temps ecoule sous forme de chaine de caracteres au format HH:mm:ss.
     */
    public String getStringElapsedTime() {
        return game.getStringElapsedTime();
    }

    /**
     * Retourne le temps ecoule.
     * @return Le temps ecoule en secondes.
     */
    public synchronized long getElapsedTime() {
        return game.getElapsedTime();
    }

    /**
     * Met en pause le jeu.
     * @throws IllegalStateException Si le jeu n'est pas encore demarre ou deja termine.
     */
    public void pauseGame() throws IllegalStateException {
        game.pauseGame();
    }

    /**
     * Reprend le jeu apres une pause.
     * @throws IllegalStateException Si le jeu n'est pas encore demarre ou deja termine.
     */
    public void resumeGame() throws IllegalStateException {
        game.restartGame();
    }

    /**
     * Arrete le timer du jeu. Cette operation doit etre effectuee a la fin du jeu.
     * 
     * @throws SQLException Si une erreur de connexion a la base de donnees se produit.
     * @throws InterruptedException Si une erreur d'interruption du timer se produit.
     */
    public void stopGame() throws SQLException, InterruptedException {
        game.stopGame();
    }

    /**
     * Met a jour le listener du temps. Cette operation doit etre effectuee avant de demarrer le timer.
     * @param listener L'interface d'ecoute du temps.
     */
    public void setGameTimeListener(GameTimeListener listener) {
        game.setGameTimeListener(listener);
    }

    /**
     * Retourne une copie de la grille du jeu.
     * Cela permet d'acceder & la grille du jeu sans alterrer la partie en cours.
     * 
     * @return Une copie de la grille du jeu.
     */
    public Grid getGrid() {
        return game.getGrid();
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
        return game.executeAction(action);
    }

    /**
     * Verifie si une action peut etre annulee.
     * @return true si undo est possible, sinon false.
     */
    public boolean canUndo() {
        return game.canUndo();
    }

    /**
     * Verifie si une action annulee peut etre refaite.
     * @return true si redo est possible, sinon false.
     */
    public boolean canRedo() {
        return game.canRedo(); 
    }

    /**
     * Annule la derniere action effectuee.
     * 
     * @throws IllegalStateException Si aucune action n'a ete effectuee ou si le jeu est en pause.
     */
    public void undoAction() throws IllegalStateException {
        game.undoAction();
    }

    /**
     * Cette methode permet de retablir les actions annulees.
     * Si l'action suivante est une `ActionCell`, elle seule est refaite.
     * Sinon, toutes les actions suivantes sont refaites jusqu'a la premiere `ActionCell` trouvee.
     *
     * @throws IllegalStateException Si aucune action n'a ete annulee ou si le jeu est en pause.
     */
    public void redoAction() throws IllegalStateException {
        game.redoAction();
    }

    /**
     * cette methode permet de retourner l'historique des actions effectuees
     * 
     * @return une chaine representant l'historique des actions effectuees
     */
    public String getHistoActions() {
        return game.getHistoActions();        
    }

    /**
     *  cette methode permet d'ajouter une annotation  & une case du jeu 
     * 
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annoatation & ajouter
     * 
     * @return la meme instance du jeu  apres appliquation de  la modification
     * 
     * @throws IllegalStateException leve une exception si le jeu se trouve dans un eta incompatible & cette methode
     * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
     */
    public Game addAnnotation(int x,int y, int value) throws IllegalStateException, NoEditableCellExeception {
        return game.addAnnotation(x, y, value);
    }

    /**
    * methode permettant de supprimer un nombre dans une cellule
    * @param x represente la position x de la case dans la grille
    * @param y represente la position Y de la case dans la grille
    * @param value represente l'annotation & ajouter
    * 
    * @return la meme instance du jeu  apres appliquation de  la modification
    * 
    * @throws IllegalStateException leve une exception si le jeu se trouve dans un eta incompatible & cette methode
    * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
    */
    public Game removeNumber(int x, int y) throws IllegalStateException, NoEditableCellExeception {
        return game.removeNumber(x, y);
    }

    /**
     * cette methode permet de supprimer une annotation  & une case du jeu
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annoatation & supprimer
     * @return la meme instance du jeu  apres appliquation de  la modification
     * @throws IllegalStateException
     * @throws NoEditableCellExeception
     */
    public Game removeAnnotation(int x, int y, int value) throws IllegalStateException, NoEditableCellExeception {
        return game.removeAnnotation(x, y, value);
    }

    /**
     * Permet d'obtenir la date de creation du jeu.
     * 
     * @return La chaine correspondant a la date de creation, par exemple "dd-MMM-yyyy HH:mm".
     */
    public String getCreatedDate() {
        return game.getCreatedDate();
    }

    /**
     * Permet d'obtenir la date de la derniere modification du jeu.
     * 
     * @return La chaine correspondant a la date de la derniere modification, par exemple "dd-MMM-yyyy HH:mm".
     */
    public String getLastModifDate() {
        return game.getLastModifDate();
    }

    /**
     * Retourne l'etat actuel du jeu.
     * @return L'etat actuel du jeu.
     */
    public SudoTypes.GameState getGameState() {
        return game.getGameState();
    }

    /**
     * Permet de definir l'etat du jeu.
     * @param gameState Nouvel etat du jeu.
     */
    public void setGameState(SudoTypes.GameState gameState) {
        game.setGameState(gameState);
    }

    /**
     * Retourne la derniere action effectuee.
     * @return La derniere action si elle existe, sinon null.
     */
    public Action getLastAction() {
        return game.getLastAction();
    }

    /**
     * Supprime toutes les actions effectuees apres l'action courante.
     */
    public void deleteActionsAfterCurrent() {
        game.deleteActionsAfterCurrent();
    }

    /**
     * Supprime toutes les actions effectuees sur une cellule specifique.
     * @param x La ligne de la cellule.
     * @param y La colonne de la cellule.
     */
    public void deleteActionsOfCell(int x, int y) {
        game.deleteActionsOfCell(x, y);
    }

    /**
     * Retourne l'index actuel de l'action.
     * @return L'index de l'action courante.
     */
    public int getCurrentIndex() {
        return game.getCurrentIndex();
    }

    /**
     * Retourne l'action a l'index specifie.
     * @param index L'index de l'action.
     * @return L'action a l'index specifie.
     */
    public Action getAction(int index) {
        return game.getAction(index);
    }

    /**
     * Vide la liste des actions.
     */
    public void clearActions() {
        game.clearActions();
    }

    /**
     * Evalue si tous les nombres de la grille actuelle correspondent a ceux de la grille solution.
     *
     * @return {@code true} si tous les nombres sont corrects, {@code false} sinon.
     */
    private  boolean evaluateNum() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(game.getGrid().getCell(i, j).getNumber() != solvedGrid.getCell(i, j).getNumber()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Evalue si toutes les annotations de la grille actuelle correspondent a celles de la grille solution.
     *
     * @return {@code true} si toutes les annotations sont correctes, {@code false} sinon.
     */
    private boolean evaluateAnnot() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(!game.getGrid().getCell(i, j).getAnnotations().equals(solvedGrid.getCell(i, j).getAnnotations())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Evalue la validite de la grille en fonction de la technique utilisee.
     * 
     * - Si la technique utilisee est une technique basee sur un seul chiffre possible dans une case,
     *   la validation se fait sur les nombres de la grille.
     * - Sinon, l'evaluation se base sur les annotations.
     *
     * @return {@code true} si la grille est correcte selon la technique appliquee, {@code false} sinon.
     */
    public boolean evaluate() {
        if(
            tech.getName().equals("HiddenSingle") ||
            tech.getName().equals("LastCell") ||
            tech.getName().equals("LastNumber") ||
            tech.getName().equals("LastPossible") ||
            tech.getName().equals("NakedSingleton")
        ) {
            return evaluateNum();
        }
        return evaluateAnnot();
    }
}