package grp6.intergraph;
import javafx.stage.Stage;

/**
 * Classe MenusController
 * Cette classe gere le lancement des differents menus de l'application.
 * Elle permet d'afficher l'ecran de selection de profil au demarrage.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see ProfileSelection
 */
public class MenusController {

    /**
     * Lance l'interface de selection de profil.
     * 
     * @param primaryStage Fenetre principale de l'application [Stage]
     */
    public static void launcher(Stage primaryStage){
        ProfileSelection ps = ProfileSelection.getInstance();
        ps.showProfileSelection(primaryStage);
    }
}