package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;
import grp6.sudocore.*;

/**
 * Cette classe représente la technique NakedSingleton.
 * 
 * Elle permet de trouver les singletons nus(1 chiffre apparaissant uniquement dans une case d'une unité(ligne colones carré)). 
 * @author POUSSE K
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class XWing implements InterfaceTech {

    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        // Parcourir chaque chiffre possible (1 à 9)
        for(int num = 1; num <= 9; num++) {
            List<Integer> lignesCandidates = new ArrayList<>();

            // Étape 1: Trouver les lignes avec exactement 2 occurrences du candidat
            for(int i = 0; i < 9; i++) {
                Cell[] ligne = grille.getLine(i);
                if(nbAnnotation(ligne, num) == 2) {
                    lignesCandidates.add(i);
                }
            }

            // Étape 2: Rechercher une paire de lignes ayant les mêmes colonnes candidates
            for(int i = 0; i < lignesCandidates.size(); i++) {
                int ligne1 = lignesCandidates.get(i);
                List<Integer> colonnes1 = getColonnesAvecAnnotation(grille.getLine(ligne1), num);

                for(int j = i + 1; j < lignesCandidates.size(); j++) {
                    int ligne2 = lignesCandidates.get(j);
                    List<Integer> colonnes2 = getColonnesAvecAnnotation(grille.getLine(ligne2), num);

                    // Vérifier si les deux lignes ont exactement les mêmes colonnes candidates
                    if(colonnes1.equals(colonnes2) && colonnes1.size() == 2) {
                        aide.addLine(ligne1);
                        aide.addLine(ligne2);
                        aide.addColumn(colonnes1.get(0));
                        aide.addColumn(colonnes1.get(1));

                        aide.setMessage(1, "Pense à utiliser les annotations. Une technique est faisable avec les " + num + ".");
                        aide.setMessage(3, "Le chiffre " + num + " forme un X-Wing dans cette zone, fait attention à bien retirer tes annotations !");

                        removeAnnotations(grille, ligne1, ligne2, colonnes1, num);

                        for(Cell c: grille) {
                            if(c.OnlyOneAnnotation()) {
                                aide.setMessage(3, "Le chiffre " + num + " forme un X-Wing dans cette zone, fait attention à bien retirer tes annotations ! Cela pourrait te mener à un coups avec un " + c.getLastAnnotation() + ".");
                            }
                        }


                        return aide; 
                    }
                }
            }
        }
        return null; // Aucun X-Wing trouvé
    }

    private int nbAnnotation(Cell[] data, int num) {
        int res = 0;
        for(Cell c : data) {
            if(c.getAnnotationsBool()[num - 1]) {
                res++;
            }
        }
        return res;
    }

    private List<Integer> getColonnesAvecAnnotation(Cell[] ligne, int num) {
        List<Integer> colonnes = new ArrayList<>();
        for(int c = 0; c < 9; c++) {
            if(ligne[c].getAnnotationsBool()[num - 1]) {
                colonnes.add(c);
            }
        }
        return colonnes;
    }

    private void removeAnnotations(Grid grille, int ligne1, int ligne2, List<Integer> colonnes, int num) {
        for(int i = 0; i < 9; i++) {
            if(i != ligne1 && i != ligne2) {
                for(int col : colonnes) {
                    grille.getCell(i, col).removeAnnotation(num);
                }
            }
        }

        for(int col: colonnes) {
            for(int i: List.of(ligne1, ligne2)) {
                for(int j = 0; j < 9; j++) {
                    if(!colonnes.contains(j)) {
                        grille.getCell(i, j).removeAnnotation(num);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        XWing tech = new XWing();

        int[] data = {
            5,3,2, 1,4,6, 7,9,8,
            4,6,9, 0,0,7, 1,3,5,
            0,0,7, 5,3,9, 4,6,2,

            9,0,6, 0,0,0, 0,7,0,
            0,0,0, 7,6,8, 0,0,0,
            0,7,0, 0,9,0, 0,0,6,

            0,0,1, 0,7,0, 6,0,0,
            6,0,0, 9,0,0, 0,1,7,
            7,0,5, 6,1,4, 0,2,0
        };

        Grid grid = new Grid(data);
        AutoAnnotation.generate(grid);

        Help help = tech.getHelp(grid);

        if(help == null) {
            System.out.println("Pas de X-Wing trouvé");
        } else {
            System.out.println("X-Wing trouvé !!");
            System.out.println(help);
        }
    }
}
