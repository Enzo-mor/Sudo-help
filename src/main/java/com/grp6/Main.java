package com.grp6;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    static String selectedProfile = "Invité";

    private static ProfileSelection profileSelection;
    private static MainMenu mainMenu;
    private static SudokuLibrary sudokuLibrary;
    
    @Override
    public void start(Stage stage) {
        profileSelection = new ProfileSelection();
        mainMenu = new MainMenu();
        sudokuLibrary = new SudokuLibrary();

        showProfileSelection(stage);

        // Sample Sudoku data
        populateSudokus();
    }

    private void populateSudokus() {
        for (int i = 1; i <= 12; i++) {
            //sudokus.add(new Sudoku("Sudoku " + i, "Meilleur temps: " + (i * 10) + "s", "Score: " + (i * 50), i % 2 == 0 ? "Fini" : "Non fini"));
        }
    }

    public static void showProfileSelection(Stage stage) {
        profileSelection.show(stage);
    }

    public static void showMainMenu(Stage stage) {
        mainMenu.show(stage);
    }

    public static void showSudokuLibrary(Stage stage) {
        sudokuLibrary.show(stage);
    }

    public static void showSudokuGame(Stage stage, int sudokuId) {
    }
    



    public static void main(String[] args) {
        launch();
    }

    private static class Sudoku {
        private final String name;
        private final String bestTime;
        private final String score;
        private final String status;

        public Sudoku(String name, String bestTime, String score, String status) {
            this.name = name;
            this.bestTime = bestTime;
            this.score = score;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getBestTime() {
            return bestTime;
        }

        public String getScore() {
            return score;
        }

        public String getStatus() {
            return status;
        }
    }
}