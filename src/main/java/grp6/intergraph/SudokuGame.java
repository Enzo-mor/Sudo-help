package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.GameState;
import grp6.syshelp.*;

import java.sql.SQLException;
import java.util.List;

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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.RotateTransition;

/**
 * Classe SudokuGame
 * Cette classe gere l'affichage de la fenetre de jeu Sudoku et le processus du jeu en lui-meme.
 * Elle permet a l'utilisateur de jouer au Sudoku, de faire des choix, et de suivre l'etat du jeu en temps reel.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
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
     * Fenetre d'aide pour le jeu 
     */
    private static VBox helpOverlay;

    /** 
     * Texte d'aide pour la fenetre d'aide 
     */
    private static Label helpText;

    /**
     * Fenetre de confirmation pour quitter la partie
     */
    private static VBox quitConfirmation;

    /** 
     * Fenetre de technique pour le jeu 
     */
    private static VBox techniqueOverlay;


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

        bookButton.setOnAction(e -> showTechniquesOverlay());

        // --- Barre de menu en haut ---
        HBox leftBox = new HBox(homeButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox centerBox = new HBox(timerLabel);
        centerBox.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(10, bookButton);
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
        
        // --- Conteneur principal du panneau droit ---
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);

        // --- Conteneur du panneau droit sans le bouton settings ---
        VBox rightPanelContent = new VBox(20, numberSelection.getNumberSelection(), toolsPanel.getTools());
        rightPanelContent.setAlignment(Pos.CENTER);

        // --- Panneau d'aide ---
        helpOverlay = new VBox(20);
        helpText = new Label();
        
        StyledContent.setupHelpOverlay(helpOverlay, helpText);

        // --- StackPane pour superposer helpOverlay sur rightPanelContent ---
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rightPanelContent, helpOverlay);

        // --- Conteneur principal du panneau droit avec StackPane en haut et les boutons en bas ---
        VBox rightPanelWrapper = new VBox(20, stackPane, controlsButtons.getControlButtons());
        rightPanelWrapper.setAlignment(Pos.CENTER);

        // --- Conteneur pour le bouton settings, aligné en bas à droite ---
        HBox settingsContainer = new HBox(settingsButton);
        settingsContainer.setAlignment(Pos.BOTTOM_RIGHT);
        settingsContainer.setPadding(new Insets(10, 10, 10, 10)); // Ajoute un peu de marge

        // --- BorderPane pour organiser la mise en page ---
        BorderPane rightPanelBorder = new BorderPane();
        rightPanelBorder.setCenter(rightPanelWrapper);
        rightPanelBorder.setBottom(settingsContainer);

        // --- Layout global avec la grille a gauche et le panneau droit a droite ---
        HBox layout = new HBox(20, SudokuGrid.getGridPane(), rightPanelBorder);
        layout.setAlignment(Pos.CENTER_LEFT); // Garde la grille bien alignee a gauche        

        VBox mainLayout = new VBox(10, topBar, layout);

        // Création du panneau de confirmation pour cacher la partie (au départ caché)
        quitConfirmation = new VBox();
        quitConfirmation.setAlignment(Pos.CENTER);
        quitConfirmation.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        quitConfirmation.setVisible(false);

        // Création du panneau de techniques (au départ caché)
        techniqueOverlay = new VBox();
        techniqueOverlay.setAlignment(Pos.CENTER);
        techniqueOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        techniqueOverlay.setVisible(false);

        // Empiler le panneau de techniques par-dessus le reste
        StackPane rootStack = new StackPane(mainLayout, quitConfirmation, techniqueOverlay);
        
        // --- BorderPane pour organiser la mise en page ---
        BorderPane root = new BorderPane();
        root.setCenter(rootStack);

        Scene scene = new Scene(root, 935, 570);
        primaryStage.setTitle(selectedSudoku.getName() + " - " + MainMenu.getProfileName());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();

        // --- Verification automatique de la fin du jeu
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
     * Affiche un panneau de confirmation pour quitter le jeu.
     * Cette méthode permet à l'utilisateur de retourner au menu de sélection ou au menu principal.
     * 
     * @param parentStage La fenêtre principale de l'application [Stage].
     */
    private static void showExitDialog(Stage parentStage) {

        // Mise en pause du jeu si une partie est en cours
        if (actualGame != null) {
            actualGame.pauseGame();
            gameStateChecker.stop();
        }

        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(420);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        // Création du message de confirmation
        Label messageLabel = new Label("Voulez-vous retourner au menu de sélection ou au menu principal ?");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #343a40;");

        // Boutons
        Button menuSelectButton = new Button("Menu Sélection");
        Button menuButton = new Button("Menu Principal");
        Button cancelButton = new Button("Annuler");

        StyledContent.applyButtonStyle(menuSelectButton);
        StyledContent.applyButtonStyle(menuButton);
        StyledContent.applyButtonStyle(cancelButton);

        // Traitement pour le retour au menu de sélection
        menuSelectButton.setOnAction(e -> {
            if (actualGame != null) {
                try {
                    actualGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualGame = null;
            }
            quitConfirmation.setVisible(false);
            SudokuMenu.showSudokuLibrary(parentStage);
        });

        // Traitement pour le retour au menu principal
        menuButton.setOnAction(e -> {
            if (actualGame != null) {
                try {
                    actualGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualGame = null;
            }
            quitConfirmation.setVisible(false);
            MainMenu.showMainMenu(parentStage, MainMenu.getProfile());
        });

        // Traitement pour annuler la fermeture
        cancelButton.setOnAction(e -> {
            if (actualGame != null) {
                actualGame.resumeGame();
            }
            quitConfirmation.setVisible(false);
            gameStateChecker.setCycleCount(Animation.INDEFINITE);
            gameStateChecker.play();
        });

        // Conteneur pour les boutons
        HBox buttonBox = new HBox(10, menuSelectButton, menuButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        contentBox.getChildren().addAll(messageLabel, buttonBox);

        quitConfirmation.getChildren().addAll(contentBox);
        quitConfirmation.setVisible(true);
    }

    /**
     * Affiche le panneau d'aide.
     */
    public static void setHelpOverlayTrue() {
        helpOverlay.setVisible(true);
    }

    /**
     * Modifie le texte de l'aide
     * @param text
     */
    public static void setHelpText(String text) {
        helpText.setText(text);
    }

    /**
     * Methode pour afficher le panneau de technique
     */
    private static void showTechniquesOverlay() {
        if (actualGame != null) {
            actualGame.pauseGame();
            gameStateChecker.stop();
        }
    
        List<Technique> techniques = MainMenu.getProfile().getUnlockedTechniques();
    
        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));
    
        Label title = new Label("Mes techniques");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        VBox techniquesBox = new VBox(10);
        techniquesBox.setPadding(new Insets(10));
        techniquesBox.setAlignment(Pos.TOP_CENTER);
    
        ScrollPane scrollPane = new ScrollPane(techniquesBox);
        StyledContent.applyScrollPaneStyle(scrollPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    
        Label descriptionLabel = new Label();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal; -fx-text-fill: black; -fx-line-spacing: 5px;");
    
        ScrollPane descriptionScrollPane = new ScrollPane(descriptionLabel);
        StyledContent.applyScrollPaneStyle(descriptionScrollPane);
        descriptionScrollPane.setFitToWidth(true);
        descriptionScrollPane.setPrefHeight(250);
        descriptionScrollPane.setPadding(new Insets(10));
        descriptionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        descriptionScrollPane.setVisible(false);
    
        // Bouton unique pour gérer "Fermer" et "Retour"
        Button actionButton = new Button("Fermer");
        StyledContent.applyButtonStyle(actionButton);
    
        actionButton.setOnAction(e -> {
            if (descriptionScrollPane.isVisible()) {
                descriptionScrollPane.setVisible(false);
                scrollPane.setVisible(true);
                scrollPane.toFront();
                actionButton.setText("Fermer");
            } else {
                techniqueOverlay.setVisible(false);
                if (actualGame != null) {
                    actualGame.resumeGame();
                }
                gameStateChecker.setCycleCount(Animation.INDEFINITE);
                gameStateChecker.play();
            }
        });
    
        // Ajout des boutons de techniques
        for (Technique technique : techniques) {
            Button techButton = new Button(technique.getName());
            StyledContent.applyButtonBoxStyle(techButton);
            techButton.setOnAction(e -> {
                descriptionLabel.setText(technique.getLongDesc());
                scrollPane.setVisible(false);
                descriptionScrollPane.setVisible(true);
                descriptionScrollPane.toFront();
                actionButton.setText("Retour");
            });
            techniquesBox.getChildren().add(techButton);
        }
    
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(scrollPane, descriptionScrollPane);
    
        contentBox.getChildren().addAll(title, stackPane, actionButton);
        techniqueOverlay.getChildren().setAll(contentBox);
        techniqueOverlay.setVisible(true);
    }
}