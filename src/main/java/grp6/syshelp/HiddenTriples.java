package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Technique : Triplets cachés (Hidden Triples)
 * Objectif : Réduire le nombre d’annotations dans une ligne, une colonne ou un carré.
 * Détection :
 * - Trouver **trois cases** dans une ligne, une colonne ou un carré qui contiennent exactement **trois chiffres communs** dans leurs annotations.
 * - Ces trois chiffres n'apparaissent **dans aucune autre case** du même bloc.
 * - On peut alors **supprimer toutes les autres annotations** de ces trois cases.
 */
public class HiddenTriples implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection des triplets cachés
        return false;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique des triplets cachés
    }

    public static void main(String[] args) {
        // Exemple de grille où un triplet caché peut être trouvé
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

        HiddenTriples hiddenTriples = new HiddenTriples();
        System.out.println(hiddenTriples.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.HiddenTriples"
    }
}
