package grp6.intergraph;

import grp6.sudocore.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
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
     * Fenetre d'ajout d'un nouveau profil
     */
    private static VBox createBox;

    /**
     * Fenetre d'information de profile supprimer
     */
    private static VBox profileDeleteMessage;

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
        StyledContent.applyContentBoxStyle(navigation);
        navigation.setMaxWidth(500);
        navigation.setAlignment(Pos.CENTER);
        navigation.setPadding(new Insets(10));
        
        Button addProfileButton = new Button("Ajouter un profil");
        StyledContent.applyButtonBoxStyle(addProfileButton);
        addProfileButton.setOnAction(e -> showAddProfilePopup(stage, profileContainer, leftArrow, rightArrow));
        
        Button guestButton = new Button("Continuer en tant qu'invité");
        StyledContent.applyButtonBoxStyle(guestButton);
        guestButton.setOnAction(e -> {
            instance = null;
            MainMenu.showMainMenu(stage, selectedProfile);
        });
        
        Button quitButton = new Button("Quitter");
        StyledContent.applyButtonBoxStyle(quitButton);
        quitButton.setOnAction(e -> Platform.exit());
        
        Button deleteButton = new Button("Supprimer BDD");
        StyledContent.applyButtonBoxStyle(deleteButton);
        
        VBox layout = new VBox(20, titleLabel, navigation, addProfileButton, guestButton, quitButton, deleteButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        
        createBox = new VBox(10);
        createBox.setAlignment(Pos.CENTER);
        createBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        createBox.setVisible(false);
        
        profileDeleteMessage = new VBox(10);
        profileDeleteMessage.setAlignment(Pos.CENTER);
        profileDeleteMessage.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        profileDeleteMessage.setVisible(false);
        
        VBox deleteBox = new VBox();
        deleteBox.setAlignment(Pos.CENTER);
        deleteBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        deleteBox.setVisible(false);
        
        Label textErase = new Label("Vous vous apprêtez à supprimer votre base de données et allez quitter le jeu !\n Voulez vous continuez ?");
        textErase.setWrapText(true);
        textErase.setTextAlignment(TextAlignment.CENTER);
        textErase.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #343a40;");

        deleteButton.setOnAction(e -> {
            deleteBox.setVisible(true);
        });
        
        Button confirmDeleteButton = new Button("Continuer");
        StyledContent.applyButtonWarningStyle(confirmDeleteButton);
        confirmDeleteButton.setOnAction(e -> {
            try {
                DBManager.delete();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        });

        Button cancelButton = new Button("Annuler");
        StyledContent.applyButtonStyle(cancelButton);
        cancelButton.setOnAction(e -> deleteBox.setVisible(false));
        
        HBox buttons = new HBox(10, confirmDeleteButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(10);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.getChildren().addAll(textErase, buttons);

        deleteBox.getChildren().addAll(contentBox);
        
        StackPane stackLayout = new StackPane(layout, createBox, profileDeleteMessage, deleteBox);

        // --- BorderPane pour organiser la mise en page ---
        BorderPane root = new BorderPane();
        root.setCenter(stackLayout);

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
     * Affiche un panneau de creation de profil.
     * Cette methode permet a l'utilisateur d'ajouter un nouveau profil en saisissant un nom.
     * 
     * @param parentStage Fenetre principale de l'application [Stage].
     * @param profileContainer Conteneur des profils [HBox].
     * @param leftArrow Bouton gauche de navigation [Button].
     * @param rightArrow Bouton droit de navigation [Button].
     */
    private void showAddProfilePopup(Stage parentStage, HBox profileContainer, Button leftArrow, Button rightArrow) {

        // Vider createBox avant d'ajouter du contenu
        createBox.getChildren().clear(); 

        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        // Création du message d'invite
        Label messageLabel = new Label("Entrez le nom du profil :");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #343a40;");

        // Champ de saisie
        TextField nameField = new TextField();
        nameField.setTextFormatter(new TextFormatter<>(change -> 
            change.getControlNewText().length() > 20 ? null : change
        ));
        nameField.setPromptText("ex : David");
        StyledContent.styleTextField(nameField);

        // Bouton de confirmation
        Button confirmButton = new Button("Créer");
        StyledContent.applyButtonStyle(confirmButton);

        confirmButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            try {
                if (!name.isEmpty() && !DBManager.profileExists(name)) {
                    Profile newProfile = new Profile(name);
                    profiles.add(newProfile);
                    DBManager.saveProfile(newProfile);
                    updateProfiles(profileContainer, leftArrow, rightArrow, parentStage);
                }
            } catch (SQLException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
            createBox.setVisible(false);
        });

        // Permet d'appuyer sur Entrée pour valider
        nameField.setOnAction(e -> confirmButton.fire());

        // Bouton d'annulation
        Button cancelButton = new Button("Annuler");
        StyledContent.applyButtonStyle(cancelButton);
        cancelButton.setOnAction(e -> createBox.setVisible(false));

        // Conteneur pour les boutons
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        contentBox.getChildren().addAll(messageLabel, nameField, buttonBox);

        createBox.getChildren().addAll(contentBox);
        createBox.setVisible(true);
    }

    /**
     * Affiche un panneau d'indication pour signifier que le profile passe en parametre a ete supprime
     * 
     * @param profileName Profile supprime [Profile]
     */
    public static void profileDeleteMessage(String profileName) {
        // Vider le message précédent avant d'ajouter un nouveau
        profileDeleteMessage.getChildren().clear();

        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        Label message = new Label("Le profil '" + profileName + "' a été supprimé !");
        message.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        contentBox.getChildren().addAll(message);

        profileDeleteMessage.getChildren().add(contentBox);
        profileDeleteMessage.setVisible(true);

        // Cacher le message après 1 seconde
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> profileDeleteMessage.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }
}