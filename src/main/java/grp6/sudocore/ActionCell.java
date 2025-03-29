package grp6.sudocore;

/**
 * Cette classe permet d'effectuer les differentes actions au niveau de la cellule flexible ou editable du jeu de sudoku.
 * Elle permet de faire l'action et de l'annuler.
 * Elle permet aussi de serialiser et deserialiser les actions.
 * 
 * @author NGANGA YABIE Taïse de These
 * @see NoEditableCellExeception
 * @see NoCompatibleActionGameException
 * @see Game
 * @see ActionManagerApply
 * @see Action
 */
abstract public class ActionCell extends Action {

  /**
   * Coordonnee X de la cellule.
   */
  protected final int x;

  /**
   * Coordonnee Y de la cellule.
   */
  protected final int y;

  /**
   * Constructeur pour les actions sur les cellules flexibles ou editables.
   * 
   * @param game Le jeu sur lequel les actions seront appliquees.
   * @param x Coordonee X de la cellule.
   * @param y Coordonee Y de la cellule.
   * 
   * @throws NoEditableCellExeception Si la cellule n'est pas editable.
   */
  public ActionCell(Game game, int x, int y) throws NoEditableCellExeception {
      super(game);
      this.x = x;
      this.y = y;
      try {
          if(game.getGrid().getCell(x, y).isEditable() == false) {
              throw new NoCompatibleActionGameException("L'action ne peut pas etre creee car la cellule doit etre editable ou flexible.");
          }
      } catch (NoEditableCellExeception | NoCompatibleActionGameException e) {
          System.err.println(e.getMessage());
      }
  }

  /**
   * Retourne la ligne de la cellule.
   * 
   * @return La ligne de la cellule.
   */
  @Override
  public int getRow() {
      return x;
  }

  /**
   * Retourne la colonne de la cellule.
   * 
   * @return La colonne de la cellule.
   */
  @Override
  public int getColumn() {
      return y;
  }
}
