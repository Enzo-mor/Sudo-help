package com.example;

import com.bdd.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class SudokuGame {
    private static final double MIN_WIDTH = 600;
    private static final double MIN_HEIGHT = 500;
    private static SudokuTimer sudokuTimer; // Chronomètre

    public static void showSudokuGame(Stage primaryStage, int selectedSudokuId) {

        // Initialiser le chronomètre avec une valeur arbitraire (ex: 120 secondes)
        sudokuTimer = new SudokuTimer(120);
        sudokuTimer.startTimer(); // Démarrer le chronomètre
        
        // Bouton retour (Maison)
        Button homeButton = new Button();
        ImageView homeIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeButton.setGraphic(homeIcon);
        homeButton.setStyle("-fx-background-color: transparent;");
        homeButton.setOnAction(e -> showExitDialog(primaryStage));

        // Créer le topBar HBox avec le bouton Home et le Timer à droite
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));

        // Ajouter le bouton Home à la gauche de la barre
        topBar.getChildren().add(homeButton);

        // Créer un espace flexible qui va pousser le timer à droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // L'espace prend tout l'espace restant
        topBar.getChildren().add(spacer);

        // Ajouter le timer à droite
        topBar.getChildren().add(sudokuTimer.getTimerDisplay());

        // Créer le panneau de sélection des chiffres
        NumberSelection numberSelection = new NumberSelection();
        Grid gridSudoku = DBManager.getGrid(selectedSudokuId);
        ToolsPanel toolsPanel = new ToolsPanel();
        SudokuGrid grid = new SudokuGrid(numberSelection, gridSudoku, toolsPanel);
        grid.setGrid();
        ControlButtons controlsButtons = new ControlButtons(grid);

        VBox rightPanel = new VBox(20);
        rightPanel.getChildren().addAll(numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
        HBox layout = new HBox(20);
        layout.getChildren().addAll(grid.getGridPane(), rightPanel);

        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, layout);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Sudoku - Niveau facile n°" + selectedSudokuId);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private static void showExitDialog(Stage primaryStage) {
        sudokuTimer.pauseTimer(); // Arrêter le chrono avant de quitter

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Retour au menu");
        alert.setHeaderText("Voulez-vous retourner au menu de sélection ou au menu principal ?");
    
        Button menuSelectButton = new ProfileButton("Menu Sélection");
        Button menuButton = new ProfileButton("Menu Principal");
        Button cancelButton = new ProfileButton("Annuler");
    
        menuSelectButton.setOnAction(e -> {
            alert.setResult(ButtonType.OK);
            alert.close();
            SudokuMenu.showSudokuLibrary(primaryStage);
        });
    
        menuButton.setOnAction(e -> {
            alert.setResult(ButtonType.OK);
            alert.close();
            MainMenu.showMainMenu(primaryStage, MainMenu.getProfileName());
        });
    
        cancelButton.setOnAction(e -> {
            sudokuTimer.resumeTimer(); // Reprendre le chrono
            alert.setResult(ButtonType.CANCEL);
            alert.close();
        });
    
        HBox buttons = new HBox();
        buttons.getChildren().addAll(menuSelectButton, menuButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
    
        alert.getDialogPane().setContent(buttons);
    
        alert.showAndWait();
    }
}