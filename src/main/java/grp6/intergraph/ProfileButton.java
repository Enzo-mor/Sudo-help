package grp6.intergraph;

import javafx.scene.control.Button;

public class ProfileButton extends Button{
    private Double buttonWidth = 180.0;
    private Double buttonHeight = 30.0;
    private String buttonStyle = "-fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 1px; -fx-background-radius: 5px;  -fx-border-radius: 5px; -fx-background-color: #939cb5; ";

    ProfileButton(String text) {
        super(text);

        setMinWidth(buttonWidth);
        setMinHeight(buttonHeight);
        setStyle(buttonStyle);

    }
}
