package grp6.sudocore;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe modelisant une cellule fixe, c'est-a-dire qu'elle ne peut pas etre
 * modifiee par le joueur.
 * 
 * @author POUSSE Kilian
 * @see Cell
 * @see Grid
 * @see NoPutNumberOnCellExeception
 */
public class FixCell implements Cell {

    /* ======= Variables d'instance ======= */
    
    /** Chiffre stocke dans la cellule */
    protected int number; 

    /** Position de la cellule  */
    private int[] position;

    /* ======= Methodes d'instance ======= */

    /**
     * Constructeur de la classe 'FixCell'.
     * 
     * @param number Chiffre qui sera stocke dans la cellule 
     * @param x coordonnee de la ligne
     * @param y coordonnee de la colonne
     */
    public FixCell(int number, int x, int y) {
        this.position = new int[] {x, y};
        // Le chiffre doit etre valide: number in [0, 9] // 0 == vide
        if(Grid.isValidNumber(number) || number == 0){
            this.number = number;
        }
        else {
            System.err.println("Impossible d'initialiser cette cellule: " + number + " doit etre incluse dans [0, " + Grid.NB_NUM + "]");
            this.number = 0;
        }
    }
    

    /**
     * Recuperer le chiffre de la cellule.
     * 
     * @return Entier representant le chiffre (0 si vide) [int]
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Mettre un chiffre dans une cellule.
     * /!\ Impossible pour une cellule fixe /!\
     * 
     * @param number Chiffre a mettre dans la cellule [int]
     * @throws NoPutNumberOnCellExeception Si le joueur essaie de modifier une cellule fixe
     * @see NoPutNumberOnCellExeception
     */
    @Override
    public void setNumber(int number)  {
        throw new NoPutNumberOnCellExeception("Il est impossible de mettre un nombre dans cette cellule car elle est fixe.");
    }

    /**
     * Ajouter une annotation a la cellule.
     * /!\ Impossible pour une cellule fixe /!\
     * 
     * @param number Chiffre de l'annotation a ajouter [int]
     */
    @Override
    public void addAnnotation(int number) {
        System.out.println("Il est impossible d'ajouter une annotation a cette cellule");
    }

    /** 
     * Recuperer les annotations de la cellule.
     * 
     * @return Liste des annotations
     */
    @Override
    public List<Integer> getAnnotations() {
        List<Integer> l = new ArrayList<>();
        return l;
    }

    /**
     * Recuperer la derniere annotation de la cellule.
     * 
     * @return La derniere annotation [Integer]
     */
    @Override
    public Integer getLastAnnotation() {
        return 1;
    }

    /**
     * Enlever une annotation d'une cellule.
     * /!\ Impossible pour une cellule fixe /!\
     * 
     * @param number Chiffre de l'annotation a retirer [int]
     */
    @Override
    public void removeAnnotation(int number) {
        System.out.println("Il est impossible d'enlever une annotation a cette cellule");
    }

    /**
     * Savoir si la cellule a des annotations.
     * 
     * @return true si la cellule a des annotations, sinon false [boolean]
     */
    @Override
    public boolean hasAnnotations() {
        return false;
    }

    /** 
     * Nettoyer la cellule (la vider).
     * /!\ Impossible pour une cellule fixe /!\
     */
    @Override
    public void clear() {
        System.out.println("Il est impossible de nettoyer cette cellule");
    }

    /**
     * Savoir si une cellule est vide ou non.
     * 
     * @return 'true' si la cellule est vide, sinon 'false' [boolean]
     */
    @Override
    public boolean isEmpty() {
        return this.number == 0;
    }

    /** 
     * Transformer la cellule en chaine de caracteres.
     * 
     * @return La chaine de caracteres correspondante [String]
     */
    @Override
    public String toString() {
        return String.valueOf(this.getNumber());
    }

    /**
     * Savoir si une cellule est modifiable.
     * 
     * @return true si la cellule peut etre modifiee, sinon false [boolean]
     */
    @Override
    public boolean isEditable() {
        return false;
    }

    /**
     * Cloner une cellule fixe.
     * 
     * @return Une nouvelle instance de Cell (clone de la cellule)
     */
    @Override
    public Cell clone() {
        // Clonage superficiel, pas de besoin de cloner des objets internes.
        return new FixCell(this.number,this.getPosition()[0],getPosition()[1]);
    }
    
    /**
     * Recuperer la position d'une cellule dans sa grille
     * @return position d'une cellule
     */
    public int[] getPosition() {
        return position;
    }

    /** 
     * Recuperer les annotations de la cellule
     * @return Liste des annotations 
     */
    @Override
    public boolean[] getAnnotationsBool() {
        return new boolean[Grid.NB_NUM];
    }

    /**
     * Permet de decter si un tableau d'annotation ne contient qu'une entree
     * @return un boolean qui est vrai quand il n'y a qu'une annotation
     */
    @Override
    public boolean OnlyOneAnnotation() {
        return false;
    }

    /**
     * Nettoyer les annotations de la cellule.
     */
    @Override
    public void clearAnnotations() {
        /* fait rien pour une fix */
    }

}
