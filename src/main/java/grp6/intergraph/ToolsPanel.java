package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class ToolsPanel {
    private final VBox tools;
    private boolean eraseMode = false;    // Mode gomme
    private boolean annotationMode = false; // Mode annotation
    private final Button eraseButton;
    private final Button pencilButton;

    public ToolsPanel() {
        this.tools = new VBox(15);
        tools.setAlignment(Pos.CENTER);

        ImageView binIcon = new ImageView(new Image(getClass().getResourceAsStream("/bin.png")));
        binIcon.setFitWidth(40);
        binIcon.setPreserveRatio(true);

        ImageView pencilIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        pencilIcon.setFitWidth(40);
        pencilIcon.setPreserveRatio(true);

        /* Déclaration des boutons pour l'effaceur et l'annotation */
        eraseButton = new Button();
        pencilButton = new Button();

        eraseButton.setGraphic(binIcon);
        eraseButton.setOnAction(e -> {
            boolean newMode = !getEraseMode();

            if (newMode) {
                setEraseButtonOn();
                setPencilButtonOff();
            } else {
                setEraseButtonOff();
            }
        });

        pencilButton.setGraphic(pencilIcon);
        pencilButton.setOnAction(e -> {
            boolean newMode = !getAnnotationMode();

            if (newMode) {
                setPencilButtonOn();
                setEraseButtonOff();
            } else {
                setPencilButtonOff();
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
    public boolean getEraseMode() {
        return eraseMode;
    }

    public boolean getAnnotationMode() {
        return annotationMode;
    }
    
    
    // Setters
    public void setEraseButtonOn() {
        eraseMode = true;
        eraseButton.setStyle("-fx-background-color: lightgreen;");
    }

    public void setPencilButtonOn() {
        annotationMode = true;
        pencilButton.setStyle("-fx-background-color: lightgreen;");

        SudokuDisplay.resetGrid(SudokuGrid.getGridPane());

        // Réinitialiser le nombre sélctionné
        if(NumberSelection.getSelectedNumber() != null) {
            NumberSelection.resetSelectedNumber();
            NumberSelection.clearSelection();
        }
    }

    public void setEraseButtonOff() {
        eraseMode = false;
        eraseButton.setStyle("");
    }

    public void setPencilButtonOff() {
        annotationMode = false;
        pencilButton.setStyle("");

        // Remettre le style de la cellule selectionné d'avant par default
        Button selectedCell = SudokuGrid.getSelectedCell();
        int selectedRow = SudokuGrid.getSelectedRow();
        int selectedCol = SudokuGrid.getSelectedCol();
        if (selectedCell != null) {
            selectedCell.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
        }
    }
}