package grp6.sudocore;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe modelisant une cellule flexible, c'est-a-dire qu'elle peut etre
 * modifiee par le joueur.
 * 
 * Cette classe herite de 'FixCell' et permet de manipuler des annotations 
 * (valeurs possibles mais non definitives) en plus de la valeur de la cellule.
 * 
 * @author POUSSE Kilian
 * @see FixCell
 * @see Grid
 * @see Cell
 */
public final class FlexCell extends FixCell {

    /* ======= Variables d'instance ======= */

    /** 
     * Tableau des annotations (si annotations[i]==true, i+1 est annotée dans la cellule) 
     */
    protected boolean[] annotations;
    
    /** 
     * Liste des annotations (si annotations[i]==true, i+1 est annotée dans la cellule) 
     */
    protected List<Integer> annotationsList;  

    /* ======= Méthodes d'instance ======= */

    /**
     * Constructeur de la classe 'FlexCell'
     * @param x Coordonnee de la ligne
     * @param y Coordonnee de la colonne
     */
    public FlexCell(int x, int y) {
        super(0, x ,y);
        // Initialise le tableau des annotations à false
        this.annotations = new boolean[Grid.NB_NUM];
        this.annotationsList = new ArrayList<>();
    }


    /**
     * Mettre un chiffre dans une cellule.
     * @param number Chiffre a mettre dans la cellule [int]
     */
    @Override
    public void setNumber(int number) {
        // Le chiffre doit etre valide: number in ]0, 9]
        if(Grid.isValidNumber(number)){
            this.number = number;
        }
        else {
            System.err.println("Impossible d'initialiser cette cellule: " + number + " doit etre inclue dans ]0, " + Grid.NB_NUM + "]");
        }
    }

    /**
     * Methode permettant de vider les annotations d'une cellule.
     */
    protected void trashAnnotation() {
        for(int i = 0; i < Grid.NB_NUM; i++) {
            this.annotations[i] = false;
        }
    }

    /**
     * Ajouter une annotation à la cellule.
     * @param number Chiffre de l'annotation à ajouter [int]
     */
    @Override
    public void addAnnotation(int number) {
        if(Grid.isValidNumber(number)){
            // Mettre le booleen à true pour signaler que 'number' est présent dans l'annotation
            this.annotations[number-1] = true;
            this.annotationsList.add(number);
        }
        else {
            System.err.println("Impossible d'ajouter une annotation dans cette cellule: " + number + " doit etre inclue dans ]0, " + Grid.NB_NUM + "]");
        }
    }

    /**
     * Enlever une annotation d'une cellule.
     * @param number Chiffre de l'annotation à retirer [int]
     */
    @Override
    public void removeAnnotation(int number) {
        // Le chiffre doit etre valide: number in ]0, 9]
        if(Grid.isValidNumber(number)){
            this.annotations[number-1] = false;
            this.annotationsList.remove(Integer.valueOf(number));
        }
        else {
            System.err.println("Impossible d'enlever une annotation dans cette cellule: " + number + " doit etre inclue dans ]0, " + Grid.NB_NUM + "]");
        }
    }

    /** 
     * Recuperer les annotations de la cellule.
     * @return Liste des annotations 
     */
    @Override
    public List<Integer> getAnnotations() {
        List<Integer> res = super.getAnnotations();
        for(int i = 0; i < annotations.length; i++) {
            if(annotations[i])
                res.add(i+1);
        }

        return res;
    }

    /** 
     * Recuperer la derniere annotation de la cellule.
     * @return La derniere annotation [Integer]
     */
    @Override
    public Integer getLastAnnotation() {
        if (annotationsList.isEmpty()) {
            return 0;
        }
        return annotationsList.get(annotationsList.size() - 1);
    }

    /**
     * Verifier si la cellule possede des annotations.
     * @return true si la cellule a des annotations, sinon false [boolean]
     */
    @Override
    public boolean hasAnnotations() {
        return !getAnnotations().isEmpty();
    }

    /** 
     * Nettoyer la cellule (la vider).
     */
    @Override  
    public void clear() {
        this.number = 0;
    }

    /** 
     * Transformer la cellule en chaine de caracteres.
     * @return La chaine de caracteres correspondante [String]
     */
    @Override
    public String toString() {
        int nb = this.number;
        if(nb == 0) {
            return " ";
        }
        return String.valueOf(nb);
    }

    /**
     * Savoir si une cellule est modifiable.
     * @return true si la cellule peut etre modifiee, sinon false [boolean]
     */
    @Override
    public boolean isEditable() {
        return true;
    }

    /**
     * Cloner une cellule flexible.
     * @return Une nouvelle instance de Cell (clone de la cellule)
     */
    @Override
    public Cell clone() {
        // Clone profond : creation d'une nouvelle instance de FlexCell,
        // et copie des annotations.
        FlexCell clonedCell = new FlexCell(this.getPosition()[0],this.getPosition()[1]);
        clonedCell.number = this.number;
        clonedCell.annotations = this.annotations; // Clonage profond du tableau d'annotations.
        return clonedCell;
    }

    /**
     * Permet de decter si un tableau d'annotation ne contient qu'une entree
     * @return un boolean qui est vrai quand il n'y a qu'une annotation
     */
    public boolean OnlyOneAnnotation() {
        int count =0;
        for(int i = 0;i<annotations.length;i++){
            if(annotations[i]){
                count++;
            }
        }
        return count == 1;
    }


    /** 
     * Recuperer les annotations de la cellule
     * @return Liste des annotations 
     */
    @Override
    public boolean[] getAnnotationsBool() {
        return annotations;
    }

    /**
     * Nettoyer les annotations de la cellule.
     */
    @Override
    public void clearAnnotations() {
        annotations = new boolean[Grid.NB_NUM];
        annotationsList.clear();
    }
}
