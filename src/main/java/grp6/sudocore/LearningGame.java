package grp6.sudocore;
import grp6.sudocore.SudoTypes.ActionType;
import grp6.syshelp.Technique;

import java.util.*;
import java.sql.SQLException;

import grp6.intergraph.SudokuGrid;

/**
 * Classe LearningGame
 * Cette classe gere la logique d'un mode d'apprentissage pour le Sudoku, ou le joueur peut
 * appliquer des techniques de resolution pour completer une grille.
 * Elle permet de sauvegarder et d'evaluer les actions du joueur.
 * 
 * @author POUSSE Kilian
 * @see Game
 * @see Grid
 * @see Profile
 * @see Technique
 */
public class LearningGame {

    /**
     * Instance du jeu associee a cette session d'apprentissage
     */
    private static Game game;

    /**
     * Grille solution du Sudoku pour comparaison
     */
    private static Grid solvedGrid;

    /**
     * Technique appliquee pour l'apprentissage
     */
    private static Technique tech;

    /**
     * Constructeur de la classe LearningGame.
     * 
     * @param profile Profil du joueur
     * @param techni Technique utilisee pour resoudre la grille
     * @throws SQLException En cas d'erreur lors de la recuperation de la grille solution
     */
    public LearningGame(Profile profile, Technique techni) throws  SQLException {
        solvedGrid = techni.getSolvedGrid();
        game = new Game(techni.getGrid(), profile);
        tech = techni;
    }

    /**
     * Retourne le profil du joueur actuel.
     * @return profile du joueur actuel
     */
    public Profile getProfile() {
        return game.getProfile();
    }

    /**
     * Retourne l'identifiant du jeu
     * 
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
     * 
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
     * 
     * @return Le temps ecoule en secondes.
     */
    public synchronized long getElapsedTime() {
        return game.getElapsedTime();
    }

    /**
     * Met en pause le jeu.
     * 
     * @throws IllegalStateException Si le jeu n'est pas encore demarre ou deja termine.
     */
    public void pauseGame() throws IllegalStateException {
        game.pauseGame();
    }

    /**
     * Reprend le jeu apres une pause.
     * 
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
     * 
     * @param listener L'interface d'ecoute du temps.
     */
    public void setGameTimeListener(GameTimeListener listener) {
        game.setGameTimeListener(listener);
    }

    /**
     * Retourne une copie de la grille du jeu.
     * Cela permet d'acceder a la grille du jeu sans alterrer la partie en cours.
     * 
     * @return Une copie de la grille du jeu.
     */
    public static Grid getGrid() {
        return game.getGrid();
    }

    /**
     * Renvoie le jeu actuel
     * @return le jeu actuel
     */
    public Game getGame() {
        return game;
    }

    /**
     * Execute une action dans le jeu. L'action est unique pour chaque instance de jeu.
     * L'action sera automatiquement sauvegardee et appliquee.
     *
     * @param action L'action a effectuer dans le jeu.
     * @return L'instance du jeu apres modification.
     * @throws IllegalStateException Si le jeu est en pause.
     * @throws IllegalArgumentException Si l'action n'est pas compatible avec le jeu.
     */
    public Game executeAction(Action action) throws IllegalStateException, IllegalArgumentException {
        return game.executeAction(action);
    }

    /**
     * Verifie si une action peut etre annulee.
     * 
     * @return true si undo est possible, sinon false.
     */
    public boolean canUndo() {
        return game.canUndo();
    }

    /**
     * Verifie si une action annulee peut etre refaite.
     * 
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
     * Retablit les actions annulees.
     * Si l'action suivante est une `ActionCell`, elle seule est refaite.
     * Sinon, toutes les actions suivantes sont refaites jusqu'a la premiere `ActionCell` trouvee.
     *
     * @throws IllegalStateException Si aucune action n'a ete annulee ou si le jeu est en pause.
     */
    public void redoAction() throws IllegalStateException {
        game.redoAction();
    }

    /**
     * Renvoie l'historique des actions effectuees
     * 
     * @return une chaine representant l'historique des actions effectuees
     */
    public String getHistoActions() {
        return game.getHistoActions();        
    }

