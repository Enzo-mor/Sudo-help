package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.GameState;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class SudokuGame {
    private static final double MIN_WIDTH = 600;
    private static final double MIN_HEIGHT = 500;
    private static Game actualGame;

    public static void showSudokuGame(Stage primaryStage, Sudoku selectedSudoku) {

        Grid gridSudokuBase = DBManager.getGrid(selectedSudoku.getId());

        if (selectedSudoku.getStatus() == GameState.IN_PROGRESS && selectedSudoku.getGame() != null) {
            actualGame = selectedSudoku.getGame();
            actualGame.resumeGame();
        }
        
        else {
            try {
                actualGame = new Game(gridSudokuBase, MainMenu.getProfile());
            } catch (SQLException e) {
                System.out.println("Error when starting new game");
            }
            // Initialisation de la partie et du chronomètre 
            actualGame.startGame();
        }
        
        // Bouton retour (Maison)
        Button homeButton = new Button();
        ImageView homeIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeButton.setGraphic(homeIcon);
        homeButton.setStyle("-fx-background-color: transparent;");
        homeButton.setOnAction(e -> showExitDialog(primaryStage));

        // Creer le topBar HBox avec le bouton Home et le Timer à droite
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));

        // Ajouter le bouton Home à la gauche de la barre
        topBar.getChildren().add(homeButton);

        // Creer un espace flexible qui va pousser le timer à droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // L'espace prend tout l'espace restant
        topBar.getChildren().add(spacer);

        // Ajouter le timer à droite
        Label timerLabel =  new Label("00:00:00");
        actualGame.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText(timer)));

        topBar.getChildren().add(timerLabel);

        // Creer le panneau de selection des chiffres
        
        // 1. Creer le panneau des outils
        ToolsPanel toolsPanel = new ToolsPanel();

        // 2. Creer la selection des nombres SANS SudokuGrid
        NumberSelection numberSelection = new NumberSelection(toolsPanel);

        // 3. Creer la grille de Sudoku en lui passant NumberSelection
        SudokuGrid grid = new SudokuGrid(numberSelection, toolsPanel, actualGame);

        // 4. Associer la grille à NumberSelection maintenant qu'elle existe
        numberSelection.setSudokuGrid(grid);

        grid.reload(gridSudokuBase);
        
        ControlButtons controlsButtons = new ControlButtons(grid, actualGame);

        VBox rightPanel = new VBox(20, numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
        HBox layout = new HBox(20, grid.getGridPane(), rightPanel);
        VBox mainLayout = new VBox(10, topBar, layout);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle(selectedSudoku.getName() + " - " + MainMenu.getProfileName());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private static void showExitDialog(Stage primaryStage) {

        if (actualGame != null) {
            actualGame.pauseGame();
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Retour au menu");
        alert.setHeaderText("Voulez-vous retourner au menu de sélection ou au menu principal ?");
    
        Button menuSelectButton = new ProfileButton("Menu Sélection");
        Button menuButton = new ProfileButton("Menu Principal");
        Button cancelButton = new ProfileButton("Annuler");
    
        menuSelectButton.setOnAction(e -> {
            if (actualGame != null) {
                try {
                    actualGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualGame = null;
            }
            alert.setResult(ButtonType.OK);
            alert.close();
            SudokuMenu.showSudokuLibrary(primaryStage);
        });
    
        menuButton.setOnAction(e -> {
            if (actualGame != null) {
                try {
                    actualGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualGame = null;
            }
            alert.setResult(ButtonType.OK);
            alert.close();
            MainMenu.showMainMenu(primaryStage, MainMenu.getProfile());
        });
    
        cancelButton.setOnAction(e -> {
            if (actualGame != null) {
                actualGame.resumeGame();
            }
            alert.setResult(ButtonType.CANCEL);
            alert.close();
        });
    
        HBox buttons = new HBox(10, menuSelectButton, menuButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
    
        alert.getDialogPane().setContent(buttons);
        alert.showAndWait();
    }
}