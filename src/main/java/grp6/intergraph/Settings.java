package grp6.intergraph;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Settings extends Stage {
    public Settings() {
        setTitle("Paramètres");

        Label usernameLabel = new Label(MainMenu.getProfileName());
        usernameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        CheckBox helpNotifications = new CheckBox("Notifications d'aide");
        CheckBox fullscreenMode = new CheckBox("Mode plein écran");
        CheckBox gridHighlight = new CheckBox("Surbrillance de la grille");
        CheckBox numberHighlight = new CheckBox("Surbrillance des chiffres");

        Button changeProfileButton = new Button("Changer de profil");
        Button deleteProfileButton = new Button("Supprimer profil");
        deleteProfileButton.setStyle("-fx-text-fill: red;");

        VBox settingsLayout = new VBox(10, usernameLabel, helpNotifications, fullscreenMode, gridHighlight, numberHighlight, changeProfileButton, deleteProfileButton);
        settingsLayout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(settingsLayout, 250, 300);
        setScene(scene);
    }
}