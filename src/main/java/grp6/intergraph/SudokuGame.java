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
import javafx.scene.control.Alert.AlertType;
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

    /*
     * Affichage de la fenetre pop-up pour l'aide
     */
    private static boolean showPopup = true;

    /*
     * Affichage de la fenetre pop-up pour l'aide
     */
    private static boolean showHelpAnimation = true;

    /*
     * Timer d'inactivité
     */
    private static Timeline inactivityTimer = new Timeline();

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

    /*
     * Bouton des controles (refaire, aide, ...)
     */
    private static ControlButtons controlsButtons = null;

    /**
     * Bouton pour voir plus quand on veut de l'aide
     */
    private static Button seeMoreButton;

    /*
     * Timer pour l'animation du bouton d'aide
     */
    private static Timeline blinkAnimation;

    /*
     * Timer pour la fin de l'anumation du bouton
     */
    private static Timeline resetTimer; 


    /**
     * Affiche la fenetre du jeu Sudoku.
     * Cette methode initialise le jeu avec une grille selectionnee et cree l'interface utilisateur.
     * 
     * @param primaryStage La fenetre principale de l'application [Stage]
     * @param selectedSudoku Le Sudoku selectionne pour le jeu [Sudoku]
     */
    public static void showSudokuGame(Stage primaryStage, Sudoku selectedSudoku) {
        Grid gridSudokuBase = DBManager.getGrid(selectedSudoku.getId());
        SudokuGame.resetTimer();

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
        settingsButton.setOnAction(e -> {
            SudokuGame.resetTimer();
            settingsWindow.toggleSettingsWindow();
        });

        // --- Initialisation des composants ---
        ToolsPanel toolsPanel = new ToolsPanel();                                   // 1. Creer le panneau des outils
        NumberSelection numberSelection = new NumberSelection(toolsPanel);          // 2. Creer la selection des nombres
        SudokuGrid grid = new SudokuGrid(toolsPanel, actualGame);                   // 3. Creer la grille de Sudoku
        numberSelection.setSudokuGrid(grid);                                        // 4. Associer la grille a NumberSelection maintenant qu'elle existe
        grid.reload(gridSudokuBase);
        controlsButtons = new ControlButtons(grid, actualGame);
        
        // --- Conteneur principal du panneau droit ---
        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.CENTER);

        // --- Conteneur du panneau droit sans le bouton settings ---
        VBox rightPanelContent = new VBox(20, numberSelection.getNumberSelection(), toolsPanel.getTools());
        rightPanelContent.setAlignment(Pos.CENTER);

        // --- Panneau d'aide ---
        helpOverlay = new VBox(20);
        helpText = new Label();
        helpText.setAlignment(Pos.CENTER);
        helpText.setWrapText(true);
        
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
        setupInactivityTimer(scene);
        
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
     * Cette methode permet à l'utilisateur de retourner au menu de selection ou au menu principal.
     * 
     * @param parentStage La fenetre principale de l'application [Stage].
     */
    private static void showExitDialog(Stage parentStage) {

        quitConfirmation.getChildren().clear();

        // Mise en pause du jeu si une partie est en cours
        if (actualGame != null) {
            actualGame.pauseGame();
            gameStateChecker.stop();
            SudokuGame.stopTimer();
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
            SudokuGame.resetTimer();
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
            SudokuGame.stopTimer();
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
                    SudokuGame.resetTimer();
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

    /**
     * Applique un style au panneau d'aide
     */
    public static void setupHelpOverlay(VBox helpOverlay, Label helpText) {

        SudokuGame.stopTimer();

        // Style du cadre extérieur
        helpOverlay.setStyle("-fx-background-color: #4A90E2; -fx-background-radius: 15; -fx-padding: 15px;");
        helpOverlay.setAlignment(Pos.CENTER);
        helpOverlay.setVisible(false); // Masqué par défaut

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
     * Methode pour changer l'etat du bouton afin d'en voir plus lors de la demande de l'aide
     */
    public static void setDisableSeeMoreButton(boolean state) {
        seeMoreButton.setDisable(state);
    }
    
    /*
     * Gere le temps d'inactivite du joueur
     * @param scene Scene affichee 
     */
    private static void setupInactivityTimer(Scene scene) {
        inactivityTimer = new Timeline(new KeyFrame(Duration.seconds(20), event -> {
            if(showPopup){
                showPopup();
            }else{
                if(showHelpAnimation){
                    if (controlsButtons.getHelpButton() != null) {
                        toggleBlinkEffect(controlsButtons.getHelpButton(), true);
                        startResetTimer(scene);
                    }
                }
            }
        
        }));
        inactivityTimer.setCycleCount(1);
        if(showHelpAnimation){
            inactivityTimer.play(); 
        }

        // Remet le temps d'inactivite a zero apres une action du joueur
        scene.setOnKeyPressed(event -> resetTimer());
        scene.setOnMouseClicked(event -> resetTimer());

        // Remise a zero du timer
        resetTimer();
    }

    /*
     * Remise a zero du timer d'inactivite
     */
    public static void resetTimer() {
        inactivityTimer.stop(); // Mets en pause le timer actuel
        inactivityTimer.playFromStart(); // Remise a zero
    }

    /**
     * Arrete le timer d'inactivite
     */
    public static void stopTimer() {
        inactivityTimer.stop();
    }

    /*
     * Affiche la fenetre pour proposer de l'aide
     */
    private static void showPopup() {
        showPopup = false;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Avez vous besoin d'aide ?");
        alert.setHeaderText(null);
        alert.setContentText("Vous semblez être en difficulté, n'oubliez pas que l'aide existe !");
        alert.show();
        resetTimer();
    }

    /*
     * Permet de changer l'etat de l'affichage de la fenetre d'aide via les Settings
     */
    public static void switchShowPopUp(){
        if(showPopup!=false){
            showPopup = !showPopup;
        }
    }

    /*
     * Permet de changer l'etat de l'affichage de l'animation du bouton d'aide via les Settings
     */
    public static void switchShowHelpAnimation(){
        showHelpAnimation = !showHelpAnimation;
    }

    /**
     * Gere l'animation pour l'effet de clignotant du bouton d'aide
     * 
     * @param button Le bouton qui doit clignoter [Button]
     * @param start Savoir si on doit faire l'animation [boolean]
     */
    private static void toggleBlinkEffect(Button button, boolean start) {
        if (start) {
            blinkAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> StyledContent.applyBlinkingButtonStyle(button)),
                new KeyFrame(Duration.seconds(1), e -> StyledContent.applyButtonStyle(button))
            );
            blinkAnimation.setCycleCount(Animation.INDEFINITE);
            blinkAnimation.play();
        } else {
            if (blinkAnimation != null) {
                blinkAnimation.stop();
                StyledContent.applyButtonStyle(button);
            }
        }
    }
    
    /**
     * Remet le timer de l'animation et de l'inactivite a zero
     * 
     * @param scene La scene actuelle [Scene]
     */
    private static void startResetTimer(Scene scene) {
        if (resetTimer != null) {
            resetTimer.stop();
        }
    
        resetTimer = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (controlsButtons.getHelpButton() != null) {
                toggleBlinkEffect(controlsButtons.getHelpButton(), false);
            }
            setupInactivityTimer(scene); // Relancer la détection d'inactivité
        }));
        resetTimer.setCycleCount(1);
        resetTimer.play();
    }
}