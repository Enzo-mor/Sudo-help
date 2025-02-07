package com.grp6;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class ToolsPanel {
    private VBox tools;
    private SudokuGrid sudokuGrid;

    public ToolsPanel(SudokuGrid grid) {
        this.sudokuGrid = grid;
        this.tools = new VBox(15);
        tools.setAlignment(Pos.CENTER);

        ImageView binIcon = new ImageView(new Image(getClass().getResourceAsStream("/bin.png")));
        binIcon.setFitWidth(40);
        binIcon.setPreserveRatio(true);

        ImageView pencilIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        pencilIcon.setFitWidth(40);
        pencilIcon.setPreserveRatio(true);

        /* Déclaration des boutons pour l'effaceur et l'annotation */
        Button eraseButton = new Button();
        Button pencilButton = new Button();

        eraseButton.setGraphic(binIcon);
        eraseButton.setOnAction(e -> {
            boolean newMode =!sudokuGrid.getEraseMode();
            sudokuGrid.setEraseMode(newMode);
            sudokuGrid.setAnnotationMode(false);

            if (newMode) {
                eraseButton.setStyle("-fx-background-color: lightgreen;");
                pencilButton.setStyle("");
            } else {
                eraseButton.setStyle("");
            }
        });

        pencilButton.setGraphic(pencilIcon);
        pencilButton.setOnAction(e -> {
            boolean newMode = !sudokuGrid.getAnnotationMode();
            sudokuGrid.setAnnotationMode(newMode);
            sudokuGrid.setEraseMode(false);

            if (newMode) {
                pencilButton.setStyle("-fx-background-color: lightgreen;");
                eraseButton.setStyle("");
            } else {
                pencilButton.setStyle("");
            }
        });

        tools.getChildren().addAll(eraseButton, pencilButton);
    }

    // Getters
    public VBox getTools() {
        return tools;
    }

    public Button getEraseButton() {
        return (Button) tools.getChildren().get(0);
    }

    public Button getPencilButton() {
        return (Button) tools.getChildren().get(1);
    }

    // Setters
    public void setEraseButton(Button eraseButton) {
        tools.getChildren().set(0, eraseButton);
    }

    public void setPencilButton(Button pencilButton) {
        tools.getChildren().set(1, pencilButton);
    }

    public void setTools(VBox tools) {
        this.tools = tools;
    }
}