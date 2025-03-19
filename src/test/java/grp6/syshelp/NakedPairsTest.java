package grp6.syshelp;

import org.junit.jupiter.api.Test;
import grp6.sudocore.Grid;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste pour la detection de la technique NakedPairs
 * @author Kilian POUSSE
 * @see NakedPairs
 */
public class NakedPairsTest {

    /** Technique utilisee pour le teste */
    private InterfaceTech tech = new NakedPairs();

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
            0,0,2, 0,8,5, 0,0,4,
            0,0,0, 0,3,0, 0,6,0,
            0,0,4, 2,1,0, 0,3,0,

            0,0,0, 0,0,0, 0,5,2,
            0,0,0, 0,0,0, 3,1,0,
            9,0,0, 0,0,0, 0,0,0,

            8,0,0, 0,0,6, 0,0,0,
            2,5,0, 4,0,0, 0,0,8,
            0,0,0, 0,0,1, 6,0,0
        };

        Grid grid = new Grid(data);

        // Application de la technique sur la grille
        AutoAnnotation.generate(grid);
        Help help = tech.getHelp(grid);

        assertNotNull(help, "Aucune Technique a ete trouve");
        assertEquals(help.getName(), techName, "Mauvaise technique");

    }
}
