package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.GameState;

import java.sql.SQLException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


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
        topBar.getChildren().add(homeButton); // Ajouter le bouton Home à la gauche de la barre


        // Creer un espace flexible qui va pousser le timer à droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // L'espace prend tout l'espace restant
        topBar.getChildren().add(spacer);

        // Ajouter le timer à droite
        Label timerLabel =  new Label("00:00:00");
        actualGame.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText(timer)));

        topBar.getChildren().add(timerLabel);

        // Création de tout les composants
        ToolsPanel toolsPanel = new ToolsPanel();                                   // 1. Creer le panneau des outils
        NumberSelection numberSelection = new NumberSelection(toolsPanel);          // 2. Creer la selection des nombres
        SudokuGrid grid = new SudokuGrid(toolsPanel, actualGame);                   // 3. Creer la grille de Sudoku
        numberSelection.setSudokuGrid(grid);                                        // 4. Associer la grille à NumberSelection maintenant qu'elle existe


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

        // Vérification automatique de la fin du jeu
        Timeline gameStateChecker = new Timeline();
        gameStateChecker.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            if (actualGame.getGameState() == GameState.FINISHED) {
                
                gameStateChecker.stop();
                if (actualGame != null) {
                    actualGame.pauseGame();
                }
                
                /* Méthode d'effet */
                showEndGameEffect(grid.getGridPane(), primaryStage);
            }
        }));
        gameStateChecker.setCycleCount(Animation.INDEFINITE);
        gameStateChecker.play();
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
    
    private static void showEndGameEffect(GridPane grid, Stage primaryStage) {
        // Calculer les dimensions du GridPane
        int rows = grid.getRowCount();
        int cols = grid.getColumnCount();
    
        // Définir les paramètres de l'animation
        double maxTranslateX = 5;  // Déplacement maximal sur l'axe X
        double maxTranslateY = 5;  // Déplacement maximal sur l'axe Y
        double maxScale = 1.05;      // Augmentation de la taille des éléments (effet de soulèvement)
        double maxDelay = 1.5;      // Délai maximal entre l'animation des boutons (plus un bouton est éloigné, plus le délai est long)
    
        // Calculer le centre de la grille
        double centerX = (cols - 1) / 2.0;
        double centerY = (rows - 1) / 2.0;
    
        // Variable pour savoir si l'animation est terminée
        final int totalCells = rows * cols;
        final int[] completedCells = {0};
    
        // Parcourir toutes les cellules de la grille
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Récupérer chaque cellule de la grille
                for (javafx.scene.Node node : grid.getChildren()) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                        if (node instanceof Button) {  // Si la cellule est un bouton
                            Button button = (Button) node;
    
                            // Calculer la distance du centre de la grille
                            double distanceX = Math.abs(col - centerX);
                            double distanceY = Math.abs(row - centerY);
                            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY); // distance euclidienne
    
                            // Calculer le délai basé sur la distance
                            double delay = (distance / Math.sqrt((cols - 1) * (cols - 1) + (rows - 1) * (rows - 1))) * maxDelay;
    
                            // Créer l'animation de secousse
                            Timeline timeline = new Timeline(
                                // Initialisation de l'animation
                                new KeyFrame(Duration.seconds(delay),
                                    new KeyValue(button.translateXProperty(), 0),
                                    new KeyValue(button.translateYProperty(), 0),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)
                                ),
                                // Animation de l'onde (déplacement vers le haut et bas)
                                new KeyFrame(Duration.seconds(delay + 0.2),
                                    new KeyValue(button.translateXProperty(), maxTranslateX),
                                    new KeyValue(button.translateYProperty(), -maxTranslateY),
                                    new KeyValue(button.scaleXProperty(), maxScale),
                                    new KeyValue(button.scaleYProperty(), maxScale)
                                ),
                                new KeyFrame(Duration.seconds(delay + 0.4),
                                    new KeyValue(button.translateXProperty(), -maxTranslateX),
                                    new KeyValue(button.translateYProperty(), maxTranslateY),
                                    new KeyValue(button.scaleXProperty(), maxScale),
                                    new KeyValue(button.scaleYProperty(), maxScale)
                                ),
                                new KeyFrame(Duration.seconds(delay + 0.6),
                                    new KeyValue(button.translateXProperty(), 0),
                                    new KeyValue(button.translateYProperty(), 0),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)
                                )
                            );
    
                            // À la fin de l'animation de chaque cellule
                            timeline.setOnFinished(event -> {
                                // Mise à jour du compteur de cellules animées
                                completedCells[0]++;
    
                                // Si toutes les cellules sont animées, on affiche un message de fin et on change de scène
                                if (completedCells[0] == totalCells) {
                                    // Pause de 1 seconde avant d'afficher le message de fin
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
    
                                    // Afficher un message de fin
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Jeu terminé");
                                        alert.setHeaderText("Félicitations !");
                                        alert.setContentText("Vous avez terminé le Sudoku !");
                                        alert.showAndWait();
    
                                        // Changer de scène après l'animation
                                        SudokuMenu.showSudokuLibrary(primaryStage); // Ou MainMenu.showMainMenu si nécessaire

                                        if (actualGame != null) {
                                            try {
                                                actualGame.stopGame();
                                            } catch (SQLException | InterruptedException e1) {
                                                System.err.println("Error stopping the game: " + e1.getMessage());
                                            }
                                            actualGame = null;
                                        }
                                    });
                                }
                            });
    
                            // Démarrer l'animation du bouton
                            timeline.play();
                        }
                    }
                }
            }
        }
    }
    
     
}
