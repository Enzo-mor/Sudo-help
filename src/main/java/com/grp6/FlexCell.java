package com.grp6;

import java.util.List;

/**
 * Classe modelisant une cellule flex, c'est-a-dire qu'elle peut être
 * modifié par le joueur.
 * 
 * Cette classe hérite de 'FixCell'
 * @author Kilian POUSSE
 * @version 1.2
 */

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

    public void setRedo (int number){}

    /**
     * methode permettant de vider les annotations d'une cellule
     * 
     */
    protected void trashAnnotation() {
        for(int i = 0; i < Grid.NB_NUM; i++) {
            this.annotations[i] = false;
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
     * @return Liste des annotations 
     */
    @Override
    public List<Integer> getAnnotations() {
        List<Integer> res = super.getAnnotations();
        for(int i=0; i<annotations.length; i++) {
            if(annotations[i])
                res.add(i+1);
        }

        return res;
    }

    /** 
     * Récupérer les annotations de la cellule
     * @return tableau des présences des annotations [booleen[]]
     */
    @Override
    public boolean[] getAnnotationsBool() {
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

    /**
     * Permet de decter si un tableau d'annotation ne contient qu'une entrée
     * @param grille represente la grille à detecter
     * @return une liste de technique application sur cette grille
     */
    public boolean OnlyOneAnnotation() {
        int count =0;
        for(int i = 0;i<annotations.length;i++){
            if(annotations[i]){
                count++;
            }
        }
        if (count == 1){
            return true;
        
        }  
        return false;
    }

<<<<<<< HEAD
    /**
     * Savoir si une cellule est modifiable
     * @return Vrai si la cellule peut etre modifiée
     */
    @Override
    public boolean isEditable() {
        return true;
    }

     /**
     * Clone une cellule flexible
     * @return Une nouvelle instance de Cell (clone de la cellule)
     */
    @Override
    public Cell clone() {
        // Clone profond : création d'une nouvelle instance de FlexCell,
        // et copie des annotations.
        FlexCell clonedCell = new FlexCell(this.position[0]*9+this.position[1]);
        clonedCell.number = this.number;
        clonedCell.annotations = this.annotations.clone(); // Clonage profond du tableau d'annotations.
        return clonedCell;
    }
=======
    public boolean isEditable(){
        return true;
    }

>>>>>>> c987820fa9f76892ad05a039d08ef6ec1f73ceae
}
