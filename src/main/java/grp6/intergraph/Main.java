package grp6.intergraph;

import grp6.sudocore.DBManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenusController.launcher(primaryStage);
    }

    public static void main(String[] args) {
        try {
            // Initialiser la base de données
            DBManager.init();
            launch(args);
        } catch (Exception e) {
            // Gestion des execptions lié à l'initilisation
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}