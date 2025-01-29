package com.example;

/**
 * Classe modelisant une cellule fixe, c'est-a-dire qu'elle ne peut pas être
 * modifié par le joueur.
 * 
 * Cette classe suite l'interface 'Cell'
 */
public class FixCell implements Cell {

    /* ======= Variables d'instance ======= */
    protected int number;   // Chiffre stocké dans la cellule

    /* ======= Méthodes d'instance ======= */

    /**
     * Constructeur de la classe 'FixCell'.
     * @param number Chiffre qui sera stoqué dans la cellule [int]
     */
    public FixCell(int number) {
        // Le chiffre doit etre valide: number in [0, 9] // 0 == vide
        if(Grid.isValidNumber(number) || number == 0){
            this.number = number;
        }
        else {
            System.err.println("Impossible d'initialiser cette cellule: " + number + " doit etre inclue dans [0, " + Grid.NB_NUM + "]");
            this.number = 0;
        }
    }

    /**
     * Recupérer le chiffre de la cellule
     * @return Entier représentant le chiffre (0 si vide) [int]
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Mettre un chiffre dans une cellule
     * /!\ Impossible pour une cellule fixe /!\
     * @param number Chiffre à mettre dans la cellule [int]
     */
    @Override
    public void setNumber(int number) {
        System.out.println("Il est impossible de mettre un nombre dans cette cellule");
    }

    /**
     * Ajouter un annotation à la cellule
     * /!\ Impossible pour une cellule fixe /!\
     * @param number Chiffre de l'annotation à ajouter [int]
     */
    @Override
    public void addAnnotation(int number) {
        System.out.println("Il est impossible d'ajouter une annotation à cette cellule");
    }

    /** 
     * Récupérer les annotations de la cellule
     * /!\ Le tableau est remplit de 'false' car les cellules fixes
     * n'ont pas d'annotations /!\
     * @return tableau des présences des annotations [booleen[]]
     */
    @Override
    public boolean[] getAnnotations() {
        return new boolean[Grid.NB_NUM];
    }
    
    /**
     * Enlever une annotation d'une cellule
     * /!\ Impossible pour une cellule fixe /!\
     * @param number Chiffre de l'annotation à retirer [int]
     */
    @Override
    public void removeAnnotation(int number) {
        System.out.println("Il est impossible d'enlever une annotation à cette cellule");
    }

    /** 
     * Néttoyer la cellule (la vider)
     * /!\ Impossible pour une cellule fixe /!\
     */
    @Override
    public void clear() {
        System.out.println("Il est impossible de netoyer cette cellule");
    }

    /**
     * Savoir si une celluce est vide ou non
     * @return 'true' si la cellule est vide, sinon 'false' [booleen]
     */
    @Override
    public boolean isEmpty() {
        return this.number == 0;
    }

    /** 
     * Transforme la cellule en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    @Override
    public String toString() {
        return String.valueOf(this.getNumber());
    }
    

}
