package grp6.sudocore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe de test pour le timer du jeu.
 * Permet d'afficher le temps ecoule pendant une partie et de tester les fonctionnalites de minuterie du jeu.
 * 
 * @author NGANGA YABIE Taise de These
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
     * Label affichant le temps écoule dans l'interface utilisateur.
     */
    private Label timerLabel;

    /**
     * Constructeur de la classe GameTimerTestApp
     */
    public GameTimerTestApp(){}

    /**
     * Lancement du jeu et affichage du temps écoule dans une fenetre JavaFX.
     * 
     * @param primaryStage stage pour afficher les informations
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
     * Point d'entree principal de l'application.
     * 
     * @param args Arguments non utilisés
     */
    public static void main(String[] args) {
        launch(args);
    }
}
