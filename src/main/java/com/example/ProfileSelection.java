package com.example;

import java.util.ArrayList;
import java.util.List;

public class ProfileSelection {
    private final List<Profile> profiles = new ArrayList<>();
    private Profile selectedProfile = new Profile("Invité", 1); //String à changer par Profile
    private int currentPage = 0;
    private static int idNum = 2;

    /*private void showProfileSelection(Stage stage) {
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

        Button guestButton = new Button("Continuer en tant qu'invité");
        guestButton.setOnAction(e -> showMainMenu(stage, "Invité"));

        Button addProfileButton = new Button("Ajouter un profil");
        addProfileButton.setOnAction(e -> {
            String newProfile = "Profil" + (profiles.size() + 1);
            profiles.add(new Profile("newProfile", idNum++));
            System.out.println("Nouveau profil ajouté : " + newProfile);
            updateProfiles(profileContainer, leftArrow, rightArrow, stage);
        });

        layout.getChildren().addAll(titleLabel, navigation, addProfileButton, guestButton);
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

            ImageView profileImage = new ImageView(new Image(getClass().getResource("/profil.jpg").toExternalForm()));
            profileImage.setFitWidth(100);
            profileImage.setFitHeight(100);

            Label profileName = new Label(profiles.get(i).getName());
            profileName.setStyle("-fx-font-size: 16px;");

            profileBox.getChildren().addAll(profileImage, profileName);
            profileContainer.getChildren().add(profileBox);

            final Profile profile = profiles.get(i); // Local variable for the profile
            profileBox.setOnMouseClicked(e -> {
                selectedProfile = profile; // Assign to the global variable
                showMainMenu(stage, selectedProfile);
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == profiles.size());
    }*/
}
