package grp6.syshelp;

import org.junit.jupiter.api.Test;
import grp6.sudocore.Grid;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste pour la detection de la technique Last Cell
 * @author Kilian POUSSE
 * @see LastCell
 */
public class LastCellTest {

    /** Nom de la technique */
    private String techName = LastCell.class.getSimpleName();

    /**
     * Test sur une ligne
     */
    @Test
    void lineTest() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        System.out.println("--> " + techName + "." + methodName);

        int[] data = {
            1,5,7, 8,0,9, 3,4,2,
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

        // Application de la technique sur la grille
        Help help = SysHelp.generateHelp(grid);

        assertNotNull(help, "L'objet help ne doit pas être null");
        assertEquals(help.getName(), techName);
    }

    /**
     * Test sur une colonne
     */
    @Test
    void columnTest() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        System.out.println("--> " + techName + "." + methodName);

        int[] data = {
            0,0,0, 2,0,0, 0,0,0,
            0,0,0, 3,0,0, 0,0,0,
            0,0,0, 4,0,0, 0,0,0,
            
            0,0,0, 5,0,0, 0,0,0,
            0,0,0, 7,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            
            0,0,0, 8,0,0, 0,0,0,
            0,0,0, 1,0,0, 0,0,0,
            0,0,0, 6,0,0, 0,0,0
        };
        
        Grid grid = new Grid(data);

        // Application de la technique sur la grille
        Help help = SysHelp.generateHelp(grid);

        assertNotNull(help, "L'objet help ne doit pas être null");
        assertEquals(help.getName(), techName);
    }

    /**
     * Test sur une sous-grille
     */
    @Test
    void squareTest() {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        System.out.println("--> " + techName + "." + methodName);

        int[] data = {
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            
            0,0,0, 0,0,0, 1,3,2,
            0,0,0, 0,0,0, 5,0,4,
            0,0,0, 0,0,0, 6,9,8,
            
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,0, 0,0,0
        };
        
        Grid grid = new Grid(data);

        // Application de la technique sur la grille
        Help help = SysHelp.generateHelp(grid);

        assertNotNull(help, "L'objet help ne doit pas être null");
        assertEquals(help.getName(), techName);
    }
}
