package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Classe representant une technique de resolution pour le Sudoku.
 * Chaque technique a un identifiant, un nom, une description courte et une description longue.
 * Elle est associee a une grille de Sudoku initiale et une grille resolue.
 * 
 * @author POUSSE Kilian
 */
public class Technique {

    /** 
     * Identifiant de la technique 
     */
    private final int id;

    /** 
     * Nom de la technique 
     */
    private final String name;

    /** 
     * Description courte de la technique 
     */
    private final String shortDesc;

    /** 
     * Description longue de la technique 
     */
    private final String longDesc;

    /**
     * Grille resolue de la technique
     */
    private final Grid solvedGrid;

    /**
     * Grille exemple de la technique
     */
    private final Grid grid;

    /**
     * Constructeur de la classe Technique.
     * 
     * @param id L'identifiant unique de la technique.
     * @param nameTech Le nom de la technique.
     * @param shortDesc La description courte de la technique.
     * @param longDesc La description longue de la technique.
     * @param data La representation de la grille de Sudoku de la technique.
     * @param finalCells Les cellules resolues de la grille.
     * @param annotFinal Les annotations finales des cellules de la grille.
     */
    public Technique(int id, String nameTech, String shortDesc, String longDesc, String data, String finalCells, String annotFinal) { 
        this.id = id;
        this.name = nameTech;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.grid = new Grid(0, SudoTypes.Difficulty.EASY, data);
        this.solvedGrid = initSolvedGrid(finalCells, annotFinal);
        AutoAnnotation.generate(grid);
    }

    /**
     * Recupere le nom de la technique.
     * 
     * @return Le nom de la technique.
     */
    public String getName() {
        return name;
    }

    /**
     * Recupere l'identifiant unique de la technique.
     * 
     * @return L'ID de la technique.
     */
    public int getId() {
        return id;
    }

    /**
     * Recupere la description courte de la technique.
     * 
     * @return La description courte de la technique.
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Recupere la description longue de la technique.
     * 
     * @return La description longue de la technique.
     */
    public String getLongDesc() {
        return longDesc;
    }

    /**
     * Recupere la grille exemple de la technique.
     * 
     * @return La grille exemple de la technique.
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Initialise la grille resolue de la technique.
     * 
     * @param solvedCells Les cellules resolues.
     * @param solvedAnnot Les annotations resolues.
     * @return La grille resolue.
     */
    private static Grid initSolvedGrid(String solvedCells, String solvedAnnot) {
        Grid grid = new Grid(solvedCells);
        
        // Diviser la chaine en sous-chaines basees sur ':'
        String[] parts = solvedAnnot.split(":");

        // Verifier si le nombre de parties est inferieur a 9, et completer avec des chaines vides si necessaire
        if (parts.length < 9) {
            String[] newParts = new String[9];
            System.arraycopy(parts, 0, newParts, 0, parts.length);
            for (int i = parts.length; i < 9; i++) {
                newParts[i] = ""; // Remplir les cases manquantes avec des chaines vides
            }
            parts = newParts;
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int idx = i * 9 + j;
                if (idx >= parts.length) continue;
        
                String part = parts[idx]; 
                for (char c : part.toCharArray()) {
                    if (Character.isDigit(c) && c != '0') {
                        if (grid.getCell(i, j) == null) {
                            System.err.println("Erreur: La cellule (" + i + "," + j + ") est null !");
                            continue;
                        }
                        grid.getCell(i, j).addAnnotation(Character.getNumericValue(c));
                    }
                }
            }
        }
        return grid;
    }

    /**
     * Recupere la grille resolue de la technique.
     * 
     * @return La grille resolue de la technique.
     */
    public Grid getSolvedGrid() {
        return solvedGrid;
    }
}
