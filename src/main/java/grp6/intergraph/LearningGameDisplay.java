package grp6.intergraph;
import java.sql.SQLException;

import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.GameState;
import grp6.syshelp.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Classe LearningGameDisplay
 * Cette classe represente la fenetre de jeu pour le mode apprentissage.
 * Elle affiche la grille du jeu, les controles, et les informations de progression.
 * 
 * @author PERRON Nathan
 * @see DBManager
 * @see ControlButtons
 * @see Help
 * @see LearningGame
 * @see LearningMenu
 * @see MainMenu
 * @see NumberSelection
 * @see SudokuDisplay
 * @see StyledContent
 * @see SudokuGame
 * @see SudokuGrid
 * @see Settings
 * @see Technique
 * @see ToolsPanel
 */
public class LearningGameDisplay {

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
    private static LearningGame actualLearningGame;

    /**
     * Technique actuel a apprendre
     */
    private static Technique actualTechnique;

    /** 
     * Fenetre de parametres de jeu 
     */
    private static Settings settingsWindow = null;

    /** 
     * Animation de rotation pour le bouton des parametres 
     */
    private static RotateTransition rotateAnimation;

    /*
     * Bouton des controles (refaire, aide, ...)
     */
    private static ControlButtons controlsButtons = null;

    /**
     * Grille du jeu
     */
    private static SudokuGrid grid;

    /**
     * Message d'introduction de la technique
     */
    private static VBox techniqueIntro;

    /**
     * Bouton pour voir plus quand on veut de l'aide
     */
    private static Button seeMoreButton;

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
    private static VBox infoOverlay;

    /**
     * Fenetre de fin
     */
    private static VBox endOverlay;

    /** 
     * Timeline pour verifier l'etat du jeu a chaque seconde 
     */
    private static final Timeline gameStateChecker = new Timeline();

    /**
     * Constructeur de la classe LearningGameDisplay.
     */
    public LearningGameDisplay() {}
    
