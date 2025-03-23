package grp6.sudocore;

import java.util.List;

/**
 * Cette interface permet de representer une cellule modifiable d'une grille
 * de Sudoku.
 * 
 * @author POUSSE Kilian
 */
public interface Cell {
    
    /**
     * Recuperer le chiffre de la cellule.
     * 
     * @return Entier representant le chiffre (0 si vide) [int]
     */
    public int getNumber();

    /** 
     * Recuperer les annotations de la cellule.
     * 
     * @return Liste des annotations
     */
    public List<Integer> getAnnotations();

    /**
     * Recuperer la derniere annotation de la cellule.
     * 
     * @return Derniere annotation
     */
    public Integer getLastAnnotation();

    /**
     * Verifier si la cellule est vide ou non.
     * 
     * @return 'true' si la cellule est vide, sinon 'false' [boolean]
     */
    public boolean isEmpty();

    /**
     * Transforme la cellule en chaine de caracteres.
     * 
     * @return La chaine de caracteres correspondante [String]
     */
    @Override
    public String toString();

    /**
     * Savoir si une cellule est modifiable.
     * 
     * @return 'true' si la cellule peut etre modifiee, 'false' sinon [boolean]
     */
    public boolean isEditable();

    /**
     * Verifier si la cellule a des annotations.
     * 
     * @return 'true' si la cellule a des annotations, 'false' sinon [boolean]
     */
    public boolean hasAnnotations();

    /**
     * Mettre un chiffre dans une cellule.
     * 
     * @param number Chiffre a mettre dans la cellule [int]
     */
    public void setNumber(int number);

    /**
     * Ajouter une annotation a la cellule.
     * 
     * @param number Chiffre de l'annotation a ajouter [int]
     */
    public void addAnnotation(int number);

    /**
     * Enlever une annotation d'une cellule.
     * 
     * @param number Chiffre de l'annotation a retirer [int]
     */
    public void removeAnnotation(int number);

    /**
     * Nettoyer la cellule (la vider).
     */
    public void clear();

    /**
     * Cloner une cellule flexible.
     * 
     * @return Une nouvelle instance de Cell (clone de la cellule)
     */
    public Cell clone();
}
