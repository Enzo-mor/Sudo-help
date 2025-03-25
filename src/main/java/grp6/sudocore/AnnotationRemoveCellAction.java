package grp6.sudocore;

import grp6.sudocore.SudoTypes.ActionType;

/**
 * Cette classe permet de gerer les actions d'annotation dans un jeu de sudoku.
 * Elle permet de supprimer une annotation d'une cellule flexible.
 * 
 * @author DE THESE Taise
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @see ActionCell
 * @see SudoTypes
 * @see AnnotationCellAction
 * @see NumberCellAction
 * @see ActionFactory
 * @see ActionManager
 */
public class AnnotationRemoveCellAction extends AnnotationCellAction {

    /**
     * Constructeur permettant de creer une action pour supprimer une annotation d'une cellule specifique de la grille.
     * 
     * @param game Jeu contenant la grille a modifier.
     * @param x Coordonee X de la cellule.
     * @param y Coordonee Y de la cellule.
     * @param annotation Annotation a supprimer.
     * @param old_annotation Valeur initiale de l'annotation avant la suppression.
     */
    public AnnotationRemoveCellAction(Game game, int x, int y, int annotation, int old_annotation) {
        super(game, x, y, annotation, old_annotation);
    }

    /**
     * Applique l'action en supprimant l'annotation de la cellule.
     */
    @Override
    protected void doAction() {
        this.game.grid.getCell(x, y).removeAnnotation(annotation);
    }

    /**
     * Annule l'action de suppression d'annotation en ajoutant l'annotation de nouveau dans la cellule.
     */
    @Override
    protected void undoAction() {
        this.game.grid.getCell(x, y).addAnnotation(annotation);
    }

    /**
     * Retourne une chaine representant l'action de suppression d'annotation.
     * 
     * @return La description de l'action de suppression d'annotation.
     */
    @Override
    public String toString() {
        return "Suppression de l'annotation de valeur : " + annotation + " a la position : x = " + x + " et y = " + y;
    }

    /**
     * Retourne le type d'action : ANNOTATION_REMOVE_CELL_ACTION.
     * 
     * @return Le type d'action.
     */
    @Override
    public SudoTypes.ActionType actionType() {
        return ActionType.ANNOTATION_REMOVE_CELL_ACTION;
    }

    /**
     * Methode principale pour tester la classe AnnotationRemoveCellAction.
     * 
     * @param args Arguments de ligne de commande.
     */
    public static void main(String[] args) {
        System.out.println("AnnotationRemoveCellAction");
        try {
            // Exemple de creation d'une action pour supprimer une annotation
            // AnnotationRemoveCellAction annotation = new AnnotationRemoveCellAction(new Game(DBManager.getGrid(2), new Profile("philippe")), 0, 0, 3);
            // System.out.println(annotation.serialise());
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}
