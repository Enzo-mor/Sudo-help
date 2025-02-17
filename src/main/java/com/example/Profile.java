package com.example;

/**
 * Classe représentant les profiles.
 * @author Kilian POUSSE
 */
public class Profile {
    
    /* ====== Variables d'instance ====== */

    /** Identifiant du profile */
    private Integer id;

    /** Pseudonyme du profile */
    private String pseudo;


    /* ====== Constructeur d'instances ======= */

    /**
     * Contructeur de la classe Profile.
     * @param id Identifiant du profile
     * @param pseudo Pseudonyme du profile
     */
    public Profile(int id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }

    /* ====== Getter & Setter ====== */

    /**
     * Getter: Récupération de l'identifiant
     * @return Identifiant du profile
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter: Récupération du pseudonyme
     * @return Pseudonyme du profile
     */
    public String getPseudo() {
        return pseudo;
    }

    /* ====== Méthodes d'instance ====== */

    @Override
    public String toString() {
        return "<Profile: " + getPseudo() + ">";
    }

}
