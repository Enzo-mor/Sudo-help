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
 * Cette classe représente le menu du classement des joueurs en fonction des grilles de Sudoku.
 * Elle permet de consulter le classement des meilleurs joueurs par difficulté et par grille spécifique.
 *
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see DBManager
 * @see Player
 * @see StyledContent
 * @see ProfileButton
 * @see GameplayChoice
 */
public class LeaderboardMenu {
    /** Index de la difficulté sélectionnée */
    private static int currentDifficultyIndex = 0;
    /** Liste des niveaux de difficulté disponibles */
    private static final String[] DIFFICULTIES = {"facile", "moyen", "difficile"};
    /** ID de la grille sélectionnée */
    private static int selectedGridId = -1;

    /**
     * Affiche le menu du classement des joueurs.
     *
     * @param stage Fenêtre principale de l'application [Stage]
     */
    public static void showLeaderBoard(Stage stage) {
        currentDifficultyIndex = 0;

        // Boutons de navigation
        Button leftArrow = new Button("<");
        Button rightArrow = new Button(">");

        StyledContent.applyArrowButtonStyle(leftArrow);
        StyledContent.applyArrowButtonStyle(rightArrow);

        // Label pour la difficulté
        Label difficultyLabel = new Label(DIFFICULTIES[currentDifficultyIndex]);
        difficultyLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // ComboBox pour choisir une grille spécifique
        ComboBox<String> gridSelector = new ComboBox<>();
        gridSelector.setMinWidth(200);

        // Conteneur pour afficher le classement
        VBox leaderboardContainer = new VBox(10);
        leaderboardContainer.setAlignment(Pos.CENTER);

        // Récupération des grilles disponibles
        Map<Integer, String> grids = DBManager.getGridsByDifficulty(DIFFICULTIES[currentDifficultyIndex]);
        gridSelector.getItems().addAll(grids.values());

        // Sélection de la première grille
        if (!grids.isEmpty()) {
            selectedGridId = grids.keySet().iterator().next();
            gridSelector.getSelectionModel().selectFirst();
        }

        // Actions pour changer de difficulté
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

        VBox layout = new VBox(15, navigation, gridSelector, leaderboardContainer, backButton);
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Mode Libre - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Change la difficulté affichée et met à jour la liste des grilles et le classement.
     *
     * @param delta Valeur de changement d'index de difficulté [-1 ou 1]
     * @param difficultyLabel Label affichant la difficulté actuelle [Label]
     * @param gridSelector ComboBox permettant la sélection d'une grille [ComboBox]
     * @param leaderboardContainer Conteneur affichant le classement [VBox]
     */
    private static void changeDifficulty(int delta, Label difficultyLabel, ComboBox<String> gridSelector, VBox leaderboardContainer) {
        currentDifficultyIndex = (currentDifficultyIndex + delta + DIFFICULTIES.length) % DIFFICULTIES.length;
        difficultyLabel.setText(DIFFICULTIES[currentDifficultyIndex]);

        // Mise à jour des grilles disponibles
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
     * Met à jour l'affichage du classement des meilleurs joueurs pour la grille sélectionnée.
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

        leaderboardContainer.getChildren().add(rankingGrid);
    }
}
