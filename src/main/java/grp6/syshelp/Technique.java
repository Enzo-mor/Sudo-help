package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Classe 
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
     * Constructeur
     * @param id
     * @param nameTech
     * @param shortDesc
     * @param longDesc
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

    public Grid getGrid() {
        return grid;
    }

    private static Grid initSolvedGrid(String solvedCells, String solvedAnnot) {

        Grid grid = new Grid(solvedCells);
        
        // Diviser la chaine en sous-chaines basees sur ':'
        String[] parts = solvedAnnot.split(":");

        // Vérifier si le nombre de parties est inférieur à 9, et compléter avec des chaînes vides si nécessaire
        if (parts.length < 9) {
            String[] newParts = new String[9];
            System.arraycopy(parts, 0, newParts, 0, parts.length);
            for (int i = parts.length; i < 9; i++) {
                newParts[i] = ""; // Remplir les cases manquantes avec des chaînes vides
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

    public Grid getSolvedGrid() {
        return solvedGrid;
    }
}