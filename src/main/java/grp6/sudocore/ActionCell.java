package grp6.sudocore;

/**
 * cette classe permet d'effectuer les diffrente actions  au niveau de la cellule flexible ou editable du jeu de sudoku
 * elle permet de faire l'action et de l'annuler
 * elle permet aussi de serialiser et deserialiser les actions
 * 
 * @version 1.0
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @author Taise De Thèse
 */
abstract public class ActionCell extends Action {

 /**
 * represente les coordonnées X de la cellule
 */
protected final int x;

/**
 * represente les coordonnées Y de la cellule
 */
protected final int y;


/**
 * constructeur des actions sur les cellules flexibles ou editable
 * @param game represente le jeu sur lequel les actions seront appliquées
 * @param x represente les coordonnées X  de la cellule
 * @param y represente les coordonnées Y de la cellule
 * 
 * @throws NoEditableCellExeception  si la cellule n'est pas editable
 */
  public ActionCell(Game game, int x, int y) throws NoEditableCellExeception {
      super(game);
      this.x = x;
      this.y = y;
    try {
    
      if(game.getGrid().getCell(x, y).isEditable() == false){
          throw new NoCompatibleActionGameException("l'action ne peut etre crée car la cellule doit etre editable ou flexible");
      }

    } catch (NoEditableCellExeception | NoCompatibleActionGameException e) {
      System.err.println(e.getMessage());
    }  
  }

  @Override
  public int getRow(){
      return x;
  }

  @Override
  public int getColumn(){
    return y;
  }
}