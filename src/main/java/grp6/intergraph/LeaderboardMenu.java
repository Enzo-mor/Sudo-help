package grp6.intergraph;
import grp6.sudocore.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

/**
 * Classe LeaderboardMenu
 * Cette classe represente le menu du classement des joueurs en fonction des grilles de Sudoku.
 * Elle permet de consulter le classement des meilleurs joueurs par difficulte et par grille specifique.
 *
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see DBManager
 * @see MainMenu
 * @see Player
 * @see StyledContent
 */
public class LeaderboardMenu {

    /** 
     * Index de la difficulte selectionnee 
     */
    private static int currentDifficultyIndex = 0;
    
    /** 
     * Liste des niveaux de difficulte disponibles 
     */
    private static final String[] DIFFICULTIES = {"facile", "moyen", "difficile"};
    
    /** 
     * ID de la grille selectionnee 
     */
    private static int selectedGridId = -1;

    /**
     * Constructeur de la classe LeaderboardMenu.
     */
    public LeaderboardMenu() {}

    /**
     * Affiche le menu du classement des joueurs.
     *
     * @param stage Fenetre principale de l'application [Stage]
     */
    public static void showLeaderBoard(Stage stage) {
        currentDifficultyIndex = 0;

        // Label pour la difficulte
        Label difficultyLabel = new Label("Classement " + DIFFICULTIES[currentDifficultyIndex]);
        difficultyLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-padding: 20px;");

        // Boutons de navigation
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");
        StyledContent.applyArrowButtonStyle(leftArrow);
        StyledContent.applyArrowButtonStyle(rightArrow);

        // ComboBox pour choisir une grille specifique
        ComboBox<String> gridSelector = new ComboBox<>();
        gridSelector.setMinWidth(250);
        gridSelector.setMinHeight(45);
        gridSelector.setStyle(
            "-fx-background-color: #4A90E2; " +
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10px; " +
            "-fx-background-insets: 0; " +
            "-fx-border-color: #2C3E50; " +
            "-fx-border-radius: 10px; " +
            "-fx-border-width: 2px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        gridSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-background-color: #4A90E2;");
                }
            }
        });
        gridSelector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black;");
                }
            }
        });

        // Conteneur pour afficher le classement
        VBox leaderboardContainer = new VBox(20);
        leaderboardContainer.setAlignment(Pos.CENTER);
        leaderboardContainer.setMinHeight(150);

        // Recuperation des grilles disponibles
        Map<Integer, String> grids = DBManager.getGridsByDifficulty(DIFFICULTIES[currentDifficultyIndex]);
        gridSelector.getItems().addAll(grids.values());

        // Selection de la première grille
        if (!grids.isEmpty()) {
            selectedGridId = grids.keySet().iterator().next();
            gridSelector.getSelectionModel().selectFirst();
        }

        // Actions pour changer de difficulte
        leftArrow.setOnAction(e -> changeDifficulty(-1, difficultyLabel, gridSelector, leaderboardContainer));
        rightArrow.setOnAction(e -> changeDifficulty(1, difficultyLabel, gridSelector, leaderboardContainer));

        // Action sur le choix de grille
        gridSelector.setOnAction(e -> {
            selectedGridId = grids.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(gridSelector.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(-1);
            updateLeaderboard(leaderboardContainer);
        });

        // Initialisation du classement
        updateLeaderboard(leaderboardContainer);

        // Bouton retour
        Button backButton = new Button("Retour");
        StyledContent.applyButtonStyle(backButton);
        backButton.setOnAction(e -> MainMenu.showMainMenu(stage, MainMenu.getProfile()));

        // Mise en page
        HBox navigation = new HBox(20, leftArrow, difficultyLabel, rightArrow);
        navigation.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, navigation, gridSelector, leaderboardContainer, backButton);
        layout.setSpacing(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Mode Libre - " + MainMenu.getProfileName());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Change la difficulte affichee et met a jour la liste des grilles et le classement.
     *
     * @param delta Valeur de changement d'index de difficulte [-1 ou 1]
     * @param difficultyLabel Label affichant la difficulte actuelle [Label]
     * @param gridSelector ComboBox permettant la selection d'une grille [ComboBox]
     * @param leaderboardContainer Conteneur affichant le classement [VBox]
     */
    private static void changeDifficulty(int delta, Label difficultyLabel, ComboBox<String> gridSelector, VBox leaderboardContainer) {
        currentDifficultyIndex = (currentDifficultyIndex + delta + DIFFICULTIES.length) % DIFFICULTIES.length;
        difficultyLabel.setText("Classement " + DIFFICULTIES[currentDifficultyIndex]);

        // Mise a jour des grilles disponibles
        gridSelector.getItems().clear();
        Map<Integer, String> grids = DBManager.getGridsByDifficulty(DIFFICULTIES[currentDifficultyIndex]);
        gridSelector.getItems().addAll(grids.values());

        if (!grids.isEmpty()) {
            selectedGridId = grids.keySet().iterator().next();
            gridSelector.getSelectionModel().selectFirst();
        } else {
            selectedGridId = -1;
        }

        leaderboardContainer.getChildren().clear();
        leaderboardContainer.setPadding(Insets.EMPTY);
        leaderboardContainer.setSpacing(0);

        updateLeaderboard(leaderboardContainer);

        leaderboardContainer.applyCss();
        leaderboardContainer.layout();
    }

    /**
     * Met a jour l'affichage du classement des meilleurs joueurs pour la grille selectionnee.
     *
     * @param leaderboardContainer Conteneur affichant le classement [VBox]
     */
    private static void updateLeaderboard(VBox leaderboardContainer) {

        leaderboardContainer.getChildren().clear();

        if (selectedGridId == -1) {
            leaderboardContainer.getChildren().add(new Label("Aucun classement disponible."));
            return;
        }

        List<Player> topPlayers = DBManager.getTop5PlayersForGrid(selectedGridId);

        GridPane rankingGrid = new GridPane();
        rankingGrid.setAlignment(Pos.CENTER);
        rankingGrid.setHgap(20); // Espacement horizontal entre les colonnes
        rankingGrid.setVgap(5);  // Espacement vertical entre les lignes

        for (int i = 0; i < topPlayers.size(); i++) {
            Player player = topPlayers.get(i);

            Label rankLabel = new Label((i + 1) + ".");
            Label nameLabel = new Label(player.getName());
            Label scoreLabel = new Label(String.valueOf(player.getScore()));

            nameLabel.setMinWidth(100);
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            scoreLabel.setMinWidth(50);
            scoreLabel.setAlignment(Pos.CENTER_RIGHT);

            rankingGrid.add(rankLabel, 0, i);
            rankingGrid.add(nameLabel, 1, i);
            rankingGrid.add(scoreLabel, 2, i);
        }

        leaderboardContainer.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px;"
        );
        leaderboardContainer.getChildren().add(rankingGrid);
    }
}
