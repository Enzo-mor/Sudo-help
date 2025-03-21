package grp6.intergraph;

import java.sql.SQLException;

import grp6.sudocore.DBManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

class Settings extends Stage {

    private boolean settingsMode = false;
    private static boolean highlightRowCol = false;
    private static boolean highlightNumbers = false;
    private final RotateTransition rotateAnimation;
    private static Settings instance = null;


    private Settings(Stage stage, ImageView gearIcon) {
        setTitle("Paramètres");

        // --- HBox pour afficher/modifier le pseudo ---
        TextField usernameField = new TextField(MainMenu.getProfileName());
        usernameField.setPromptText("ex: David"); // Texte d'invite
        
        // Ajouter un contour visible au champ de texte pour le rendre cliquable
        usernameField.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 2px;");

        // Image d'icone d'edition
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);

        // Placer le TextField et l'icone d'edition dans une HBox
        HBox usernameBox = new HBox(10, usernameField, editIcon);
        usernameBox.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        // --- Gestion de l'edition du pseudo ---

        // Verification du texte du TextField lorsque l'utilisateur appuie sur "Entree"
        usernameField.setOnAction(e -> {
            String newName = usernameField.getText();
            if (!newName.isEmpty() && DBManager.renameProfile(MainMenu.getProfile(), newName)) {
                MainMenu.getProfile().setPseudo(newName); // Met a jour le nom dans le programme
            }
        });

        // --- Parametres ---
        CheckBox helpNotifications = new CheckBox("Notifications d'aide");
        CheckBox fullscreenMode = new CheckBox("Mode plein écran");
        CheckBox checkHighlightRowCol = new CheckBox("Marquage des lignes/colonnes");
        CheckBox numberHighlight = new CheckBox("Marquage des chiffres");

        Button changeProfileButton = new Button("Changer de profil");
        Button deleteProfileButton = new Button("Supprimer profil");
        deleteProfileButton.setStyle("-fx-text-fill: red;");

        VBox settingsLayout = new VBox(10, usernameBox, helpNotifications, fullscreenMode, checkHighlightRowCol, numberHighlight, changeProfileButton, deleteProfileButton);
        settingsLayout.setStyle("-fx-padding: 20px;");

        // --- Gestion Boutons Parametres ---
        changeProfileButton.setOnAction(e -> {
            instance=null;
            ProfileSelection.getInstance().showProfileSelection(stage);
            this.close();
        });

        deleteProfileButton.setOnAction(e -> {
            try {
                String profileName = MainMenu.getProfileName();
                DBManager.deleteProfile(profileName);

                // Affichage d'une fenetre temporaire
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Suppression");

                Label message = new Label("Le profil '" + profileName + "' a été supprimé !");
                message.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

                VBox layout = new VBox(10, message);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(10));

                Scene scene = new Scene(layout, 300, 100);
                popupStage.setScene(scene);

                // Centrer par rapport a la fenetre principale
                popupStage.setX(deleteProfileButton.getScene().getWindow().getX() + 200);
                popupStage.setY(deleteProfileButton.getScene().getWindow().getY() + 100);

                popupStage.show();

                // Fermer automatiquement apres 2 secondes
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> popupStage.close()));
                timeline.setCycleCount(1);
                timeline.play();

                instance=null;
                ProfileSelection.getInstance().showProfileSelection(stage);
                this.close();
                
            } catch (SQLException e1) {
                System.err.println("Error deleting profile: " + e1.getMessage());
            }
        });

        // -- Button full screen mode --
        fullscreenMode.setOnAction(e -> {
            Platform.runLater(() -> {
                if (fullscreenMode.isSelected()) {
                    stage.setFullScreen(true);
                } else {
                    stage.setFullScreen(false);
                }
            });
        });

        // -- Button highlight row/col --
        checkHighlightRowCol.setOnAction(e -> {
            highlightRowCol = !highlightRowCol;
            if(!highlightRowCol)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
        });

        // -- Button number highlight --
        numberHighlight.setOnAction(e -> {
            highlightNumbers = !highlightNumbers;
            if(!highlightNumbers)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
        });

        Scene scene = new Scene(settingsLayout, 250, 300);
        setScene(scene);

        // --- Initialisation de l'animation ---
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setCycleCount(1);
    }

    // --- Fonction pour gerer l'ouverture/fermeture des parametres ---
    public void toggleSettingsWindow() {
        if (settingsMode) {
            this.close(); // Ferme cette instance
            settingsMode = false;

            rotateAnimation.setByAngle(-45);
            rotateAnimation.play();
        } else {
            this.show(); // Affiche cette instance
            this.setOnCloseRequest(e -> settingsMode = false);
            settingsMode = true;

            rotateAnimation.setByAngle(45);
            rotateAnimation.play();
        }
    }

    public static Settings getInstance(Stage stage, ImageView gearIcon){
        if(instance == null){
            instance = new Settings(stage, gearIcon);
        }
        return instance;
    }


    public static boolean getHighlightRowCol() {
        return highlightRowCol;
    }

    public static boolean getHighlightNumbers() {
        return highlightNumbers;
    }
}