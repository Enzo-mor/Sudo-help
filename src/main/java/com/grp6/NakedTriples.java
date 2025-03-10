package com.grp6;

/**
 * Technique : Triplets nus (Naked Triples)
 * Objectif : Réduire le nombre d’annotations dans un carré, une ligne ou une colonne.
 * Détection : 
 * - Si dans un bloc (carré, ligne ou colonne), il y a 3 cases contenant exactement 3 chiffres possibles, formant ainsi une combinaison unique (ex : {1,2,3}, {1,2}, {2,3}).
 * - Ces chiffres n'apparaissent pas ailleurs dans ce bloc.
 * - On peut alors supprimer ces 3 chiffres des annotations des autres cases du même bloc.
 */
public class NakedTriples implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {

        int[] tab, tab_line, tab_column, tab_square;
        for(int i=0; i<Grid.NB_NUM; i++){
            tab = grille.numToPosForSubGrid(i);
  
            tab_line=nb_Num_Annotations(grille.getLine(i));
            tab_column=nb_Num_Annotations(grille.getColumn(i));
            tab_square=nb_Num_Annotations(grille.getFlatSubGrid(tab[0], tab[1]));   

            
        }
        return false;
    }

    private int[] nb_Num_Annotations(Cell[] tab){
        int[] compteur;
        compteur=new int[Grid.NB_NUM];
        
        for(int i=0;i<Grid.NB_NUM;i++){
            if(tab[i].getAnnotations()[i]!=false){
                compteur[i]++;
            }
        }

        return compteur;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique des triplets nus
    }

    public static void main(String[] args) {
        // Exemple de grille où un triplet nu peut être trouvé
        int[] data = {
            2,5,0,4,7,3,6,1,8,
            6,1,3,8,2,9,4,7,5,
            7,8,4,5,6,1,9,2,3,
            9,3,1,2,5,7,8,6,4,
            5,4,7,6,8,3,1,9,2,
            8,6,2,1,9,4,7,5,3,
            1,7,8,3,4,2,5,9,6,
            3,9,5,7,1,6,2,4,0,
            4,2,6,9,0,5,3,8,7
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());

        NakedTriples nakedTriples = new NakedTriples();
        System.out.println(nakedTriples.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.NakedTriples"
    }
}
