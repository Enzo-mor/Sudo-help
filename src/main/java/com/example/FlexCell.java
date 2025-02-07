package com.example;

import java.util.List;

/**
 * Classe modelisant une cellule flex, c'est-a-dire qu'elle peut être
 * modifié par le joueur.
 * 
 * Cette classe hérite de 'FixCell'
 * @author Kilian POUSSE
 * @version 1.2
 */
public class FlexCell extends FixCell {

    /* ======= Variables d'instance ======= */
    /** Tableau des annotations (si annotations[i]==true, i+1 est annotée dans la cellule) */
    protected boolean[] annotations;  

    /* ======= Méthodes d'instance ======= */

    /**
     * Constructeur de la classe 'FlexCell'
     */
    public FlexCell() {
        super(0);
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
     * Savoir si une cellule est modifiable
     * @return Vrai si la cellule peut etre modifiée
     */
    @Override
    public boolean isEditable() {
        return true;
    }
}
