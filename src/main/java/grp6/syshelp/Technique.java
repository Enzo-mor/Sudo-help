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
    private int id;

    /** 
     * Nom de la technique 
     */
    private String name;

    /** 
     * Description courte de la technique 
     */
    private String shortDesc;

    /** 
     * Description longue de la technique 
     */
    private String longDesc;

    private Grid solvedGrid;

    /**
     * Grille exemple de la technique
     */
    private final Grid grid;

    /**
     * Constructeur
     * @param id
     * @param name
     * @param shortDesc
     * @param longDesc
     */
    public Technique(int id, String name, String shortDesc, String longDesc, String data, String finalCells, String annotFinal) { 
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.grid = new Grid(0, SudoTypes.Difficulty.EASY, data);
        this.solvedGrid = initSolvedGrid(finalCells, data);
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
        
        for(int idx=0; idx<9; idx++) {

            String part = parts[idx];
            int i = idx / 9;
            int j = idx % 9;
            
            // Ajouter chaque caractere de la sous-chaine comme un entiera la liste
            for(char c: part.toCharArray()) {
                if(c != '0')
                    grid.getCell(i, j).addAnnotation(Character.getNumericValue(c));
            }
        }
        return grid;
    }

    public Grid getSolvedGrid() {
        return solvedGrid;
    }
}