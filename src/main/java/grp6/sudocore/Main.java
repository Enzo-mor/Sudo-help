package grp6.sudocore;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe principale contenant le point d'entree de l'application.
 * Elle initialise la base de donnees et effectue quelques tests
 * sur la recuperation d'une grille et l'affichage des erreurs.
 * 
 * @author NGANGA YABIE Taïse de These 
 */
public class Main {

    /**
     * Methode principale qui initialise la base de donnees et teste la recuperation d'une grille.
     * 
     * @param args Arguments de ligne de commande (non utilises).
     */
    public static void main(String[] args) {
        try {
            DBManager.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Grid g = DBManager.getGrid(8);
        System.out.println(g);

        // Exemple de visualisation des erreurs
        List<int[]> errors = g.evaluate();
        for(int[] err: errors) {
            System.out.print("(" + err[0] + ", " + err[1] + ") ");
        }
        System.out.println();

        System.out.println(DBManager.getProfiles());
    }
}
