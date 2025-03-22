package grp6.intergraph;
import grp6.sudocore.*;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Classe MainMenu
 * Cette classe gere l'affichage et les interactions du menu principal de l'application Sudoku.
 * Elle permet d'acceder aux differents modes de jeu, au classement et aux parametres.
 * 
 * @author Perron Nathan
 * @author Rasson Emma
 * @see Profile
 * @see Settings
 * @see GameplayChoice
 * @see StyledContent
 */
public class MainMenu {

    /** 
     * Pseudo du joueur actuellement connecte 
     */
    private static String pseudo = null;

    /** 
     * Profil du joueur selectionne 
     */
    private static Profile profile = null;

    /** 
     * Fenetre des parametres 
     */
    private static Settings settingsWindow = null;

    /**
     * Animation de rotation pour l'icône des parametres 
     */
    private static RotateTransition rotateAnimation;


    /**
     * Affiche le menu principal de l'application.
     * 
     * @param stage Fenetre principale de l'application [Stage]
     * @param selectedProfile Profil du joueur selectionne [Profile]
     */
    public static void showMainMenu(Stage stage, Profile selectedProfile) {
        pseudo = selectedProfile.getPseudo();
        profile = selectedProfile;

        // Creation du layout principal
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        // Message de bienvenue
        Label welcomeLabel = new Label("Bienvenue, " + pseudo);
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Bouton pour demarrer une partie
        Button startGameButton = new ProfileButton("Jouer");
        StyledContent.applyButtonBoxStyle(startGameButton);
        startGameButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        // Bouton pour afficher le classement
        Button classementButton = new ProfileButton("Classement");
        StyledContent.applyButtonBoxStyle(classementButton);
        classementButton.setOnAction(e -> System.out.println("Classement ouvert"));

        // Bouton pour quitter l'application
        Button exitButton = new ProfileButton("Quitter");
        StyledContent.applyButtonBoxStyle(exitButton);
        exitButton.setOnAction(e -> {
            settingsWindow.close();
            stage.close();
        });

        layout.getChildren().addAll(welcomeLabel, startGameButton, classementButton, exitButton);

        // Bouton des parametres avec icône animee
        Button settingsButton = new Button();
        ImageView gearIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/gear.png")));
        gearIcon.setFitWidth(30);
        gearIcon.setFitHeight(30);
        settingsButton.setGraphic(gearIcon);
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Animation de rotation de l'icône des parametres
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setByAngle(180);
        rotateAnimation.setCycleCount(1);

        settingsWindow = Settings.getInstance(stage, gearIcon);
        settingsButton.setOnAction(e -> settingsWindow.toggleSettingsWindow());

        // Conteneur pour aligner le bouton en bas a droite
        HBox bottomRightContainer = new HBox(settingsButton);
        bottomRightContainer.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightContainer.setStyle("-fx-padding: 10px;");

        // Organisation du layout avec BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(layout);
        root.setBottom(bottomRightContainer);

        // Ferme toutes les fenetres lorsque l'utilisateur quitte l'application
        stage.setOnCloseRequest(e -> {
            Platform.exit(); // Ferme toutes les fenetres JavaFX
            System.exit(0);  // Termine l'application proprement
        });

        // Creation et affichage de la scene
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Menu Principal - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Retourne le pseudo du profil actuellement selectionne.
     * 
     * @return Pseudo du joueur [String]
     */
    public static String getProfileName() {
        return pseudo;
    }

    /**
     * Retourne le profil actuellement selectionne.
     * 
     * @return Profil du joueur [Profile]
     */
    public static Profile getProfile() {
        return profile;
    }
}