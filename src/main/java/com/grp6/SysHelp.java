package com.grp6;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: représente le système d'aide pour la résolution d'un Sudoku.
 * Il implémente plusieurs techniques pour identifier les valeurs possibles des cases.
 * Ces techniques sont ajoutées dynamiquement à une liste et appliquées pour résoudre le Sudoku.
 * 
 * @author Kilian POUSSE
 * 
 * <ul style="list-style-type:none;">
 *   <li><b>LastCell:</b> Dernière case libre d'un bloc, ligne ou colonne.</li>
 *   <li><b>LastPossible:</b> Dernière case où un chiffre donné peut être placé.</li>
 *   <li><b>LastNumber:</b> Dernier chiffre possible dans une case.</li>
 *   <li><b>NakedPairs:</b> Deux chiffres apparaissant uniquement dans deux cases d'une unité.</li>
 *   <li><b>NakedTriples:</b> Trois chiffres apparaissant uniquement dans trois cases d'une unité.</li>
 *   <li><b>HiddenSingle:</b> Un chiffre qui ne peut être placé que dans une seule case d'une unité.</li>
 *   <li><b>HiddenPairs:</b> Deux chiffres qui n'apparaissent qu'à deux positions spécifiques dans une unité.</li>
 *   <li><b>HiddenTriples:</b> Trois chiffres qui n'apparaissent qu'à trois positions spécifiques dans une unité.</li>
 *   <li><b>PointingPairs:</b> Deux chiffres restreints à une seule région, permettant d'éliminer d'autres possibilités.</li>
 *   <li><b>PointingTriples:</b> Trois chiffres restreints à une seule région, permettant d'éliminer d'autres possibilités.</li>
 *   <li><b>XWing:</b> Une technique basée sur la disposition en croix de candidats dans une grille.</li>
 *   <li><b>YWing:</b> Une technique avancée basée sur une structure en forme de Y reliant trois cellules.</li>
 *   <li><b>Swordfish:</b> Une extension de la technique X-Wing impliquant trois lignes et colonnes.</li>
 * </ul>
 */
public class SysHelp {

    /** Liste des technique applicable sur une grille */
    public static final List<InterfaceTech> TECHNIQUES = new ArrayList<>();
    static {
        TECHNIQUES.add(new LastCell());         //  1-Derniere case lible
        TECHNIQUES.add(new LastPossible());     //  2-Derniere case restante
        TECHNIQUES.add(new LastNumber());       //  3-Dernier chiffre possible
      //TECHNIQUES.add(new NakedSingleton());   //  4-Singletons nus
        TECHNIQUES.add(new NakedPairs());       //  5-Paires nus
        TECHNIQUES.add(new NakedTriples());     //  6-Triplets nus
        TECHNIQUES.add(new HiddenSingle());     //  7-Singletons cachés
        TECHNIQUES.add(new HiddenPairs());      //  8-Paires cachées 
        TECHNIQUES.add(new HiddenTriples());    //  9-Triplets cachés
        TECHNIQUES.add(new PointingPairs());    // 10-Paires pointantes
        TECHNIQUES.add(new PointingTriples());  // 11-Triplets pointants
        TECHNIQUES.add(new XWing());            // 12-XWing
        TECHNIQUES.add(new YWing());            // 13-YWing
        TECHNIQUES.add(new Swordfish());        // 14-Swordfish
    }

    /**
     * Methode pour verifier si une grille pour donner un coups
     * @param grid Grille a verifier [Grid]
     * @return Vrai si un coups est posible [boolean]
     */
    public static boolean check(Grid grid) {
        for(Cell cell: grid) {
            if(cell.getAnnotations().size() <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recherche des techniques applicable sur la grille actuel
     * @return
     */
    public static List<InterfaceTech> search(Grid g) {
        System.out.println("Recherche de techniques dans la grille ......");
        List<InterfaceTech> techs = new ArrayList<>();
        Grid grid = g/* .clone()*/; // TODO - Quand il y aura le merge general, remettre le clonnage

        for(InterfaceTech t: TECHNIQUES) {
            String techName = t.getClass().getSimpleName();
            System.out.println("Technique a check: " + techName);

            // D: Detecter une technique
            if(t.detect(grid)) {    
                          
                
                System.out.println("La technique '" + techName + "' a ete trouvee");

                // A: Appliquer la techinque
                t.applique(grid);
                techs.add(t);

                // V: Verifie si un coups est possible
                if(check(grid)) {
                    System.out.println("Un coups a ete trouvee avec la thechnique: " + techName);
                    break;
                }

            }
            
        }

        return techs;
    }

    /**
     * Demande de l'aide au systeme d'aide 
     * @return L'aide trouver par le systeme d'aide
     */
    public static int ask(Grid g) {
        
        List<InterfaceTech> techs = SysHelp.search(g);

        // TODO - Recuperer les messages d'aides ou construire, je ne sais pas mdr

        return 0;
    }

    public static void main(String[] args) {
        int[] data = {
                0,0,0, 0,8,7, 3,0,9,
                0,0,0, 9,0,6, 0,0,0,
                0,4,5, 0,0,0, 0,0,0,

                0,0,4, 8,0,0, 6,0,5,
                2,8,0, 0,0,0, 0,9,1,
                5,0,6, 0,0,1, 7,0,0,

                0,0,0, 0,0,0, 5,6,0,
                0,0,0, 3,0,8, 0,0,0,
                4,0,8, 5,6,0, 0,0,0
            };
        Grid grid = new Grid(data);

        System.out.println(grid);

        List<InterfaceTech> techs = SysHelp.search(grid);

        System.out.println(techs);
    }
}
