package grp6.syshelp;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;


/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de LastPossible.
 * @see InterfaceTech
 * @author ROQUAIN Louison /GRAMMONT Dylan
 */
public class LastPossible implements InterfaceTech{
    
    
    public Help getHelp(Grid grille){
        Help aide = new Help(getClass().getSimpleName());

        for (int i = 0; i < Grid.NB_NUM;i++){
            int[] indiceCell = grille.numToPosForSubGrid(i);
            Cell mat[][] = grille.getSubGrid(indiceCell[0], indiceCell[1]);

            //On récupère les annotations de la région
            ArrayList<List<Integer>> tabCarre = new ArrayList<List<Integer>>();
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    tabCarre.add(mat[j][k].getAnnotations());
                }
            }

            //on verifie si il y a qu'une annotation dans la region
            for (int j = 0; j < 9; j++){
                if (tabCarre.get(j).size() == 1){
                    aide.addSquare(i);
                    aide.setMessage(1, "Fais attention aux "+tabCarre.get(j).get(0));
                    aide.setMessage(2, "Le chiffre "+tabCarre.get(j).get(0)+" est le seul possible dans la région");
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
        }
        return aide;
            
            
           
    }


    /**
     * Retourne la phrase correspondant à la zone de 3×3 cellules.
     * @param indCarre la position du carré : [[1,2,3], [4,5,6], [7,8,9]]
     * @return La phrase correspondante de la zone 3×3.
     */
    private String leCarre(int indCarre) {
        switch (indCarre) {
            case 1: return " en haut à gauche";
            case 2: return " en haut au centre";
            case 3: return " en haut à droite";
            case 4: return " au milieu à gauche";
            case 5: return " au milieu au centre";
            case 6: return " au milieu à droite";
            case 7: return " en bas à gauche";
            case 8: return " en bas au centre";
            case 9: return " en bas à droite";
            default: return "Indice invalide, doit être entre 1 et 9";
        }
    }

    public static void main(String[] args) {
        int[] dataCarre={0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,8,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         0,0,0,0,0,0,0,0,0,
                         5,4,3,0,8,0,0,0,0,
                         0,6,2,0,0,0,0,0,0,
                         9,1,7,0,0,0,0,0,0};
        Grid grilleCarre =new Grid(dataCarre);
        AutoAnnotation.generate(grilleCarre);
        System.out.println(grilleCarre.toString());

        System.out.println(grilleCarre.getCell(7,0).getAnnotations());
        LastPossible v = new LastPossible();
        System.out.println(v.getHelp(grilleCarre));
    }

}
