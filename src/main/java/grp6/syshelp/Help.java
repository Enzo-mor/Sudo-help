package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;

import grp6.sudocore.Grid;

/**
 * Cette classe represente une aide qui sera apportee par le Systeme d'aide.
 * Elle contient des messages d'aide et les cellules concernees par cette aide.
 * 
 * @author POUSSE Kilian
 * @see Grid
 */
public class Help {

    /* ===== Constantes de classe ===== */
    
    /** 
     * Nombre de niveaux d'aide disponibles 
     */
    public static final int NB_LVL = 3;

    /* ===== Variables d'instances ===== */

    /** 
     * Liste des positions des cellules a mettre en valeur 
     */
    private final boolean[][] grid;

    /** 
     * Liste des messages d'aide 
     */
    private final String[] messages;

    /** 
     * Nom de la technique 
     */
    private final String name;

    /* ====== Constructeurs ====== */

    /**
     * Constructeur d'une aide associee à une technique specifique.
     * @param techName Nom de la technique utilisee pour generer cette aide.
     */
    public Help(String techName) {
        // Initialisation des messages avec des valeurs par defaut
        messages = new String[NB_LVL];
        for(int i = 0; i < NB_LVL; i++) {
            messages[i] = "Message d'aide de niveau " + (i + 1) + " non défini.";
        }

        // Initialisation de la grille d'affichage
        grid = new boolean[Grid.NB_NUM][Grid.NB_NUM];

        name = techName;
    }

    /* ===== Getters & Setters ===== */

    /**
     * Recupere les positions des cellules mises en valeur.
     * @return Matrice booleenne indiquant les cellules concernees.
     */
    public boolean[][] getPositions() {
        return grid;
    }

    /**
     * Retourne la liste des cellules a afficher sous forme de liste de points.
     * @return Liste des cellules concernees.
     */
    public List<int[]> getDisplay() {
        List<int[]> res = new ArrayList<>();
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                if(grid[i][j]) res.add(new int[] {i, j});
            }
        }
        return res;
    }

    /**
     * Definit un message d'aide a un niveau specifique.
     * @param level Niveau d'aide (1, 2 ou 3).
     * @param message Texte du message d'aide.
     */
    public void setMessage(int level, String message) {
        if(level >= 1 && level <= NB_LVL) {
            messages[level - 1] = message;
        } else {
            throw new IllegalArgumentException("Le niveau d'aide doit être compris entre 1 et " + NB_LVL);
        }
    }

    /**
     * Recupere le message d'aide d'un niveau donne.
     * @param level Niveau d'aide (1, 2 ou 3).
     * @return Message correspondant au niveau d'aide.
     */
    public String getMessage(int level) {
        if(level >= 1 && level <= NB_LVL) {
            return messages[level - 1];
        } else {
            throw new IllegalArgumentException("Le niveau d'aide doit être compris entre 1 et " + NB_LVL);
        }
    }

    /**
     * Recuperattion du nom de la technique
     * @return Nom de la technique
     */
    public String getName() {
        return name;
    }

    /* ======= Methodes d'affichage ======= */

    /**
     * Ajoute une cellule a l'affichage de l'aide.
     * @param x Coordonnee ligne.
     * @param y Coordonnee colonne.
     */
    public void addPos(int x, int y) {
        if(x >= 0 && x < Grid.NB_NUM && y >= 0 && y < Grid.NB_NUM) {
            grid[x][y] = true;
        }
    }

    /**
     * Ajoute une ligne entiere a l'affichage.
     * @param i Indice de la ligne.
     */
    public void addLine(int i) {
        for(int j = 0; j < Grid.NB_NUM; j++) {
            addPos(i, j);
        }
    }

    /**
     * Ajoute une colonne entiere a l'affichage.
     * @param i Indice de la colonne.
     */
    public void addColumn(int i) {
        for(int j = 0; j < Grid.NB_NUM; j++) {
            addPos(j, i);
        }
    }

    /**
     * Ajoute une sous-grille a l'affichage.
     * @param i Indice ligne de la sous-grille (0 à 2).
     * @param j Indice colonne de la sous-grille (0 à 2).
     */
    public void addSquare(int i, int j) {
        if(i < 0 || i >= 3 || j < 0 || j >= 3) return;

        int startRow = Grid.NB_SUBGRID * i;  
        int startCol = Grid.NB_SUBGRID * j; 

        for(int di = 0; di < Grid.NB_SUBGRID; di++) {
            for(int dj = 0; dj < Grid.NB_SUBGRID; dj++) {
                addPos(startRow + di, startCol + dj);
            }
        }
    }

    /**
     * Ajoute une sous-grille a l'affichage via son index (0 à 8).
     * @param i Indice de la sous-grille.
     */
    public void addSquare(int i) {
        addSquare(i / 3, i % 3);
    }

    /**
     * Remet les positions a zero
     */
    public void resetPositions(){
        for (int i = 0;i<9;i++){
            for(int y = 0;y<9;y++){
                grid[i][y] = false;
            }
        }
    }

    /**
     * Affichage de l'aide sous forme de texte.
     * @return Chaine de caracteres contenant l'aide.
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("HELP("+getName()+"):\n");
        
        // Affichage des messages
        for(int i = 0; i < NB_LVL; i++) {
            res.append("Niveau d'aide n°").append(i + 1).append(":\n   ").append(messages[i]).append("\n");
        }

        // Affichage de la grille
        boolean[][] pos = getPositions();
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                res.append(pos[i][j] ? "X " : ". ");

                // Séparation verticale
                if(j % 3 == 2 && j != Grid.NB_NUM - 1) {
                    res.append("| ");
                }
            }
            res.append("\n");

            // Séparateur horizontal
            if(i % 3 == 2 && i != Grid.NB_NUM - 1) {
                res.append("------+-------+------\n");
            }
        }
        return res.toString();
    }

    /**
     * Methode principale pour tester l'affichage des aides en fonction des lignes, colonnes et sous-grilles.
     * Cette methode cree des instances de la classe {@code Help} et y ajoute des indices de lignes, colonnes 
     * et sous-grilles, puis affiche le resultat.
     * 
     * @param args Arguments de la ligne de commande (non utilises).
     */
    public static void main(String[] args) {
        // Test affichage ligne par ligne
        for(int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addLine(i);
            System.out.println(help);
        }

        // Test affichage colonne par colonne
        for(int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addColumn(i);
            System.out.println(help);
        }

        // Test affichage par sous-grilles
        for(int i = 0; i < 9; i++) {
            Help help = new Help("Test");
            help.addSquare(i);
            System.out.println(help);
        }
    }
}
