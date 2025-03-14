package grp6.intergraph;

import grp6.sudocore.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenusController.launcher(primaryStage);
    }

    public static void main(String[] args) {
        try {
            // Initialiser la base de donnees
            DBManager.init();
            launch(args);
        } catch (Exception e) {
            // Gestion des execptions lie a l'initilisation
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}