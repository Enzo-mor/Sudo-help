package grp6.syshelp;

import grp6.sudocore.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

/**
 * Cette classe represente une aide qui sera apporte par le Systeme d'aide.
 * Attention, avant de recuperer un message de cette aide, il faut initialiser 
 * la Base de Donnees avec la class 'DBManager'.
 * @author Kilian POUSSE
 * 
 * @see SysHelp Classe qui represente le systeme d'aide
 * @see DBManager Classe qui gere la base de donnees
 */
public class Help {

    /* ===== Constantes de classe ===== */
    
    /** Nombre de niveau d'aide disponible */
    public static final int NB_LVL = 3;




    /* ===== Variable d'instances ===== */

    /** Liste des positions des cellules qui doit etre visible */
    private boolean[][] grid;

    /** Liste des messages d'aide a affciher au joueur */
    private String[] messages;




    /* ====== Constructeurs d'instances ====== */

    /**
     * Constructeur d'une instance d'une aide depuis un type d'aide
     * @param techName
     */
    public Help(String techName) {
        // TODO - Recuperer depuis la bdd les messages selon un type d'aide
        messages = new String[NB_LVL];
        grid = new boolean[Grid.NB_NUM][Grid.NB_NUM];
    }


    /* ===== Getter & Setter ===== */

    /**
     * Getter: Recuperation des positions des cellules qui doit etre visible
     * @return positions des cellules qui doit etre visible
     */
    public boolean[][] getPositions() {
        return grid;
    }

    /**
     * Retourne la liste des cellules a afficher
     * @return liste des cellules a afficher
     */
    public List<Point> getDisplay() {
        List<Point> res = new ArrayList<>();
        for(int i=0; i<Grid.NB_NUM; i++) {
            for(int j=0; j<Grid.NB_NUM; j++) {
                if(grid[i][j]) res.add(new Point(i, j));
            }
        }
        return res;
    }



    /* ======= Methodes d'instances ======= */

    /**
     * Ajout une position dans l'affichage de l'aide
     * @param x Coordonnee sur la ligne
     * @param y Coordonnee sur la colonne
     */
    public void addPos(int x, int y) {
        grid[x][y] = true;
    }

    /**
     * Ajout une ligne a l'affichage 
     * @param i indice de la ligne
     */
    public void addLine(int i) {
        for(int j=0; j<Grid.NB_NUM; j++) {
            addPos(i, j);
        }
    }

    /**
     * Ajout une colonne a l'affichage 
     * @param i indice de la colonne
     */
    public void addColomn(int i) {
        for(int j=0; j<Grid.NB_NUM; j++) {
            addPos(j, i);
        }
    }

    /**
    * Ajoute une sous-grille à l'affichage 
    * @param i Indice sur X de la sous-grille (indexe de 0 à 2)
    * @param j Indice sur Y de la sous-grille (indexe de 0 à 2)
    */
    public void addSquare(int i, int j) {
        int n_line = Grid.NB_SUBGRID * i;  
        int n_column = Grid.NB_SUBGRID * j; 

        for(int di = 0; di < Grid.NB_SUBGRID; di++) {
            for(int dj = 0; dj < Grid.NB_SUBGRID; dj++) {
                addPos(di + n_line, dj + n_column);
            }
        }
    }

    /**
     * Ajoute une sous-grille à l'affichage
     * @param i Indice de la sous-grille (indexe de 0 à 8)
     */
    public void addSquare(int i) {
        addSquare(i / 3, i % 3);
    }



    @Override
    public String toString() {
        String res = "";
        for(int i=0; i<NB_LVL; i++) {
            res += "Niveau d'aide n°1:\n   " + messages[i] + "\n";
        }

        boolean[][] pos = getPositions();
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for (int j = 0; j < Grid.NB_NUM; j++) {
                if(pos[i][j]) {
                    res += "X ";
                }
                else {
                    res += "  ";
                }

                // Ajouter une separation verticale tous les 3 colonnes (sauf à la fin)
                if(j % 3 == 2 && j != Grid.NB_NUM - 1) {
                    res += "| ";
                }
            }
            res += "\n";

            // Ajouter un separateur horizontal tous les 3 lignes (sauf à la fin)
            if(i % 3 == 2 && i != Grid.NB_NUM - 1) {
                res += "------+-------+------\n";
            }
        }
        return res;
    }

    public static void main(String[] args) {
        
        for(int i=0; i<9; i++) {
            Help help = new Help("supercool");
            help.addLine(i);

            System.out.println(help);
        }

        for(int i=0; i<9; i++) {
            Help help = new Help("supercool");
            help.addColomn(i);

            System.out.println(help);
        }

        for(int i=0; i<9; i++) {
            Help help = new Help("supercool");
            help.addSquare(i);

            System.out.println(help);
        }

    }


}
