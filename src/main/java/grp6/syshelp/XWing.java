package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;
import grp6.sudocore.*;

public class XWing implements InterfaceTech {

    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        // Parcourir chaque chiffre possible (1 à 9)
        for (int num = 1; num <= 9; num++) {
            List<Integer> lignesCandidates = new ArrayList<>();

            // Étape 1: Trouver les lignes avec exactement 2 occurrences du candidat
            for (int i = 0; i < 9; i++) {
                Cell[] ligne = grille.getLine(i);
                if (nbAnnotation(ligne, num) == 2) {
                    lignesCandidates.add(i);
                }
            }

            // Étape 2: Rechercher une paire de lignes ayant les mêmes colonnes candidates
            for (int i = 0; i < lignesCandidates.size(); i++) {
                int ligne1 = lignesCandidates.get(i);
                List<Integer> colonnes1 = getColonnesAvecAnnotation(grille.getLine(ligne1), num);

                for (int j = i + 1; j < lignesCandidates.size(); j++) {
                    int ligne2 = lignesCandidates.get(j);
                    List<Integer> colonnes2 = getColonnesAvecAnnotation(grille.getLine(ligne2), num);

                    // Vérifier si les deux lignes ont exactement les mêmes colonnes candidates
                    if (colonnes1.equals(colonnes2) && colonnes1.size() == 2) {
                        aide.addLine(ligne1);
                        aide.addLine(ligne2);
                        aide.addColumn(colonnes1.get(0));
                        aide.addColumn(colonnes1.get(1));

                        aide.setMessage(1, "La méthode du X-Wing peut être utilisée dans la grille.");
                        aide.setMessage(2, "Le chiffre " + num + " forme un X-Wing dans les colonnes " 
                                            + colonnes1.get(0) + " et " + colonnes1.get(1));
                        aide.setMessage(3, "Éliminez " + num + " des autres cellules de ces colonnes.");

                        return aide; // On retourne dès qu'on trouve un X-Wing
                    }
                }
            }
        }
        return null; // Aucun X-Wing trouvé
    }

    private int nbAnnotation(Cell[] data, int num) {
        int res = 0;
        for (Cell c : data) {
            if (c.getAnnotationsBool()[num - 1]) {
                res++;
            }
        }
        return res;
    }

    private List<Integer> getColonnesAvecAnnotation(Cell[] ligne, int num) {
        List<Integer> colonnes = new ArrayList<>();
        for (int c = 0; c < 9; c++) {
            if (ligne[c].getAnnotationsBool()[num - 1]) {
                colonnes.add(c);
            }
        }
        return colonnes;
    }

    public static void main(String[] args) {
        XWing tech = new XWing();

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

        Help help = tech.getHelp(grid);
        if (help == null) {
            System.out.println("Pas de X-Wing trouvé");
        } else {
            System.out.println("X-Wing trouvé !!");
            System.out.println(help);
        }
    }
}
