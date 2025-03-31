package grp6.intergraph;
import grp6.sudocore.*;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe Main
 * Point d'entree principal de l'application Sudoku.
 * Cette classe initialise la base de donnees et demarre l'interface graphique.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see DBManager
 * @see MenusController
 */
public class Main extends Application {

    /**
     * Lance l'application en affichant le menu principal.
     * 
     * @param primaryStage Fenetre principale de l'application [Stage]
     */
    @Override
    public void start(Stage primaryStage) {
        
        MenusController.launcher(primaryStage);
    }

    /**
     * Methode principale, point d'entree du programme.
     * Elle initialise la base de donnees et demarre l'application JavaFX.
     * 
     * @param args Arguments passes en ligne de commande [String[]]
     */
    public static void main(String[] args) {
        try {
            // Initialiser la base de donnees
            DBManager.init();
            launch(args);
            //DBManager.deleteAllGames(); // Optionnel
        } catch (SQLException e) {
            // Gestion des execptions lie a l'initilisation
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}