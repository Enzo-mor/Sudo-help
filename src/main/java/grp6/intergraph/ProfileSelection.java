package grp6.intergraph;

import grp6.sudocore.*;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * Classe ProfileSelection
 * Cette classe gere l'affichage et la selection des profils de l'application Sudoku.
 * Elle permet a l'utilisateur de choisir un profil existant, d'en creer un nouveau
 * ou de continuer en tant qu'invite.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see Profile
 * @see DBManager
 * @see StyledContent
 * @see MainMenu
 */
public class ProfileSelection {

    /** 
     * Liste des profils disponibles 
     */
    private final List<Profile> profiles = DBManager.getProfiles();

    /** 
     * Profil selectionne par defaut 
     */
    private Profile selectedProfile = new Profile("Invité");

    /** 
     * Page actuelle pour l'affichage des profils 
     */
    private int currentPage = 0;

    /** 
     * Instance unique de ProfileSelection 
     */
    private static ProfileSelection instance = null;


    /**
     * Constructeur prive pour le singleton
     */
    private ProfileSelection() {}

    /**
     * Retourne l'instance unique de ProfileSelection.
     * 
     * @return Instance de ProfileSelection [ProfileSelection]
     */
    public static ProfileSelection getInstance() {
        if (instance == null) {
            instance = new ProfileSelection();
        }
        return instance;
    }

    /**
     * Affiche la fenetre de selection du profil.
     * 
     * @param stage Fenetre principale de l'application [Stage]
     */
    public void showProfileSelection(Stage stage) {
        StackPane root = new StackPane();
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Choisissez votre profil");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox profileContainer = new HBox(20);
        profileContainer.setAlignment(Pos.CENTER);

        Button leftArrow = new Button("<");
        leftArrow.setDisable(true);
        Button rightArrow = new Button(">");

        StyledContent.applyArrowButtonStyle(leftArrow);
        StyledContent.applyArrowButtonStyle(rightArrow);

        leftArrow.setOnAction(e -> {
            currentPage--;
            updateProfiles(profileContainer, leftArrow, rightArrow, stage);
        });

        rightArrow.setOnAction(e -> {
            currentPage++;
            updateProfiles(profileContainer, leftArrow, rightArrow, stage);
        });

        updateProfiles(profileContainer, leftArrow, rightArrow, stage);
        
        HBox navigation = new HBox(10, leftArrow, profileContainer, rightArrow);
        navigation.setAlignment(Pos.CENTER);
        navigation.setStyle("-fx-background-color: #939cb5;");
        
        Button guestButton = new Button("Continuer en tant qu'invité");
        guestButton.setOnAction(e -> {
            instance = null;
            MainMenu.showMainMenu(stage, selectedProfile);
        });
        
        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> Platform.exit());

        Button addProfileButton = new Button("Ajouter un profil");
        addProfileButton.setOnAction(e -> showAddProfilePopup(stage, profileContainer, leftArrow, rightArrow));
        
        StyledContent.applyButtonBoxStyle(guestButton);
        StyledContent.applyButtonBoxStyle(quitButton);
        StyledContent.applyButtonBoxStyle(addProfileButton);
        
        layout.getChildren().addAll(titleLabel, navigation, addProfileButton, guestButton, quitButton);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Choix du Profil");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Met a jour l'affichage des profils disponibles.
     * 
     * @param profileContainer Conteneur des profils [HBox]
     * @param leftArrow Bouton de navigation vers la gauche [Button]
     * @param rightArrow Bouton de navigation vers la droite [Button]
     * @param stage Fenetre principale [Stage]
     */
    private void updateProfiles(HBox profileContainer, Button leftArrow, Button rightArrow, Stage stage) {
        profileContainer.getChildren().clear();
        int profilesPerPage = 3;
        int startIndex = currentPage * profilesPerPage;
        int endIndex = Math.min(startIndex + profilesPerPage, profiles.size());

        for (int i = startIndex; i < endIndex; i++) {
            VBox profileBox = new VBox(10);
            profileBox.setAlignment(Pos.CENTER);
            ImageView profileImage = new ImageView(new Image(getClass().getResource("/profil.png").toExternalForm()));
            profileImage.setFitWidth(100);
            profileImage.setFitHeight(100);
            Label profileName = new Label(profiles.get(i).getPseudo());
            profileName.setStyle("-fx-font-size: 16px;");
            profileBox.getChildren().addAll(profileImage, profileName);
            profileContainer.getChildren().add(profileBox);

            final Profile profile = profiles.get(i);
            profileBox.setOnMouseClicked(e -> {
                selectedProfile = profile;
                instance = null;
                MainMenu.showMainMenu(stage, selectedProfile);
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == profiles.size());
    }

    /**
     * Affiche une fenetre pop-up pour ajouter un nouveau profil.
     * 
     * @param stage Fenetre principale [Stage]
     * @param profileContainer Conteneur des profils [HBox]
     * @param leftArrow Bouton gauche de navigation [Button]
     * @param rightArrow Bouton droit de navigation [Button]
     */
    private void showAddProfilePopup(Stage stage, HBox profileContainer, Button leftArrow, Button rightArrow) {
        
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Créer un profil");

        Label label = new Label("Entrez le nom du profil :");
        TextField nameField = new TextField();
        nameField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().length() > 20 ? null : change));
        Button confirmButton = new Button("Créer");
        confirmButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            try {
                if (!name.isEmpty() && !DBManager.profileExists(name)) {
                    Profile newProfile = new Profile(name);
                    profiles.add(newProfile);
                    DBManager.saveProfile(newProfile);
                    updateProfiles(profileContainer, leftArrow, rightArrow, stage);
                }
            } catch (SQLException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
            popupStage.close();
        });

        nameField.setOnAction(e -> confirmButton.fire());
        VBox popupLayout = new VBox(10, label, nameField, confirmButton);
        popupLayout.setPadding(new Insets(10));
        popupStage.setScene(new Scene(popupLayout, 300, 150));
        popupStage.showAndWait();
    }
}
