package grp6.syshelp;

import org.junit.jupiter.api.Test;
import grp6.sudocore.Grid;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste pour la detection de la technique XWing
 * @author Kilian POUSSE
 * @see XWing
 */
public class XWingTest {

    /** Technique utilisee pour le teste */
    private InterfaceTech tech = new XWing();

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
            0,0,3, 8,0,0, 5,1,0,
            0,0,8, 7,0,0, 9,3,0,
            1,0,0, 3,0,5, 7,2,8,

            0,0,0, 2,0,0, 8,4,9,
            8,0,1, 9,0,6, 2,5,7,
            0,0,0, 5,0,0, 1,6,3,

            9,6,4, 1,2,7, 3,8,5,
            3,8,2, 6,5,9, 4,7,1,
            0,1,0, 4,0,0, 6,9,2
        };

        Grid grid = new Grid(data);
        AutoAnnotation.generate(grid);

        // Application de la technique sur la grille
        Help help = tech.getHelp(grid);

        assertNotNull(help, "Aucune Technique a ete trouve");
        assertEquals(help.getName(), techName, "Mauvaise technique");

    }

    /**
     * Teste avec les annotations
     */
    @Test
    void annotationTest() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        System.out.println("--> " + techName + "." + methodName);

        int[] data = {
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0
        };

        Grid grid = new Grid(data);
        grid.getCell(1, 2).addAnnotation(3);
        grid.getCell(1, 7).addAnnotation(3);
        grid.getCell(5, 2).addAnnotation(3);
        grid.getCell(5, 7).addAnnotation(3);

        // Application de la technique sur la grille
        Help help = tech.getHelp(grid);

        assertNotNull(help, "Aucune Technique a ete trouve");
        assertEquals(help.getName(), techName, "Mauvaise technique");

    }
}
