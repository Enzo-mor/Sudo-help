package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import grp6.sudocore.Cell;
import grp6.sudocore.DBManager;
import grp6.sudocore.Grid;
import grp6.sudocore.Profile;
import grp6.sudocore.SudoLog;

/**
 * La classe SysHelp represente le systeme d'aide pour la resolution d'un Sudoku.
 * Elle implemente plusieurs techniques pour identifier les valeurs possibles des cases.
 * Ces techniques sont ajoutees dynamiquement a une liste et appliquees pour resoudre le Sudoku.
 * 
 * Les techniques disponibles incluent :
 * - LastCell : Derniere case libre d'un bloc, ligne ou colonne.
 * - LastPossible : Derniere case ou un chiffre donne peut etre place.
 * - LastNumber : Dernier chiffre possible dans une case.
 * - NakedPairs : Deux chiffres apparaissant uniquement dans deux cases d'une unite.
 * - NakedTriples : Trois chiffres apparaissant uniquement dans trois cases d'une unite.
 * - HiddenSingle : Un chiffre qui ne peut etre place que dans une seule case d'une unite.
 * - HiddenPairs : Deux chiffres qui n'apparaissent qu'a deux positions specifiques dans une unite.
 * - HiddenTriples : Trois chiffres qui n'apparaissent qu'a trois positions specifiques dans une unite.
 * - PointingPairs : Deux chiffres restreints a une seule region, permettant d'eliminer d'autres possibilites.
 * - PointingTriples : Trois chiffres restreints a une seule region, permettant d'eliminer d'autres possibilites.
 * - XWing : Une technique basee sur la disposition en croix de candidats dans une grille.
 * - YWing : Une technique avancee basee sur une structure en forme de Y reliant trois cellules.
 * - Swordfish : Une extension de la technique X-Wing impliquant trois lignes et colonnes.
 * 
 * @author POUSSE Kilian
 * @author MOREAU Enzo
 */
public class SysHelp {

    /**
     * Constructeur de la classe SysHelp
     */
    public SysHelp(){}

    /** Liste des techniques applicables sur une grille */
    public static final List<InterfaceTech> TECHNIQUES = new ArrayList<>();
    static {
        TECHNIQUES.add(new LastCell());         // 1 - Derniere case libre
        TECHNIQUES.add(new LastPossible());     // 2 - Derniere case restante
        TECHNIQUES.add(new NakedSingleton());   // 3 - Singletons nus
        TECHNIQUES.add(new NakedPairs());       // 4 - Paires nues
        TECHNIQUES.add(new LastNumber());       // 5 - Dernier chiffre possible
        //TECHNIQUES.add(new NakedTriples());     // 6 - Triplets nus
        //TECHNIQUES.add(new HiddenSingle());    // 7 - Singletons caches
        //TECHNIQUES.add(new HiddenPairs());     // 8 - Paires caches
        //TECHNIQUES.add(new HiddenTriples());   // 9 - Triplets caches
        //TECHNIQUES.add(new PointingPairs());   // 10 - Paires pointantes
        //TECHNIQUES.add(new PointingTriples()); // 11 - Triplets pointants
        TECHNIQUES.add(new XWing());            // 12 - XWing
        //TECHNIQUES.add(new YWing());           // 13 - YWing
        //TECHNIQUES.add(new Swordfish());       // 14 - Swordfish
    }

    /**
     * Methode pour verifier si une grille peut encore recevoir un coup.
     * 
     * @param grid La grille a verifier
     * @return Vrai si un coup est possible, sinon faux
     */
    public static boolean check(Grid grid) {
        for (Cell cell : grid) {
            if (cell.getAnnotations().size() <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Genere une aide a partir d'une grille donnee.
     * 
     * @param g La grille du joueur
     * @param profile Le profil du joueur
     * @return L'aide qui peut etre apportee par le systeme d'aide
     */
    public static Help generateHelp(Grid g, Profile profile) {
        System.out.println(g);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println(g.getCell(4, 4).getAnnotations());
        SudoLog.debug("Clone de la grille");
        Grid clone = g.clone();
        Help aide = new Help("Erreur annotation");

        // On remplit les annotations
        AutoAnnotation.generate(clone);
        for (int i = 0; i < Grid.NB_NUM; i++) {
            for (int j = 0; j < Grid.NB_NUM; j++) {
                Cell cell = clone.getCell(i, j);
            }
        }

        SudoLog.debug("Generation des annotations");
        Optional<Help> help = TECHNIQUES.stream()
            .map(tech -> {
                SudoLog.debug("Test avec " + tech.getClass().getSimpleName());
                return tech.getHelp(clone);
            })
            .filter(h -> h != null)
            .findFirst();

        System.err.println(help);

        if (help.isPresent()) {
            initTechMessage(help, profile);
            return help.get();
        }
        SudoLog.debug("Aucune aide n'a ete trouvee");

        return new NoHelp();
    }

    /**
     * Initialise le message de la technique selon si elle a ete apprise ou non par le joueur.
     * 
     * @param help L'aide a initialiser
     * @param profile Le profil du joueur
     */
    private static void initTechMessage(Optional<Help> help, Profile profile) {
        String name = help.get().getName();
        Technique tech = DBManager.getTechs().stream()
            .filter(t -> t.getName().equals(name))
            .findFirst()
            .orElse(null);

        if (tech != null) {
            if (profile.getAlreadyLearn(tech)) {
                help.get().setMessage(2, tech.getShortDesc());
            } else {
                help.get().setMessage(2, tech.getLongDesc());
            }
        } else {
            SudoLog.error("La technique n'existe pas dans la base de donnees.");
            help.get().setMessage(2, "Aucune technique trouvee.");
        }
    }
}
