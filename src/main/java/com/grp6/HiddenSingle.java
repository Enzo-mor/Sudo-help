package com.grp6;

import java.util.List;

/**
 * Technique : Singleton caché (Hidden Single)
 * Objectif : Trouver une case où un chiffre ne peut apparaître qu’à un seul endroit dans un bloc (ligne, colonne ou carré).
 * Détection :
 * - Pour chaque chiffre (1 à 9), vérifier dans un bloc (ligne, colonne ou carré).
 * - Si le chiffre n’apparaît qu’en annotation dans une seule case de ce bloc, alors cette case doit obligatoirement contenir ce chiffre.
 */
public class HiddenSingle implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {    
        int[] tab;
        int[] tab_line, tab_column, tab_square;
        for(int i=0; i<Grid.NB_NUM; i++){
            tab = grille.numToPosForSubGrid(i);
  
            tab_line=nb_Num_Annotations(grille.getLine(i));
            tab_column=nb_Num_Annotations(grille.getColumn(i));
            tab_square=nb_Num_Annotations(grille.getFlatSubGrid(tab[0], tab[1]));   
            
            for(int j=0; j<Grid.NB_NUM; j++){
                if(tab_line[j]==1 || tab_column[j]==1 || tab_square[j]==1){
                    return true;
                }
            }
            
        }
        return false;
    }

    private int[] nb_Num_Annotations(Cell[] tab){
        int[] compteur;
        compteur=new int[Grid.NB_NUM];
        
        for(int i=0;i<Grid.NB_NUM;i++){
            if(tab[i].getAnnotations()!=null){
                compteur[i]++;
            }
        }

        return compteur;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique du singleton caché
    }

    public static void main(String[] args) {
        // Exemple de grille où un singleton caché peut être trouvé
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

        HiddenSingle hiddenSingle = new HiddenSingle();
        System.out.println(hiddenSingle.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.HiddenSingle"
    }
}
