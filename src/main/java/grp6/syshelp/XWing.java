package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Technique : X-Wing
 * Objectif : Réduire le nombre d’annotations en exploitant une configuration en "X".
 * Détection :
 * - Un chiffre donné n’apparaît **que dans deux cases sur exactement deux lignes distinctes** (et aux mêmes positions sur ces lignes).
 * - Ce chiffre doit être **placé dans ces cases**, formant ainsi un motif en "X".
 * - On peut alors **supprimer ce chiffre des autres cases des colonnes correspondantes** (ou inversement avec lignes et colonnes).
 */
public class XWing implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {
        // TODO: Implémenter la détection du X-Wing
        return false;
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique du X-Wing
    }

    public static void main(String[] args) {
        // Exemple de grille où un X-Wing peut être trouvé
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

        XWing xWing = new XWing();
        System.out.println(xWing.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.XWing"
    }
}
