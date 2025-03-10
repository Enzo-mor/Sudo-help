package grp6.intergraph;

import grp6.sudocore.DBManager;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

class Settings extends Stage {

    private boolean settingsMode = false;
    private final RotateTransition rotateAnimation;

    public Settings(ImageView gearIcon) {
        setTitle("Paramètres");

        // --- HBox pour afficher/modifier le pseudo ---
        TextField usernameField = new TextField(MainMenu.getProfileName());
        usernameField.setPromptText("ex: David"); // Texte d'invite
        
        // Ajouter un contour visible au champ de texte pour le rendre cliquable
        usernameField.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 2px;");

        // Désactiver l'édition par défaut
        usernameField.setEditable(false);

        // Image d'icône d'édition
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);

        // Placer le TextField et l'icône d'édition dans une HBox
        HBox usernameBox = new HBox(10, usernameField, editIcon);
        usernameBox.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        // --- Gestion de l'édition du pseudo ---

        boolean[] isEditing = {false};
        // Action lors du clic sur l'icône
        editIcon.setOnMouseClicked(e -> {
            if (isEditing[0]) {
                usernameField.setEditable(false);  // Désactivation de la modification du texte
                usernameField.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 2px;");
            } else {
                // Activer l'édition du TextField
                usernameField.setEditable(true);  // Activation de la modification du texte
                usernameField.requestFocus(); // Place le focus dans le champ de texte
                usernameField.selectAll(); // Sélectionne tout le texte pour le remplacer directement
                usernameField.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-border-color: blue; -fx-border-width: 2px;"); // Changer la bordure pour montrer qu'on peut éditer
            }

            // Alterner l'état de l'édition
            isEditing[0] = !isEditing[0];
        });

        // Vérification du texte du TextField lorsque l'utilisateur appuie sur "Entrée"
        usernameField.setOnAction(e -> {
            String newName = usernameField.getText();
            if (!newName.isEmpty() && DBManager.renameProfile(MainMenu.getProfile(), newName)) {
                MainMenu.getProfile().setPseudo(newName); // Met à jour le nom dans le programme
            }

            // Désactiver l'édition et revenir à un style normal
            usernameField.setEditable(false);
            usernameField.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-border-color: darkgray; -fx-border-width: 2px;");
            isEditing[0] = false;
        });

        // --- Paramètres ---
        CheckBox helpNotifications = new CheckBox("Notifications d'aide");
        CheckBox fullscreenMode = new CheckBox("Mode plein écran");
        CheckBox gridHighlight = new CheckBox("Surbrillance de la grille");
        CheckBox numberHighlight = new CheckBox("Surbrillance des chiffres");

        Button changeProfileButton = new Button("Changer de profil");
        Button deleteProfileButton = new Button("Supprimer profil");
        deleteProfileButton.setStyle("-fx-text-fill: red;");

        VBox settingsLayout = new VBox(10, usernameBox, helpNotifications, fullscreenMode, gridHighlight, numberHighlight, changeProfileButton, deleteProfileButton);
        settingsLayout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(settingsLayout, 250, 300);
        setScene(scene);

        // --- Initialisation de l'animation ---
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setCycleCount(1);
    }

    // --- Fonction pour gérer l'ouverture/fermeture des paramètres ---
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
}