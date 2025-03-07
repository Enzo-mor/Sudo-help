package com.grp6;

/**
 * Technique : Swordfish
 * Objectif : Réduire les annotations en exploitant un modèle avancé en grille.
 * Détection :
 * - Un chiffre donné apparaît **exactement dans trois colonnes (ou trois lignes) et seulement à trois emplacements distincts**.
 * - Ces positions forment un **motif en Swordfish** : elles sont liées en ligne et en colonne.
 * - On peut alors **supprimer ce chiffre des autres cases des mêmes lignes/colonnes**.
 */
public class Swordfish implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection du Swordfish
        return false;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique du Swordfish
    }

    public static void main(String[] args) {
        // Exemple de grille où un Swordfish peut être trouvé
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

        Swordfish swordfish = new Swordfish();
        System.out.println(swordfish.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.Swordfish"
    }
}
