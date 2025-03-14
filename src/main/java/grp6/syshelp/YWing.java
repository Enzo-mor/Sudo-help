package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Technique : Y-Wing
 * Objectif : Réduire les annotations en exploitant une relation en chaîne entre trois cases.
 * Détection :
 * - On trouve une **case pivot** contenant **exactement deux annotations (ex: {X,Y})**.
 * - On trouve deux autres **cases appelées "pinces"**, chacune partageant une annotation avec la pivot :
 *   - Une pince avec {X, Z}
 *   - Une pince avec {Y, Z}
 * - Si les **deux pinces se trouvent sur la même ligne, colonne ou carré** et contiennent une annotation commune (Z), 
 *   alors on peut **supprimer Z de toutes les cases qui voient ces deux pinces**.
 */
public class YWing implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection du Y-Wing
        return false;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique du Y-Wing
    }

    public static void main(String[] args) {
        // Exemple de grille où un Y-Wing peut être trouvé
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

        YWing yWing = new YWing();
        System.out.println(yWing.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.YWing"
    }
}