    /**
     * Ajoute une annotation a une case du jeu 
     * 
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annotation a ajouter
     * 
     * @return la meme instance du jeu apres application de la modification
     * 
     * @throws IllegalStateException leve une exception si le jeu se trouve dans un etat incompatible a cette methode
     * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
     */
    public Game addAnnotation(int x,int y, int value) throws IllegalStateException, NoEditableCellExeception {
        return game.addAnnotation(x, y, value);
    }

    /**
    * Supprime un nombre dans une cellule
    * @param x represente la position x de la case dans la grille
    * @param y represente la position Y de la case dans la grille
    * 
    * @return la meme instance du jeu  apres application de  la modification
    * 
    * @throws IllegalStateException leve une exception si le jeu se trouve dans un etat incompatible a cette methode
    * @throws NoEditableCellExeception leve une exception si la cellule n'est pas editable
    */
    public Game removeNumber(int x, int y) throws IllegalStateException, NoEditableCellExeception {
        return game.removeNumber(x, y);
    }

    /**
     * Supprime une annotation d'une case du jeu
     * 
     * @param x represente la position x de la case dans la grille
     * @param y represente la position Y de la case dans la grille
     * @param value represente l'annotation a supprimer
     * @return la meme instance du jeu apres application de la modification
     * @throws IllegalStateException exception
     * @throws NoEditableCellExeception exception
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
     * 
     * @param x La ligne de la cellule.
     * @param y La colonne de la cellule.
     */
    public void deleteActionsOfCell(int x, int y) {
        game.deleteActionsOfCell(x, y);
    }

    /**
     * Retourne l'index actuel de l'action.
     * 
     * @return L'index de l'action courante.
     */
    public int getCurrentIndex() {
        return game.getCurrentIndex();
    }

    /**
     * Retourne l'action a l'index specifie.
     * 
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
    private static boolean evaluateNum() {
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
     * Evalue si toutes les annotations de la grille actuelle correspondent à celles de la grille solution,
     * en ignorant l'ordre des annotations.
     *
     * @return {@code true} si toutes les annotations sont correctes, {@code false} sinon.
     */
    private static boolean evaluateAnnot() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Set<Integer> currentAnnotations = new HashSet<>();
                for (String annotation : SudokuGrid.getAnnotations(i, j)) {
                    currentAnnotations.add(Integer.valueOf(annotation));
                }
                Set<Integer> correctAnnotations = new HashSet<>(solvedGrid.getCell(i, j).getAnnotations());

                if (!currentAnnotations.equals(correctAnnotations)) {
                    System.out.println("grid : " + currentAnnotations);
                    System.out.println("solved : " + correctAnnotations);
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
    public static boolean evaluate() {
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

    /**
     * Evalue la derniere action effectuee dans la grille de Sudoku.
     * Cette methode verifie si la derniere modification (ajout de nombre ou annotation) est correcte.
     * 
     * @return true si l'action est correcte, false sinon.
     */
    public static boolean evaluateLastAction() {
        Action action = game.getLastAction();
        if (action == null) {
            return false;
        }
        
        boolean isCorrect = false;
        
        if (action instanceof NumberCellAction cellAction) {
            int x = cellAction.getRow();
            int y = cellAction.getColumn();
            
            if (cellAction.actionType() == ActionType.NUMBER_CELL_ACTION) {
                isCorrect = (game.getGrid().getCell(x, y).getNumber() == solvedGrid.getCell(x, y).getNumber());
            } else {
                isCorrect = false; // Retirer un nombre est toujours incorrect dans le mode apprentissage
            }
        } else if (action instanceof AnnotationCellAction annotAction) {
            int x = annotAction.getRow();
            int y = annotAction.getColumn();
            
            if (annotAction.actionType() == ActionType.ANNOTATION_CELL_ACTION) {
                isCorrect = solvedGrid.getCell(x, y).getAnnotations().contains(annotAction.getAnnotation());
            } else if (annotAction.actionType() == ActionType.ANNOTATION_REMOVE_CELL_ACTION) {
                isCorrect = !solvedGrid.getCell(x, y).getAnnotations().contains(annotAction.getAnnotation());
            }
        }
        return isCorrect;
    }
}