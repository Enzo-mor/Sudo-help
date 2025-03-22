package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.GameState;

import java.sql.SQLException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.RotateTransition;

/**
 * Classe SudokuGame
 * Cette classe gere l'affichage de la fenetre de jeu Sudoku et le processus du jeu en lui-meme.
 * Elle permet a l'utilisateur de jouer au Sudoku, de faire des choix, et de suivre l'etat du jeu en temps reel.
 * 
 * @author Perron Nathan
 * @author Rasson Emma
 * @see Game
 * @see Sudoku
 * @see DBManager
 * @see Settings
 * @see MainMenu
 * @see SudokuGame
 * @see SudokuGrid
 */
public class SudokuGame {

    /** 
     * Largeur minimale de la fenetre du jeu 
     */
    private static final double MIN_WIDTH = 600;

    /** 
     * Hauteur minimale de la fenetre du jeu 
     */
    private static final double MIN_HEIGHT = 500;

    /** 
     * Instance du jeu actuel 
     */
    private static Game actualGame;

    /** 
     * Timeline pour verifier l'etat du jeu a chaque seconde 
     */
    private static final Timeline gameStateChecker = new Timeline();

    /** 
     * Fenetre de parametres de jeu 
     */
    private static Settings settingsWindow = null;

    /** 
     * Animation de rotation pour le bouton des parametres 
     */
    private static RotateTransition rotateAnimation;


    /**
     * Affiche la fenetre du jeu Sudoku.
     * Cette methode initialise le jeu avec une grille selectionnee et cree l'interface utilisateur.
     * 
     * @param primaryStage La fenetre principale de l'application [Stage]
     * @param selectedSudoku Le Sudoku selectionne pour le jeu [Sudoku]
     */
    public static void showSudokuGame(Stage primaryStage, Sudoku selectedSudoku) {
        Grid gridSudokuBase = DBManager.getGrid(selectedSudoku.getId());

        // Initialisation du jeu en fonction de son etat
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
            // Initialisation de la partie et du chronometre 
            actualGame.startGame();
        }
        
        // Creation du bouton "retour a la maison"
        Button homeButton = new Button();
        ImageView homeIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeButton.setGraphic(homeIcon);
        homeButton.setStyle("-fx-background-color: transparent;");
        homeButton.setOnAction(e -> showExitDialog(primaryStage));

