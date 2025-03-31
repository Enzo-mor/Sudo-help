package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;
import grp6.sudocore.*;

/**
 * Cette classe represente la technique XWing.
 * 
 * Elle permet de trouver les X-Wings, une technique qui consiste a trouver un chiffre apparaissant exactement deux fois dans deux lignes, 
 * et qui partage les memes colonnes candidates.
 * 
 * @author POUSSE Kilian
 * @see InterfaceTech Contenant les methodes des techniques
 * 
 */
public class XWing implements InterfaceTech {

    /**
     * Constructeur de la classe XWing
     */
    public XWing(){}

    /**
     * Applique la technique X-Wing pour identifier une solution possible dans une grille de Sudoku. 
     * La technique repose sur l'identification de paires de lignes avec des annotations identiques dans deux colonnes.
     * Lorsqu'une telle paire est trouvée, les annotations dans les autres lignes et colonnes sont supprimées, 
     * ce qui aide à réduire les choix possibles pour les cases restantes.
     * 
     * @param grille La grille de Sudoku sur laquelle la technique X-Wing sera appliquée
     * @return Un objet Help contenant des informations sur la technique utilisée, ou null si aucun X-Wing n'a été trouvé
    */
    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        // Parcourir chaque chiffre possible (1 à 9)
        for(int num = 1; num <= 9; num++) {
            List<Integer> lignesCandidates = new ArrayList<>();

            // Etape 1: Identifier les lignes avec exactement 2 annotations pour le candidat
            for(int i = 0; i < 9; i++) {
                Cell[] ligne = grille.getLine(i);
                if(nbAnnotation(ligne, num) == 2) {
                    lignesCandidates.add(i);
                }
            }

            // Etape 2: Rechercher une paire de lignes avec les mêmes colonnes candidates
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
                        aide.setMessage(3, "Le chiffre " + num + " forme un X-Wing dans cette zone, fais attention à bien retirer tes annotations !");

                        removeAnnotations(grille, ligne1, ligne2, colonnes1, num);

                        for(Cell c: grille) {
                            if(c.OnlyOneAnnotation()) {
                                aide.setMessage(3, "Le chiffre " + num + " forme un X-Wing dans cette zone, fais attention à bien retirer tes annotations ! Cela pourrait te mener à un coup avec un " + c.getLastAnnotation() + ".");
                            }
                        }

                        return aide;
                    }
                }
            }
        }
        return null; // Aucun X-Wing trouvé
    }

    /**
     * Compte le nombre d'annotations pour un chiffre donné dans une ligne spécifique de la grille.
     * 
     * @param data La ligne de la grille sur laquelle les annotations doivent être comptées
     * @param num Le chiffre dont le nombre d'annotations est calculé
     * @return Le nombre d'annotations pour le chiffre donné dans la ligne
     */
    private int nbAnnotation(Cell[] data, int num) {
        int res = 0;
        for(Cell c : data) {
            if(c.getAnnotationsBool()[num - 1]) {
                res++;
            }
        }
        return res;
    }

    /**
     * Identifie les colonnes contenant des annotations pour un chiffre donné dans une ligne de la grille.
     * 
     * @param ligne La ligne de la grille dans laquelle les colonnes avec annotations doivent être identifiées
     * @param num Le chiffre dont les colonnes avec annotation sont recherchées
     * @return Une liste contenant les indices des colonnes qui contiennent des annotations pour le chiffre donné
     */
    private List<Integer> getColonnesAvecAnnotation(Cell[] ligne, int num) {
        List<Integer> colonnes = new ArrayList<>();
        for(int c = 0; c < 9; c++) {
            if(ligne[c].getAnnotationsBool()[num - 1]) {
                colonnes.add(c);
            }
        }
        return colonnes;
    }

    /**
     * Supprime les annotations pour un chiffre donné dans les cases de la grille, 
     * en fonction des lignes et colonnes identifiées par la technique X-Wing.
     * 
     * @param grille La grille sur laquelle les annotations doivent être retirées
     * @param ligne1 L'indice de la première ligne candidate
     * @param ligne2 L'indice de la deuxième ligne candidate
     * @param colonnes La liste des colonnes candidates contenant les annotations à retirer
     * @param num Le chiffre dont les annotations doivent être retirées
     */
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

    /**
     * Teste la technique X-Wing sur une grille de Sudoku. Initialise une grille, applique des annotations automatiques 
     * et vérifie si un X-Wing peut être détecté en utilisant cette technique.
     * 
     * @param args Les arguments de la ligne de commande
     */
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