    /**
     * Affiche l'interface du mode d'apprentissage du jeu.
     * 
     * @param primaryStage La fenetre principale de l'application.
     * @param selectedTechnique La technique de Sudoku selectionnee pour l'apprentissage.
     */
    public static void showLearningGame(Stage primaryStage, Technique selectedTechnique) {

        actualTechnique = DBManager.getTech(selectedTechnique.getId());

        try {
            actualLearningGame = new LearningGame(MainMenu.getProfile(), actualTechnique);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        actualLearningGame.startGame();

        // Creation du bouton "retour a la maison"
        Button homeButton = new Button();
        ImageView homeIcon = new ImageView(new Image(LearningGameDisplay.class.getResourceAsStream("/home.png")));
        homeIcon.setFitWidth(30);
        homeIcon.setFitHeight(30);
        homeButton.setGraphic(homeIcon);
        homeButton.setStyle("-fx-background-color: transparent;");
        homeButton.setOnAction(e -> showExitDialog(primaryStage));

        // Creation du timer
        Label timerLabel =  new Label("00:00:00");
        actualLearningGame.setGameTimeListener(timer -> Platform.runLater(() -> timerLabel.setText(timer)));

        // Creation des icônes Livre et Info
        ImageView infoIcon = new ImageView(new Image(LearningGameDisplay.class.getResourceAsStream("/info.png")));
        infoIcon.setFitWidth(30);
        infoIcon.setFitHeight(30);
        Button infoButton = new Button();
        infoButton.setGraphic(infoIcon);
        infoButton.setStyle("-fx-background-color: transparent;");

        infoButton.setOnAction(e -> showInfoOverlay());

        // --- Barre de menu en haut ---
        HBox leftBox = new HBox(homeButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        HBox centerBox = new HBox(timerLabel);
        centerBox.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(10, infoButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(leftBox);
        topBar.setCenter(centerBox);
        topBar.setRight(rightBox);
        topBar.setPadding(new Insets(10));

        // --- Bouton "gear" en bas a droite ---
        Button settingsButton = new Button();
        ImageView gearIcon = new ImageView(new Image(LearningGameDisplay.class.getResourceAsStream("/gear.png")));
        gearIcon.setFitWidth(30);
        gearIcon.setFitHeight(30);
        settingsButton.setGraphic(gearIcon);
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // --- Animation de rotation pour le bouton des parametres ---
        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setByAngle(180);
        rotateAnimation.setCycleCount(1);

        settingsWindow = Settings.getInstance(primaryStage, gearIcon);
        settingsButton.setOnAction(e -> {
            SudokuGame.resetTimer();
            settingsWindow.toggleSettingsWindow();
        });

        // --- Initialisation des composants ---
        ToolsPanel toolsPanel = new ToolsPanel();
        NumberSelection numberSelection = new NumberSelection(toolsPanel);
        grid = new SudokuGrid(toolsPanel, actualLearningGame.getGame());
        numberSelection.setSudokuGrid(grid);
        grid.setLearningMode(true);
        grid.setTechnique(actualTechnique);
        grid.reload(LearningGame.getGrid());
        controlsButtons = new ControlButtons(grid, actualLearningGame.getGame());
        controlsButtons.disableHelpButton(); // Desactive le bouton d'aide
        
        // --- Conteneur principal du panneau droit ---
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);

        // --- Conteneur du panneau droit sans le bouton settings ---
        VBox rightPanelContent = new VBox(20, numberSelection.getNumberSelection(), toolsPanel.getTools());
        rightPanelContent.setAlignment(Pos.CENTER);

        // --- Panneau d'aide ---
        helpOverlay = new VBox(20);
        helpText = new Label();
        
        setupHelpOverlay(helpOverlay, helpText);

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

        // Création du panneau d'introduction à la technique
        techniqueIntro = new VBox();
        techniqueIntro.setAlignment(Pos.CENTER);
        techniqueIntro.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        showIntroLearning();

        // Création du panneau de confirmation pour cacher la partie (au départ caché)
        quitConfirmation = new VBox();
        quitConfirmation.setAlignment(Pos.CENTER);
        quitConfirmation.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        quitConfirmation.setVisible(false);

        // Création du panneau d'information (au départ caché)
        infoOverlay = new VBox();
        infoOverlay.setAlignment(Pos.CENTER);
        infoOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        infoOverlay.setVisible(false);

        endOverlay = new VBox();
        endOverlay.setAlignment(Pos.CENTER);
        endOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        endOverlay.setVisible(false);

        // Empiler le panneau de techniques par-dessus le reste
        StackPane rootStack = new StackPane(mainLayout, quitConfirmation, infoOverlay, endOverlay, techniqueIntro);
        
        // --- BorderPane pour organiser la mise en page ---
        BorderPane root = new BorderPane();
        root.setCenter(rootStack);

        Scene scene = new Scene(root, 935, 570);
        
        primaryStage.setTitle(selectedTechnique.getName() + " - " + MainMenu.getProfileName());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // --- Verification automatique de la fin du jeu
        gameStateChecker.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            if (actualLearningGame.getGameState() == GameState.FINISHED) {
                
                gameStateChecker.stop();
                if (actualLearningGame != null) {
                    actualLearningGame.pauseGame();
                }
                
                /* Methode d'effet */
                SudokuDisplay.showEndGameEffect(SudokuGrid.getGridPane(), primaryStage);

                Platform.runLater(() -> {        
                    if (actualLearningGame != null) {
                        try {
                            actualLearningGame.stopGame();
                            MainMenu.getProfile().addTech(actualTechnique.getName());
        
                            // Déplacement ici pour éviter le problème
                            actualLearningGame = null;
        
                        } catch (SQLException | InterruptedException e1) {
                            System.err.println("Error stopping the game: " + e1.getMessage());
                        }
                    }
                });
            }
        }));
        gameStateChecker.setCycleCount(Animation.INDEFINITE);
        gameStateChecker.play();
    }

    /**
     * Affiche un panneau de confirmation pour quitter le jeu.
     * Cette methode permet a l'utilisateur de retourner au menu de technique ou au menu principal.
     * 
     * @param parentStage La fenetre principale de l'application [Stage].
     */
    private static void showExitDialog(Stage parentStage) {

        quitConfirmation.getChildren().clear();

        // Mise en pause du jeu si une partie est en cours
        if (actualLearningGame != null) {
            actualLearningGame.pauseGame();
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
        Button menuLearningButton = new Button("Menu Technique");
        Button menuButton = new Button("Menu Principal");
        Button cancelButton = new Button("Annuler");

        StyledContent.applyButtonStyle(menuLearningButton);
        StyledContent.applyButtonStyle(menuButton);
        StyledContent.applyButtonStyle(cancelButton);

        // Traitement pour le retour au menu de sélection
        menuLearningButton.setOnAction(e -> {
            if (actualLearningGame != null) {
                try {
                    actualLearningGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualLearningGame = null;
            }
            quitConfirmation.setVisible(false);
            grid.setLearningMode(false);
            LearningMenu.showLearningLibrary(parentStage);
        });

        // Traitement pour le retour au menu principal
        menuButton.setOnAction(e -> {
            if (actualLearningGame != null) {
                try {
                    actualLearningGame.stopGame();
                } catch (SQLException | InterruptedException e1) {
                    System.err.println("Error stopping the game: " + e1.getMessage());
                }
                actualLearningGame = null;
            }
            quitConfirmation.setVisible(false);
            grid.setLearningMode(false);
            MainMenu.showMainMenu(parentStage, MainMenu.getProfile());
        });

        // Traitement pour annuler la fermeture
        cancelButton.setOnAction(e -> {
            if (actualLearningGame != null) {
                actualLearningGame.resumeGame();
            }
            quitConfirmation.setVisible(false);
            gameStateChecker.setCycleCount(Animation.INDEFINITE);
            gameStateChecker.play();
        });

        // Conteneur pour les boutons
        HBox buttonBox = new HBox(10, menuLearningButton, menuButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        contentBox.getChildren().addAll(messageLabel, buttonBox);

        quitConfirmation.getChildren().addAll(contentBox);
        quitConfirmation.setVisible(true);
    }

    /**
     * Methode pour afficher le panneau de technique
     */
    private static void showInfoOverlay() {
        if (actualLearningGame != null) {
            actualLearningGame.pauseGame();
            gameStateChecker.stop();
            SudokuGame.stopTimer();
        }
    
        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));
    
        Label title = new Label(actualTechnique.getName());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
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
            actualLearningGame.resumeGame();
            infoOverlay.setVisible(false);
            gameStateChecker.setCycleCount(Animation.INDEFINITE);
            gameStateChecker.play();
        });
    
        descriptionLabel.setText(actualTechnique.getLongDesc());
        descriptionScrollPane.setVisible(true);
        descriptionScrollPane.toFront();
    
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(descriptionScrollPane);
    
        contentBox.getChildren().addAll(title, stackPane, actionButton);
        infoOverlay.getChildren().setAll(contentBox);
        infoOverlay.setVisible(true);
    }

    /**
     * Methode pour afficher le panneau d'introduction a une technique
     */
    private static void showIntroLearning() {

        if (actualLearningGame != null) {
            actualLearningGame.pauseGame();
            gameStateChecker.stop();
            SudokuGame.stopTimer();
        }
    
        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));
    
        Label title = new Label(actualTechnique.getName());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        Label introLabel = new Label();
        introLabel.setWrapText(true);
        introLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal; -fx-text-fill: black; -fx-line-spacing: 5px;");
    
        ScrollPane introScrollPane = new ScrollPane(introLabel);
        StyledContent.applyScrollPaneStyle(introScrollPane);
        introScrollPane.setFitToWidth(true);
        introScrollPane.setPrefHeight(250);
        introScrollPane.setPadding(new Insets(10));
        introScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        introScrollPane.setVisible(false);
    
        Button learnButton = new Button("Apprendre");
        StyledContent.applyButtonStyle(learnButton);
        learnButton.setOnAction(e -> {
            actualLearningGame.resumeGame();
            techniqueIntro.setVisible(false);
            gameStateChecker.setCycleCount(Animation.INDEFINITE);
            gameStateChecker.play();
        });
    
        introLabel.setText(actualTechnique.getLongDesc());
        introScrollPane.setVisible(true);
        introScrollPane.toFront();
    
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(introScrollPane);
    
        contentBox.getChildren().addAll(title, stackPane, learnButton);
        techniqueIntro.getChildren().setAll(contentBox);
        techniqueIntro.setVisible(true);
    }

    /**
     * Applique un style au panneau d'aide
     * @param helpOverlay boite vbox pour l'affichage
     * @param helpText label pour l'affichage
     */
    public static void setupHelpOverlay(VBox helpOverlay, Label helpText) {

        SudokuGame.stopTimer();

        // Style du cadre extérieur
        helpOverlay.setStyle("-fx-background-color: #4A90E2; -fx-background-radius: 15; -fx-padding: 15px;");
        helpOverlay.setAlignment(Pos.CENTER);
        helpOverlay.setVisible(false);

        // Cadre blanc intérieur
        StackPane innerPane = new StackPane();
        innerPane.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        innerPane.setPrefSize(300, 150);

        // Style du texte
        helpText.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
        innerPane.getChildren().add(helpText);
        
        // Bouton "Voir plus"
        seeMoreButton = new Button("Voir plus");
        StyledContent.applyButtonStyle(seeMoreButton);
        seeMoreButton.setOnAction(e -> {
            int indexHelp = ControlButtons.getCurrentHelp() + 1;
            Help actualHelp = ControlButtons.getHelp();
            
            // Vérification avant d'afficher le prochain message
            if (indexHelp <= 3) {
                SudokuGame.setHelpText(actualHelp.getMessage(indexHelp));
                ControlButtons.setCurrentHelp(indexHelp);
                if (indexHelp == 2) {
                    MainMenu.getProfile().addTech(actualHelp.getName());
                }
                if (indexHelp == 3) {
                    SudokuDisplay.highlightCells(SudokuGrid.getGridPane(), actualHelp.getDisplay());
                }
            }
            
            // Désactiver le bouton si l'index dépasse 3
            seeMoreButton.setDisable(indexHelp >= 3);
        });

        // Bouton de fermeture (croix)
        Button closeHelpButton = new Button("✖");
        closeHelpButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        closeHelpButton.setOnAction(e -> {
            ControlButtons.setCurrentHelp(0);
            SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            seeMoreButton.setDisable(false);
            helpOverlay.setVisible(false);
            SudokuGame.resetTimer();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Ajouter la croix en haut à droite
        HBox topBar = new HBox(spacer, closeHelpButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPrefWidth(400);

        // Icône ampoule
        ImageView lightBulb = new ImageView(new Image(SudokuGame.class.getResourceAsStream("/lightBulb.png"))); // Ajoute cette icône dans tes ressources
        lightBulb.setFitWidth(20);
        lightBulb.setFitHeight(20);

        HBox topContent = new HBox(10, lightBulb, topBar);
        topContent.setAlignment(Pos.TOP_LEFT);
        topContent.setPrefWidth(300);

        // Organiser les éléments dans la VBox
        VBox content = new VBox(10, topContent, innerPane, seeMoreButton);
        content.setAlignment(Pos.CENTER);

        helpOverlay.getChildren().add(content);
    }

    /**
     * Affiche une superposition avec un titre et un message.
     * 
     * @param primaryStage La scene principale a changer apres la fin du jeu [ Stage ]
     */
    public static void showEndOverlay(Stage primaryStage) {

        // Conteneur principal
        VBox contentBox = new VBox(20);
        StyledContent.applyContentBoxStyle(contentBox);
        contentBox.setMaxWidth(400);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));

        Label title = new Label("Apprentissage terminé");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label messageLabel = new Label("Félicitations ! Vous avez appris une nouvelle technique !");
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        Button closeButton = new Button("Quitter");
        StyledContent.applyButtonStyle(closeButton);
        closeButton.setOnAction(e -> {
            grid.setLearningMode(false);
            endOverlay.setVisible(false);
            LearningMenu.showLearningLibrary(primaryStage);
        });

        contentBox.getChildren().addAll(title, messageLabel, closeButton);
        endOverlay.getChildren().setAll(contentBox);
        endOverlay.setVisible(true);
    }

    /**
     * Renvoie la technique utilisee pour cette grille
     * 
     * @return La technique de la grille
     */
    public static Technique getActualTechnique() {
        return actualTechnique;
    }
}