        // Creation du timer
        Label timerLabel =  new Label("00:00:00");
        actualGame.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText(timer)));

        // Creation des icônes Livre et Info
        ImageView bookIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/book.png")));
        bookIcon.setFitWidth(30);
        bookIcon.setFitHeight(30);
        Button bookButton = new Button();
        bookButton.setGraphic(bookIcon);
        bookButton.setStyle("-fx-background-color: transparent;");
        
        ImageView infoIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/info.png")));
        infoIcon.setFitWidth(30);
        infoIcon.setFitHeight(30);
        Button infoButton = new Button();
        infoButton.setGraphic(infoIcon);
        infoButton.setStyle("-fx-background-color: transparent;");

        // --- Barre de menu en haut ---
        HBox leftBox = new HBox(homeButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox centerBox = new HBox(timerLabel);
        centerBox.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(10, bookButton, infoButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(leftBox);
        topBar.setCenter(centerBox);
        topBar.setRight(rightBox);
        topBar.setPadding(new Insets(10));

        // --- Bouton "gear" en bas a droite ---
        Button settingsButton = new Button();
        ImageView gearIcon = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/gear.png")));
        gearIcon.setFitWidth(30);
        gearIcon.setFitHeight(30);
        settingsButton.setGraphic(gearIcon);
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // --- Animation de rotation pour le bouton des parametres ---
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setByAngle(180);
        rotateAnimation.setCycleCount(1);

        settingsWindow = Settings.getInstance(primaryStage, gearIcon);
        settingsButton.setOnAction(e -> settingsWindow.toggleSettingsWindow());

        // --- Initialisation des composants ---
        ToolsPanel toolsPanel = new ToolsPanel();                                   // 1. Creer le panneau des outils
        NumberSelection numberSelection = new NumberSelection(toolsPanel);          // 2. Creer la selection des nombres
        SudokuGrid grid = new SudokuGrid(toolsPanel, actualGame);                   // 3. Creer la grille de Sudoku
        numberSelection.setSudokuGrid(grid);                                        // 4. Associer la grille a NumberSelection maintenant qu'elle existe
        grid.reload(gridSudokuBase);
        ControlButtons controlsButtons = new ControlButtons(grid, actualGame);

        // --- Conteneur du panneau droit sans le bouton settings ---
        VBox rightPanelContent = new VBox(20, numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
        rightPanelContent.setAlignment(Pos.CENTER);

        // --- Conteneur principal du panneau droit ---
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.getChildren().add(rightPanelContent);
        VBox.setVgrow(rightPanelContent, Priority.ALWAYS); // Permet d'occuper tout l'espace disponible

        // --- Conteneur pour le bouton settings, aligne en bas a droite ---
        HBox settingsContainer = new HBox(settingsButton);
        settingsContainer.setAlignment(Pos.BOTTOM_RIGHT);
        settingsContainer.setPadding(new Insets(10, 10, 10, 10)); // Ajoute un peu de marge

        // --- Conteneur principal pour inclure tout ---
        BorderPane rightPanelWrapper = new BorderPane();
        rightPanelWrapper.setCenter(rightPanel);  // Centre le contenu principal verticalement
        rightPanelWrapper.setBottom(settingsContainer); // Met le bouton settings en bas

        // --- Layout global avec la grille a gauche et le panneau droit a droite ---
        HBox layout = new HBox(20, SudokuGrid.getGridPane(), rightPanelWrapper);
        layout.setAlignment(Pos.CENTER_LEFT); // Garde la grille bien alignee a gauche        

        VBox mainLayout = new VBox(10, topBar, layout);

        // --- BorderPane pour organiser la mise en page ---
        BorderPane root = new BorderPane();
        root.setCenter(mainLayout); // Place le menu principal au centre

        Scene scene = new Scene(root, 935, 570);
        primaryStage.setTitle(selectedSudoku.getName() + " - " + MainMenu.getProfileName());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();

        // Verification automatique de la fin du jeu
        gameStateChecker.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            if (actualGame.getGameState() == GameState.FINISHED) {
                
                gameStateChecker.stop();
                if (actualGame != null) {
                    actualGame.pauseGame();
                }
                
                /* Methode d'effet */
                SudokuDisplay.showEndGameEffect(SudokuGrid.getGridPane(), primaryStage);

                if (actualGame != null) {
                    try {
                        actualGame.stopGame();
                    } catch (SQLException | InterruptedException e1) {
                        System.err.println("Error stopping the game: " + e1.getMessage());
                    }
                    actualGame = null;
                }
            }
        }));
        gameStateChecker.setCycleCount(Animation.INDEFINITE);
        gameStateChecker.play();
    }

    /**
     * Affiche une boîte de dialogue de confirmation pour quitter le jeu.
     * Cette methode permet a l'utilisateur de retourner au menu de selection ou au menu principal.
     * 
     * @param primaryStage La fenetre principale de l'application [Stage]
     */
    private static void showExitDialog(Stage primaryStage) {
        
        if (actualGame != null) {
            actualGame.pauseGame();
            gameStateChecker.stop();
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Retour au menu");
        alert.setHeaderText("Voulez-vous retourner au menu de sélection ou au menu principal ?");
        
        Button menuSelectButton = new ProfileButton("Menu Sélection");
        Button menuButton = new ProfileButton("Menu Principal");
        Button cancelButton = new ProfileButton("Annuler");
        
        // Traitement de quand on clique sur le bouton de retour au choix du sudoku
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
        
        // Traitement de quand on clique sur le bouton de retour au menu
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
        
        // Traitement de quand on clique sur le bouton d'annulation
        cancelButton.setOnAction(e -> {
            if (actualGame != null) {
                actualGame.resumeGame();
            }
            alert.setResult(ButtonType.CANCEL);
            alert.close();
            gameStateChecker.setCycleCount(Animation.INDEFINITE);
            gameStateChecker.play();
        });
        
        HBox buttons = new HBox(10, menuSelectButton, menuButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        
        alert.getDialogPane().setContent(buttons);
        alert.showAndWait();
    }
}
