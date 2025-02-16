package com.example;
import javafx.stage.Stage;


public class MenusController {
    public static void launcher(Stage primaryStage){
        ProfileSelection ps = new ProfileSelection();
        ps.showProfileSelection(primaryStage);
    }
}
