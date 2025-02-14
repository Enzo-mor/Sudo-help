package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class ProfileSelection {
    private final List<Profile> profiles = new ArrayList<>();
    private Profile selectedProfile = new Profile("Invité", 1);
    private int currentPage = 0;
    private static int idNum = 2;

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

        navigation.autosize();

        Button guestButton = new ProfileButton("Continuer en tant qu'invité");
        
        guestButton.setOnAction(e -> MainMenu.showMainMenu(stage, "Invité"));
        
        Button quitButton = new ProfileButton("Quitter");
        quitButton.setOnAction(e -> {
            Platform.exit();
        });

        Button addProfileButton = new ProfileButton("Ajouter un profil");

        addProfileButton.setOnAction(e -> {
            // Créer une nouvelle petite fenêtre pop-up (fenêtre modale)
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquer les interactions avec la fenêtre principale
            popupStage.setTitle("Créer un profil"); // Définir le titre de la fenêtre

            // Composants de l'interface utilisateur
            Label label = new Label("Entrez le nom du profil :"); // Libellé au-dessus du champ de texte
            TextField nameField = new TextField(); // Champ de texte pour la saisie du nom du profil
            Button confirmButton = new Button("Créer"); // Bouton pour confirmer la création du profil
            // Gérer l'événement lors du clic sur le bouton "Créer"
            confirmButton.setOnAction(event -> {
                String name = nameField.getText().trim(); // Récupérer le texte saisi et supprimer les espaces inutiles
                if (!name.isEmpty()) {
                    profiles.add(new Profile(name, idNum++)); // Ajouter un nouveau profil avec un identifiant unique
                    System.out.println("Nouveau profil ajouté : " + name); // Afficher un message dans la console
                    updateProfiles(profileContainer, leftArrow, rightArrow, stage); // Mettre à jour l'affichage des profils
                    popupStage.close(); // Fermer la fenêtre pop-up après l'ajout du profil
                }
            });

            // Créer une mise en page verticale avec un espacement entre les éléments
            VBox popupLayout = new VBox(10, label, nameField, confirmButton);
            popupLayout.setPadding(new Insets(10)); // Ajouter une marge pour une meilleure apparence

            // Créer et appliquer la scène à la fenêtre pop-up
            Scene scene = new Scene(popupLayout, 300, 150);
            popupStage.setScene(scene);

            // Afficher la fenêtre pop-up et attendre l'interaction de l'utilisateur
            popupStage.showAndWait();
        });


        layout.getChildren().addAll(titleLabel, navigation, addProfileButton, guestButton, quitButton);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Choix du Profil");
        stage.setScene(scene);
        stage.show();
    }

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

            Label profileName = new Label(profiles.get(i).getName());
            profileName.setStyle("-fx-font-size: 16px;");

            profileBox.getChildren().addAll(profileImage, profileName);
            profileContainer.getChildren().add(profileBox);

            final Profile profile = profiles.get(i);
            profileBox.setOnMouseClicked(e -> {
                selectedProfile = profile;
                MainMenu.showMainMenu(stage, selectedProfile.getName());
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == profiles.size());
    }
}
