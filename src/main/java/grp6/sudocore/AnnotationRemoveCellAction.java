package grp6.sudocore;

import grp6.sudocore.SudoTypes.ActionType;

/**
 * cette classe permet de gerer les actions d'annotation d'un jeu de sudoku
 * elle permet de supprimer une annotation à une cellule flexible
 * 
 * @author Taise DE Thèse
 * @version 1.0
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @see ActionCell
 * @see SudoTypes
 */
public class AnnotationRemoveCellAction extends AnnotationCellAction {
    /**
     * constructeur permettant de creer une action qui permet de supprimer une annotation à une cellule specifique de la grille
     * @param game represente le jeu contenant la grille à modifier
     * @param x represente les coordonnées X de la cellule
     * @param y represente les coordonnées Y de la cellule
     * @param annotation represente la nouvelle annotation à supprimer
     */
    public AnnotationRemoveCellAction(Game game, int x, int y, int annotation, int old_annotation) {
        super(game, x, y, annotation, old_annotation);
    }

    @Override
    protected void doAction() {
        this.game.grid.getCell(x, y).removeAnnotation(annotation);
    }

    @Override
    protected void undoAction() {
        this.game.grid.getCell(x, y).addAnnotation(annotation);
    }

    @Override
    public String toString(){
        return " suppression de l'annotation  au jeu de valeur : "+annotation+ " à la position : x = "+x+" et y = "+y;
    }

     @Override
    public SudoTypes.ActionType actionType(){
        return ActionType.ANNOTATION_REMOVE_CELL_ACTION;
    }

    public static void main(String[] args) {


            System.out.println("AnnotationRemoveCellAction");
            try {
                //AnnotationRemoveCellAction annotation = new AnnotationRemoveCellAction(new Game(DBManager.getGrid(2),new Profile("philippe")), 0, 0, 3);
                 //System.out.println(annotation.serialise());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            
        
       
    }
    
}
