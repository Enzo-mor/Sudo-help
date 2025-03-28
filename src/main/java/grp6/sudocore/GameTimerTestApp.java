package grp6.sudocore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe de test pour le timer du jeu.
 * Permet d'afficher le temps écoulé pendant une partie et de tester les fonctionnalités de minuterie du jeu.
 * 
 * @author NGANGA YABIE Taïse de These
 * @see Game
 * @see GameTimeListener
 * @see DBManager
 * @see Profile
 */
public class GameTimerTestApp extends Application {

    /**
     * Instance du jeu en cours de test.
     */
    private Game game;

    /**
     * Label affichant le temps écoulé dans l'interface utilisateur.
     */
    private Label timerLabel;

    /**
     * Lancement du jeu et affichage du temps écoulé dans une fenetre JavaFX.
     * 
     * @param args Arguments non utilisés
     */
    @Override
    public void start(Stage primaryStage) {
        timerLabel = new Label("Temps : 00:00:00");
        
        try {
            game = new Game(DBManager.getGrid(2), new Profile("mathieu"));
        } catch (Exception e) {
        }

        // Initialiser le jeu
        game.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText("Temps : " + timer)));
        game.startGame();

        VBox root = new VBox(10, timerLabel);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Timer de Jeu");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Arreter le timer a la fermeture de la fenetre
        primaryStage.setOnCloseRequest(e -> {
            try {
                game.stopGame();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    /**
     * Point d'entrée principal de l'application.
     * 
     * @param args Arguments non utilisés
     */
    public static void main(String[] args) {
        launch(args);
    }
}
