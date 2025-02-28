package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private int currentPage = 0;
    //private final List<String> profiles = new ArrayList<>();
    private final List<Sudoku> sudokus = new ArrayList<>();
    //private String selectedProfile = "Invité";
    private int selectedNumber = 0;

    @Override
    public void start(Stage stage) {
        // Sample profiles (to simulate a database)
        profiles.add("Alice");
        profiles.add("Bob");
        profiles.add("Charlie");
        profiles.add("Diana");

        // Sample Sudoku data
        populateSudokus();

        showProfileSelection(stage);
    }

    private void populateSudokus() {
        for (int i = 1; i <= 12; i++) {
            sudokus.add(new Sudoku("Sudoku " + i, "Meilleur temps: " + (i * 10) + "s", "Score: " + (i * 50), i % 2 == 0 ? "Fini" : "Non fini"));
        }
    }

    private void showProfileSelection(Stage stage) {
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
            profiles.add(newProfile);
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

            Label profileName = new Label(profiles.get(i));
            profileName.setStyle("-fx-font-size: 16px;");

            profileBox.getChildren().addAll(profileImage, profileName);
            profileContainer.getChildren().add(profileBox);

            final String profile = profiles.get(i); // Local variable for the profile
            profileBox.setOnMouseClicked(e -> {
                selectedProfile = profile; // Assign to the global variable
                showMainMenu(stage, selectedProfile);
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == profiles.size());
    }

    private void showMainMenu(Stage stage, String profileName) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + profileName);
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Jouer");
        startGameButton.setOnAction(e -> showSudokuLibrary(stage));

        Button settingsButton = new Button("Paramètres");
        settingsButton.setOnAction(e -> System.out.println("Paramètres ouverts"));

        Button exitButton = new Button("Quitter");
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(welcomeLabel, startGameButton, settingsButton, exitButton);

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Menu Principal");
        stage.setScene(scene);
        stage.show();
    }

    private void showSudokuLibrary(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        int columns = 4;
        for (int i = 0; i < sudokus.size(); i++) {
            Sudoku sudoku = sudokus.get(i);

            VBox sudokuBox = new VBox(10);
            sudokuBox.setAlignment(Pos.CENTER);
            sudokuBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #F0F0F0;");

            Label nameLabel = new Label(sudoku.getName());
            Label bestTimeLabel = new Label(sudoku.getBestTime());
            Label scoreLabel = new Label(sudoku.getScore());
            Label statusLabel = new Label(sudoku.getStatus());

            sudokuBox.getChildren().addAll(nameLabel, bestTimeLabel, scoreLabel, statusLabel);

            gridPane.add(sudokuBox, i % columns, i / columns);

            final int selectedSudokuId = i + 1; // Crée une copie finale
            sudokuBox.setOnMouseClicked(e -> showSudokuGame(stage, selectedSudokuId));
        }

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> showMainMenu(stage, selectedProfile));

        VBox layout = new VBox(20, gridPane, backButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Bibliothèque de Sudoku");
        stage.setScene(scene);
        stage.show();
    }

    private void showSudokuGame(Stage stage, int sudokuId) {
        BorderPane root = new BorderPane(); // Utilisation d'une disposition BorderPane
    
        // ---- Grille Sudoku (à gauche) ----
        GridPane sudokuGrid = new GridPane();
        sudokuGrid.setPadding(new Insets(20));
        sudokuGrid.setHgap(5);
        sudokuGrid.setVgap(5);
        sudokuGrid.setAlignment(Pos.CENTER);
        sudokuGrid.setStyle("-fx-border-color: black; -fx-background-color: #F8F8F8;");
    
        // Création de la grille 9x9 (pré-remplie pour cet exemple)
        int[][] sudokuData = new int[9][9]; // À remplacer par les données réelles du Sudoku
        Label[][] cells = new Label[9][9]; // Utilisation de labels pour éviter la saisie
    
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Label cell = new Label();
                cell.setPrefSize(40, 40);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: gray; -fx-font-size: 16px; -fx-background-color: white;");
    
                if (sudokuData[row][col] != 0) {
                    cell.setText(String.valueOf(sudokuData[row][col]));
                    cell.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
                }
    
                final int r = row, c = col; // Final variables pour la lambda
                cell.setOnMouseClicked(e -> {
                    // Insérer le chiffre sélectionné dans la case cliquée
                    if (selectedNumber != 0) {
                        cell.setText(String.valueOf(selectedNumber));
                    }
                });
    
                cells[row][col] = cell;
                sudokuGrid.add(cell, col, row);
            }
        }
    
        root.setLeft(sudokuGrid);
    
        // ---- Chiffres à sélectionner (à droite, sur 2 lignes) ----
        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(20));
        rightPane.setAlignment(Pos.TOP_CENTER);
    
        Label numbersLabel = new Label("Chiffres");
        numbersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        GridPane numberGrid = new GridPane();
        numberGrid.setHgap(10);
        numberGrid.setVgap(10);
        numberGrid.setAlignment(Pos.CENTER);
    
        // Création des boutons pour les chiffres de 1 à 9
        int buttonSize = 50; // Taille des boutons
        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefSize(buttonSize, buttonSize);
            numberButton.setStyle("-fx-font-size: 16px;");
    
            // Action : Permet de sélectionner un chiffre
            final int number = i; // Final pour utilisation dans la lambda
            numberButton.setOnAction(e -> {
                selectedNumber = number; // Définir le chiffre sélectionné
            });
    
            // Ajouter les boutons dans la grille (2 lignes)
            if (i <= 5) {
                numberGrid.add(numberButton, i - 1, 0); // Ligne 0
            } else {
                numberGrid.add(numberButton, i - 6, 1); // Ligne 1
            }
        }
    
        // ---- Zone d'annotations ----
        Label annotationsLabel = new Label("Annotations");
        annotationsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        TextArea annotationsArea = new TextArea();
        annotationsArea.setPrefSize(200, 200);
        annotationsArea.setPromptText("Ajoutez vos annotations ici...");
        annotationsArea.setStyle("-fx-font-size: 14px;");
    
        rightPane.getChildren().addAll(numbersLabel, numberGrid, annotationsLabel, annotationsArea);
        root.setRight(rightPane);
    
        // ---- Bouton Retour ----
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> showSudokuLibrary(stage));
    
        VBox bottomPane = new VBox(backButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(10));
        root.setBottom(bottomPane);
    
        // ---- Affichage de la scène ----
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Jeu de Sudoku");
        stage.setScene(scene);
        stage.show();
    }
    



    public static void main(String[] args) {
        launch();
    }

    private static class Sudoku {
        private final String name;
        private final String bestTime;
        private final String score;
        private final String status;

        public Sudoku(String name, String bestTime, String score, String status) {
            this.name = name;
            this.bestTime = bestTime;
            this.score = score;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getBestTime() {
            return bestTime;
        }

        public String getScore() {
            return score;
        }

        public String getStatus() {
            return status;
        }
    }
}