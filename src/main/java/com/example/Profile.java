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
     * Contructeur de la classe Profile
     * @param pseudo Pseudonyme du profile
     */
    public Profile(String pseudo) {
        this.pseudo = pseudo;
    }

    /* ====== Getter & Setter ====== */

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
