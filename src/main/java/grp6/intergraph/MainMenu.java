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

public class MainMenu {
    private static String pseudo = null;
    private static Profile profile = null;
    private static Settings settingsWindow = null;
    private static RotateTransition rotateAnimation;

    public static void showMainMenu(Stage stage, Profile selectedProfile) {
        pseudo = selectedProfile.getPseudo();
        profile = selectedProfile;

        // --- Contenu principal au centre ---
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + pseudo);
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new ProfileButton("Jouer");
        startGameButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        Button classementButton = new ProfileButton("Classement");
        classementButton.setOnAction(e -> System.out.println("Classement ouvert"));

        Button exitButton = new ProfileButton("Quitter");
        exitButton.setOnAction(e -> {
            settingsWindow.close();
            stage.close();
        });

        layout.getChildren().addAll(welcomeLabel, startGameButton, classementButton, exitButton);

        // --- Bouton "gear" en bas a droite ---
        Button settingsButton = new Button();
        ImageView gearIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/gear.png")));
        gearIcon.setFitWidth(30);
        gearIcon.setFitHeight(30);
        settingsButton.setGraphic(gearIcon);
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // --- Animation de rotation ---
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setByAngle(180);
        rotateAnimation.setCycleCount(1);

        settingsWindow = new Settings(stage, gearIcon);
        settingsButton.setOnAction(e -> settingsWindow.toggleSettingsWindow());

        // Conteneur pour aligner le bouton en bas a droite
        HBox bottomRightContainer = new HBox(settingsButton);
        bottomRightContainer.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightContainer.setStyle("-fx-padding: 10px;");

        // --- BorderPane pour organiser la mise en page ---
        BorderPane root = new BorderPane();
        root.setCenter(layout); // Place le menu principal au centre
        root.setBottom(bottomRightContainer); // Place le bouton settings en bas

        // --- Fermer toutes les fenetres avec la croix ---
        stage.setOnCloseRequest(e -> {
            Platform.exit(); // Ferme toutes les fenetres JavaFX
            System.exit(0);  // Termine l'application proprement
        });


        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Menu Principal - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    public static String getProfileName() {
        return pseudo;
    }

    public static Profile getProfile() {
        return profile;
    }
}