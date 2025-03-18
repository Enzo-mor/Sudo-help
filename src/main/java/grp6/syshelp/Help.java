package grp6.syshelp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import grp6.sudocore.Grid;

/**
 * Cette classe représente une aide qui sera apportée par le Système d'aide.
 * Elle contient des messages d'aide et les cellules concernées par cette aide.
 * 
 * @author Kilian POUSSE
 */
public class Help {

    /* ===== Constantes de classe ===== */
    
    /** Nombre de niveaux d'aide disponibles */
    public static final int NB_LVL = 3;

    /* ===== Variables d'instances ===== */

    /** Liste des positions des cellules à mettre en valeur */
    private boolean[][] grid;

    /** Liste des messages d'aide */
    private String[] messages;

    /* ====== Constructeurs ====== */

    /**
     * Constructeur d'une aide associée à une technique spécifique.
     * @param techName Nom de la technique utilisée pour générer cette aide.
     */
    public Help(String techName) {
        // Initialisation des messages avec des valeurs par défaut
        messages = new String[NB_LVL];
        for (int i = 0; i < NB_LVL; i++) {
            messages[i] = "Message d'aide de niveau " + (i + 1) + " non défini.";
        }

        // Initialisation de la grille d'affichage
        grid = new boolean[Grid.NB_NUM][Grid.NB_NUM];
    }

    /* ===== Getters & Setters ===== */

    /**
     * Récupère les positions des cellules mises en valeur.
     * @return Matrice booléenne indiquant les cellules concernées.
     */
    public boolean[][] getPositions() {
        return grid;
    }

    /**
     * Retourne la liste des cellules à afficher sous forme de liste de points.
     * @return Liste des cellules concernées.
     */
    public List<Point> getDisplay() {
        List<Point> res = new ArrayList<>();
        for (int i = 0; i < Grid.NB_NUM; i++) {
            for (int j = 0; j < Grid.NB_NUM; j++) {
                if (grid[i][j]) res.add(new Point(i, j));
            }
        }
        return res;
    }

    /**
     * Définit un message d'aide à un niveau spécifique.
     * @param level Niveau d'aide (1, 2 ou 3).
     * @param message Texte du message d'aide.
     */
    public void setMessage(int level, String message) {
        if (level >= 1 && level <= NB_LVL) {
            messages[level - 1] = message;
        } else {
            throw new IllegalArgumentException("Le niveau d'aide doit être compris entre 1 et " + NB_LVL);
        }
    }

    /**
     * Récupère le message d'aide d'un niveau donné.
     * @param level Niveau d'aide (1, 2 ou 3).
     * @return Message correspondant au niveau d'aide.
     */
    public String getMessage(int level) {
        if (level >= 1 && level <= NB_LVL) {
            return messages[level - 1];
        } else {
            throw new IllegalArgumentException("Le niveau d'aide doit être compris entre 1 et " + NB_LVL);
        }
    }

    /* ======= Méthodes d'affichage ======= */

    /**
     * Ajoute une cellule à l'affichage de l'aide.
     * @param x Coordonnée ligne.
     * @param y Coordonnée colonne.
     */
    public void addPos(int x, int y) {
        if (x >= 0 && x < Grid.NB_NUM && y >= 0 && y < Grid.NB_NUM) {
            grid[x][y] = true;
        }
    }

    /**
     * Ajoute une ligne entière à l'affichage.
     * @param i Indice de la ligne.
     */
    public void addLine(int i) {
        for (int j = 0; j < Grid.NB_NUM; j++) {
            addPos(i, j);
        }
    }

    /**
     * Ajoute une colonne entière à l'affichage.
     * @param i Indice de la colonne.
     */
    public void addColumn(int i) {
        for (int j = 0; j < Grid.NB_NUM; j++) {
            addPos(j, i);
        }
    }

    /**
     * Ajoute une sous-grille à l'affichage.
     * @param i Indice ligne de la sous-grille (0 à 2).
     * @param j Indice colonne de la sous-grille (0 à 2).
     */
    public void addSquare(int i, int j) {
        if (i < 0 || i >= 3 || j < 0 || j >= 3) return;

        int startRow = Grid.NB_SUBGRID * i;  
        int startCol = Grid.NB_SUBGRID * j; 

        for (int di = 0; di < Grid.NB_SUBGRID; di++) {
            for (int dj = 0; dj < Grid.NB_SUBGRID; dj++) {
                addPos(startRow + di, startCol + dj);
            }
        }
    }

    /**
     * Ajoute une sous-grille à l'affichage via son index (0 à 8).
     * @param i Indice de la sous-grille.
     */
    public void addSquare(int i) {
        addSquare(i / 3, i % 3);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        
        // Affichage des messages
        for (int i = 0; i < NB_LVL; i++) {
            res.append("Niveau d'aide n°").append(i + 1).append(":\n   ").append(messages[i]).append("\n");
        }

        // Affichage de la grille
        boolean[][] pos = getPositions();
        for (int i = 0; i < Grid.NB_NUM; i++) {
            for (int j = 0; j < Grid.NB_NUM; j++) {
                res.append(pos[i][j] ? "X " : ". ");

                // Séparation verticale
                if (j % 3 == 2 && j != Grid.NB_NUM - 1) {
                    res.append("| ");
                }
            }
            res.append("\n");

            // Séparateur horizontal
            if (i % 3 == 2 && i != Grid.NB_NUM - 1) {
                res.append("------+-------+------\n");
            }
        }
        return res.toString();
    }

    public static void main(String[] args) {
        // Test affichage ligne par ligne
        for (int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addLine(i);
            System.out.println(help);
        }

        // Test affichage colonne par colonne
        for (int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addColumn(i);
            System.out.println(help);
        }

        // Test affichage par sous-grilles
        for (int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addSquare(i);
            System.out.println(help);
        }
    }
}
