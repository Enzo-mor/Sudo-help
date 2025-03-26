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

    /**
     * Grille exemple de la technique
     */
    private Grid grid;

    /**
     * Constructeur
     * @param id
     * @param name
     * @param shortDesc
     * @param longDesc
     */
    public Technique(int id, String name, String shortDesc, String longDesc, String data) { 
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.grid = new Grid(0, SudoTypes.Difficulty.EASY, data);
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



}