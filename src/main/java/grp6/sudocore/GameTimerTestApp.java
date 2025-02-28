package grp6.sudocore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * classe de teste pour le timer du jeu
 */
public class GameTimerTestApp extends Application {

    private Game game;
    private Label timerLabel;

    @Override
    public void start(Stage primaryStage)  {
        timerLabel = new Label("Temps : 00:00:00");
         try {
            game = new Game(DBManager.getGrid(2), new Profile("mathieu"));
         } catch (Exception e) {
            // TODO: handle exception
         }
        // Initialiser le jeu
        
        game.setGameTimeListener(timer-> Platform.runLater(() -> timerLabel.setText("Temps : " + timer)));
        game.startGame();

        VBox root = new VBox(10, timerLabel);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Timer de Jeu");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Arrêter le timer à la fermeture de la fenêtre
        primaryStage.setOnCloseRequest(event -> {
            try {
                game.stopGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
       
    }

    public static void main(String[] args) {
        launch(args);
    }
}
