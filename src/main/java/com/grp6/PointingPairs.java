package com.grp6;

/**
 * Technique : Paires pointantes (Pointing Pairs)
 * Objectif : Réduire le nombre d’annotations dans une ligne ou une colonne.
 * Détection :
 * - Si un chiffre donné **n’apparaît que dans une seule ligne ou une seule colonne d’un carré (bloc 3x3)**,
 *   alors ce chiffre **doit forcément être placé dans ce carré**.
 * - On peut donc **supprimer ce chiffre des annotations des autres cases de cette ligne/colonne en dehors du carré**.
 */
public class PointingPairs implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection des paires pointantes
        return false;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique des paires pointantes
    }

    public static void main(String[] args) {
        // Exemple de grille où une paire pointante peut être trouvée
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

        PointingPairs pointingPairs = new PointingPairs();
        System.out.println(pointingPairs.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.PointingPairs"
    }
}
