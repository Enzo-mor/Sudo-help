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

public class ProfileSelection {
    private final List<Profile> profiles = DBManager.getProfiles();
    private Profile selectedProfile = new Profile("Invité");
    private int currentPage = 0;
    private static ProfileSelection instance = null;

    private ProfileSelection() {}

    public static ProfileSelection getInstance() {
        if (instance == null) {
            instance = new ProfileSelection();
        }
        return instance;
    }

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

        navigation.autosize();

        Button guestButton = new ProfileButton("Continuer en tant qu'invité");
        
        guestButton.setOnAction(e -> {
            instance=null;
            MainMenu.showMainMenu(stage, selectedProfile);
            });
        
        Button quitButton = new ProfileButton("Quitter");
        quitButton.setOnAction(e -> {
            Platform.exit();
        });

        Button addProfileButton = new ProfileButton("Ajouter un profil");

        StyledContent.applyButtonBoxStyle(guestButton);
        StyledContent.applyButtonBoxStyle(quitButton);
        StyledContent.applyButtonBoxStyle(addProfileButton);

        addProfileButton.setOnAction(e -> {
            // Creer une nouvelle petite fenetre pop-up (fenetre modale)
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquer les interactions avec la fenetre principale
            popupStage.setTitle("Créer un profil"); // Definir le titre de la fenetre

            // Composants de l'interface utilisateur
            Label label = new Label("Entrez le nom du profil :"); // Libelle au-dessus du champ de texte
            TextField nameField = new TextField(); // Champ de texte pour la saisie du nom du profil

            // Limiter la longueur du texte à 20 caractères
            TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                if (change.getControlNewText().length() > 20) {
                    return null; // Refuser l'ajout si la longueur dépasse 20
                }
                return change;
            });
            nameField.setTextFormatter(textFormatter);

            Button confirmButton = new Button("Créer"); // Bouton pour confirmer la creation du profil

            // Gerer l'evenement lors du clic sur le bouton "Creer"
            confirmButton.setOnAction(event -> {
                String name = nameField.getText().trim(); // Recuperer le texte saisi et supprimer les espaces inutiles
                if (!name.isEmpty()) {
                    try{
                        if (DBManager.profileExists(name)){
                            System.out.println("Le profil " + name + " existe déjà");
                        }
                        else{
                            Profile newProf= new Profile(name);
                            profiles.add(newProf); // Ajouter un nouveau profil avec un identifiant unique
                            try {
                                DBManager.saveProfile(newProf);
                            }
                            catch (SQLException err){
                                System.out.println("Impossible d'accéder à la BDD");
                            }
                            System.out.println("Nouveau profil ajouté : " + name); // Afficher un message dans la console
                            updateProfiles(profileContainer, leftArrow, rightArrow, stage); // Mettre a jour l'affichage des profils
                        }
                    }
                    catch(SQLException er){
                        System.out.println("Impossible d'accéder à la BDD");
                    }
                    popupStage.close(); // Fermer la fenetre pop-up apres l'ajout du profil
                }
            });

            // Ajouter un gestionnaire d'événements pour détecter la touche "Entrée"
            nameField.setOnAction(event -> confirmButton.fire()); // Lorsque "Entrée" est pressée, déclencher le bouton

            // Creer une mise en page verticale avec un espacement entre les elements
            VBox popupLayout = new VBox(10, label, nameField, confirmButton);
            popupLayout.setPadding(new Insets(10)); // Ajouter une marge pour une meilleure apparence

            // Creer et appliquer la scene a la fenetre pop-up
            Scene scene = new Scene(popupLayout, 300, 150);
            popupStage.setScene(scene);

            // Afficher la fenetre pop-up et attendre l'interaction de l'utilisateur
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

            Label profileName = new Label(profiles.get(i).getPseudo());
            profileName.setStyle("-fx-font-size: 16px;");

            profileBox.getChildren().addAll(profileImage, profileName);
            profileContainer.getChildren().add(profileBox);

            final Profile profile = profiles.get(i);
            profileBox.setOnMouseClicked(e -> {
                selectedProfile = profile;
                instance=null;
                MainMenu.showMainMenu(stage, selectedProfile);
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == profiles.size());
    }
}
