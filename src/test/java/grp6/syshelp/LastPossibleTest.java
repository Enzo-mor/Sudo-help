package grp6.syshelp;


import org.junit.jupiter.api.Test;
import grp6.sudocore.Grid;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste pour la detection de la technique LastPossible
 * @author Kilian POUSSE
 * @see LastPossible
 */
public class LastPossibleTest {
    /** Technique utilisee pour le teste */
    private InterfaceTech tech = new LastPossible();

    /** Nom de la technique */
    private String techName = tech.getClass().getSimpleName();


    /**
     * Teste sur une grille quelconque
     */
    @Test
    void simpleTest() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        System.out.println("--> " + techName + "." + methodName);

        int[] data = {
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,

            0,0,8, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,

            0,0,0, 0,8,0, 0,0,0,
            0,6,0, 0,0,0, 0,0,0,
            9,1,0, 0,0,0, 0,0,0
        };

        Grid grid = new Grid(data);

        // Application de la technique sur la grille
        AutoAnnotation.generate(grid);
        Help help = tech.getHelp(grid);

        assertNotNull(help, "Aucune Technique a ete trouve");
        assertEquals(help.getName(), techName, "Mauvaise technique");

    }
}
