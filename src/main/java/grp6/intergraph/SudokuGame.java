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

        Grid gridSudokuBase = null;

        if (selectedSudoku.getStatus() == GameState.IS_STARTED) {
            actualGame = selectedSudoku.getGame();
            actualGame.resumeGame();
        }
        
        else {
            try {
                gridSudokuBase = DBManager.getGrid(selectedSudoku.getId());
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
        Label timerLabel =  new Label("00:00:00");
        actualGame.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText(timer)));

        topBar.getChildren().add(timerLabel);

        // Créer le panneau de sélection des chiffres
        
        // 1. Créer le panneau des outils
        ToolsPanel toolsPanel = new ToolsPanel();

        // 2. Créer la sélection des nombres SANS SudokuGrid
        NumberSelection numberSelection = new NumberSelection(toolsPanel);

        // 3. Créer la grille de Sudoku en lui passant NumberSelection
        SudokuGrid grid = new SudokuGrid(numberSelection, toolsPanel, actualGame);

        // 4. Associer la grille à NumberSelection maintenant qu'elle existe
        numberSelection.setSudokuGrid(grid);

        grid.loadGrid(gridSudokuBase);
        ControlButtons controlsButtons = new ControlButtons(grid, actualGame);

        VBox rightPanel = new VBox(20);
        rightPanel.getChildren().addAll(numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
        HBox layout = new HBox(20);
        layout.getChildren().addAll(grid.getGridPane(), rightPanel);

        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, layout);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle(selectedSudoku.getName() + " - " + MainMenu.getProfileName());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private static void showExitDialog(Stage primaryStage) {

        actualGame.pauseGame();

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Retour au menu");
        alert.setHeaderText("Voulez-vous retourner au menu de sélection ou au menu principal ?");
    
        Button menuSelectButton = new ProfileButton("Menu Sélection");
        Button menuButton = new ProfileButton("Menu Principal");
        Button cancelButton = new ProfileButton("Annuler");
    
        menuSelectButton.setOnAction(e -> {
            try {
                actualGame.stopGame();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println(actualGame.getGameState());
            alert.setResult(ButtonType.OK);
            alert.close();
            SudokuMenu.showSudokuLibrary(primaryStage);
        });
    
        menuButton.setOnAction(e -> {
            try {
                actualGame.stopGame();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println(actualGame.getGameState());
            alert.setResult(ButtonType.OK);
            alert.close();
            MainMenu.showMainMenu(primaryStage, MainMenu.getProfile());
        });
    
        cancelButton.setOnAction(e -> {
            actualGame.resumeGame(); // Reprendre le chrono
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