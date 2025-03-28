package grp6.syshelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
import grp6.sudocore.SudoLog;

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
        TECHNIQUES.add(new NakedSingleton());   //  4-Singletons nus
        TECHNIQUES.add(new NakedPairs());       //  5-Paires nus
        //TECHNIQUES.add(new NakedTriples());     //  6-Triplets nus
        TECHNIQUES.add(new HiddenSingle());     //  7-Singletons cachés
        TECHNIQUES.add(new HiddenPairs());      //  8-Paires cachées 
        TECHNIQUES.add(new HiddenTriples());    //  9-Triplets cachés
        TECHNIQUES.add(new PointingPairs());    // 10-Paires pointantes
        TECHNIQUES.add(new PointingTriples());  // 11-Triplets pointants
        TECHNIQUES.add(new XWing());            // 12-XWing
        //TECHNIQUES.add(new YWing());            // 13-YWing
        //TECHNIQUES.add(new Swordfish());        // 14-Swordfish
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
     * Generation d'une aide a partir d'une grille donnee
     * @param g Grille du joueur
     * @return L'aide qui peut etre apporte par le Systeme d'aide
     */
    public static Help generateHelp(Grid g) {
        SudoLog.debug("Clone de la grille");
        Grid clone = g.clone();
        Help aide = new Help("Eurreur anotation");

        // On remplit les annotations
        AutoAnnotation.generate(clone);
        
        

        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {

                if(g.getCell(i, j).getAnnotations().size() == 1 && clone.getCell(i, j).getAnnotations().size() >1  ) {
                    System.out.println("La casse : "+i+" "+j+"");
                    System.out.println(" Grille joueur : "+g.getCell(i, j).getAnnotations().size()+"");
                    System.out.println(" Grille clone : "+clone.getCell(i, j).getAnnotations().size()+"\n");
                    aide.setMessage(1, "Tu peux rajouter des annotation");
                    SudoLog.debug("Tu peux rajouter des annotation");    
                }
                
                if(!(clone.getCell(i, j).getAnnotations().containsAll(g.getCell(i,j).getAnnotations()))){

                    aide.addPos(i, j);
                    aide.setMessage(3, "Il y a des erreur dans les annotations");

                }
            }
        }



        SudoLog.debug("Generation des annotations");
       // AutoAnnotation.generate(clone);
       Optional<Help> help = TECHNIQUES.stream()
                                        .filter(tech -> { 
                                            SudoLog.debug("Teste avec " + tech.getClass().getSimpleName());
                                            return tech.getHelp(g.clone()) != null;
                                             })
                                         .reduce((a, b) -> Math.random() > 0.5 ? a : b)
                                         .map(tech -> tech.getHelp(g.clone())); 

        if (help.isPresent()) {
        return  help.get();
        }
        SudoLog.debug("Aucune aide n'a été trouvée");
    
        return new NoHelp(); 
    }
    

    public static void main(String[] args) {
        SudoLog.setDebug(true);

        int[] data = {
            6,1,0, 9,0,8, 4,0,0,
            9,8,0, 5,0,0, 0,0,0,
            5,0,3, 0,0,6, 0,1,9,

            0,0,0, 6,0,0, 5,0,4,
            0,0,2, 0,9,0, 3,8,6,
            8,0,5, 0,2,3, 0,7,0,

            0,0,0, 0,0,4, 0,0,7,
            0,5,6, 0,0,0, 1,4,8,
            7,0,8, 0,0,0, 2,9,5
        };
        Grid grille = new Grid(data); 

        System.out.println("🔹 Grille de départ :");
        System.out.println(grille);

        // Demande d'aide au système
        Help help = SysHelp.generateHelp(grille);

        System.out.println(help);
    }
}
