package grp6.intergraph;
import javafx.stage.Stage;


public class MenusController {
    public static void launcher(Stage primaryStage){
        ProfileSelection ps = ProfileSelection.getInstance();
        ps.showProfileSelection(primaryStage);
    }
}