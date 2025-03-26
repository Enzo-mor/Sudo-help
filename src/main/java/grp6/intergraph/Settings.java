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

/**
 * Classe Settings
 * Cette classe gere l'affichage et les interactions de la fenetre des parametres.
 * Elle permet de modifier les preferences utilisateur telles que le pseudo, 
 * les options d'affichage et la gestion des profils.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see DBManager
 * @see SudokuGrid
 * @see SudokuDisplay
 * @see ProfileSelection
 */
class Settings extends Stage {

    /** 
     * Indique si la fenetre des parametres est ouverte 
     */
    private boolean settingsMode = false;

    /** 
     * Indique si le marquage des lignes/colonnes est active 
     */
    private static boolean highlightRowCol = false;

    /** 
     * Indique si le marquage des chiffres est active 
     */
    private static boolean highlightNumbers = false;

    /** 
     * Animation de rotation pour l'icône des parametres 
     */
    private final RotateTransition rotateAnimation;

    /** 
     * Instance unique de Settings 
     */
    private static Settings instance = null;


    /**
     * Constructeur prive de la fenetre des parametres.
     * 
     * @param stage Fenetre principale [Stage]
     * @param gearIcon Icône d'engrenage pour l'animation [ImageView]
     */
    private Settings(Stage stage, ImageView gearIcon) {

        setTitle("Paramètres");

        TextField usernameField = new TextField(MainMenu.getProfileName());
        usernameField.setTextFormatter(new TextFormatter<>(change -> 
            change.getControlNewText().length() > 20 ? null : change)
        );
        usernameField.setPromptText("ex : David");
        StyledContent.styleTextField(usernameField);

        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        editIcon.setFitWidth(20);
        editIcon.setFitHeight(20);

        HBox usernameBox = new HBox(10, usernameField, editIcon);
        usernameBox.setStyle("-fx-padding: 5px; -fx-alignment: center-left;");

        usernameField.setOnAction(e -> {
            String newName = usernameField.getText();
            if (!newName.isEmpty() && DBManager.renameProfile(MainMenu.getProfile(), newName)) {
                MainMenu.getProfile().setPseudo(newName);
            }
        });

        ToggleSwitch toggleFullscreen = new ToggleSwitch("Mode plein écran", stage.isFullScreen());
        ToggleSwitch toggleHighlightRowCol = new ToggleSwitch("Marquage lignes/colonnes", highlightRowCol);
        ToggleSwitch toggleHighlightNumbers = new ToggleSwitch("Marquage des chiffres", highlightNumbers);
        Button changeProfileButton = new Button("Changer de profil");
        Button deleteProfileButton = new Button("Supprimer profil");

        StyledContent.applyButtonStyle(changeProfileButton);
        StyledContent.applyButtonWarningStyle(deleteProfileButton);

        VBox.setVgrow(toggleFullscreen, Priority.ALWAYS);
        VBox.setVgrow(toggleHighlightRowCol, Priority.ALWAYS);
        VBox.setVgrow(toggleHighlightNumbers, Priority.ALWAYS);

        // Ajout d'un espaceur pour équilibrer la mise en page
        Region spacer = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        VBox settingsLayout = new VBox(10, usernameBox, spacer, toggleFullscreen, toggleHighlightRowCol, toggleHighlightNumbers, spacer2, changeProfileButton, deleteProfileButton);
        settingsLayout.setStyle("-fx-padding: 20px;");
        settingsLayout.setPrefHeight(300);
        
        // Gérer l'événement du mode plein écran
        toggleFullscreen.setOnToggleChanged((obs, oldState, newState) -> 
            Platform.runLater(() -> stage.setFullScreen(newState))
        );

        // Gérer l'événement du marquage lignes/colonnes
        toggleHighlightRowCol.setOnToggleChanged((obs, oldState, newState) -> {
            highlightRowCol = newState;
            if (!highlightRowCol) {
                if (SudokuGrid.getGridPane() != null)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            }
        });

        // Gérer l'événement du marquage des chiffres
        toggleHighlightNumbers.setOnToggleChanged((obs, oldState, newState) -> {
            highlightNumbers = newState;
            if (!highlightNumbers) {
                if (SudokuGrid.getGridPane() != null)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            }
        });
        
        // Gérer l'événement du bouton de changer de profil
        changeProfileButton.setOnAction(e -> {
            instance = null;
            ProfileSelection.getInstance().showProfileSelection(stage);
            this.close();
        });
        
        // Gérer l'événement du bouton de suppression de profil
        deleteProfileButton.setOnAction(e -> deleteProfile(stage, deleteProfileButton));
        

        setScene(new Scene(settingsLayout, 275, 300));
        setResizable(false);

        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setCycleCount(1);
    }

    /**
     * Gere l'ouverture et la fermeture de la fenetre des parametres.
     */
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

    /**
     * Retourne l'instance unique de Settings.
     * 
     * @param stage Fenetre principale [Stage]
     * @param gearIcon Icône d'engrenage pour l'animation [ImageView]
     * @return Instance unique de Settings [Settings]
     */
    public static Settings getInstance(Stage stage, ImageView gearIcon){
        if(instance == null){
            instance = new Settings(stage, gearIcon);
        }
        return instance;
    }

    /**
     * Supprime le profil actuel et affiche une confirmation.
     * 
     * @param stage Fenetre principale [Stage]
     * @param deleteProfileButton Bouton de suppression du profil [Button]
     */
    private void deleteProfile(Stage stage, Button deleteProfileButton) {
        try {
            String profileName = MainMenu.getProfileName();
            DBManager.deleteProfile(profileName);
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
            popupStage.setX(deleteProfileButton.getScene().getWindow().getX() + 200);
            popupStage.setY(deleteProfileButton.getScene().getWindow().getY() + 100);
            popupStage.show();

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> popupStage.close()));
            timeline.setCycleCount(1);
            timeline.play();

            instance = null;
            ProfileSelection.getInstance().showProfileSelection(stage);
            this.close();
        } catch (SQLException e) {
            System.err.println("Error deleting profile: " + e.getMessage());
        }
    }

    /**
     * Retourne l'etat du marquage des lignes/colonnes.
     * 
     * @return true si active, false sinon [boolean]
     */
    public static boolean getHighlightRowCol() {
        return highlightRowCol;
    }

    /**
     * Retourne l'etat du marquage des chiffres.
     * 
     * @return true si active, false sinon [boolean]
     */
    public static boolean getHighlightNumbers() {
        return highlightNumbers;
    }
}