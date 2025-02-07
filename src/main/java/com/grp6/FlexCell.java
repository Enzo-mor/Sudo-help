package com.grp6;

/**
 * Classe modelisant une cellule flex, c'est-a-dire qu'elle peut être
 * modifié par le joueur.
 * 
 * Cette classe hérite de 'FixCell'
 */
public class FlexCell extends FixCell {

    /* ======= Variables d'instance ======= */
    protected boolean[] annotations;    // Tableau des annotations (si annotations[i]==true, i+1 est annotée dans la cellule)
    private int[] position;             // Tableau de deux int [x,y] reprsesentant la position en x y

    /* ======= Méthodes d'instance ======= */

    /**
     * Constructeur de la classe 'FlexCell'
     * @param indice indice de la cellule
     */
    public FlexCell(int indice) {
        super(0,indice);
        // Initialise le tableau des annotations à false
        this.annotations = new boolean[Grid.NB_NUM];
    }

    /**
     * Mettre un chiffre dans une cellule
     * @param number Chiffre à mettre dans la cellule [int]
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
     * Ajouter un annotation à la cellule
     * @param number Chiffre de l'annotation à ajouter [int]
     */
    @Override
    public void addAnnotation(int number) {
        if(Grid.isValidNumber(number)){
            // Mettre le booleen à true pour signaler que 'number' est présant dans l'annotation
            this.annotations[number-1] = true;
        }
        else {
            System.err.println("Impossible d'ajouter une annotation dans cette cellule: " + number + " doit etre inclue dans ]0, " + Grid.NB_NUM + "]");
        }
    }
    
    /**
     * Enlever une annotation d'une cellule
     * @param number Chiffre de l'annotation à retirer [int]
     */
    @Override
    public void removeAnnotation(int number) {
        // Le chiffre doit etre valide: number in ]0, 9]
        if(Grid.isValidNumber(number)){
            this.annotations[number-1] = false;
        }
        else {
            System.err.println("Impossible d'enlever une annotation dans cette cellule: " + number + " doit etre inclue dans ]0, " + Grid.NB_NUM + "]");
        }
    }

    /** 
     * Récupérer les annotations de la cellule
     * @return tableau des présences des annotations [booleen[]]
     */
    @Override
    public boolean[] getAnnotations() {
        return this.annotations;
    }

    /** 
     * Néttoyer la cellule (la vider)
     */
    @Override  
    public void clear() {
        this.number = 0;
    }
    
    /** 
     * Transforme la cellule en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    @Override
    public String toString() {
        int nb = this.number;
        if(nb == 0) {
            return " ";
        }
        return String.valueOf(nb);
    }
}
