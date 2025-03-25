package grp6.sudocore;

import java.sql.SQLException;

/**
 * Classe representant les profils.
 * @author POUSSE Kilian
 * @author DE THESE Taise
 */
public class Profile {
    
    /* ====== Variables d'instance ====== */

    /** Pseudonyme du profil */
    private String pseudo;

    /* ====== Constructeur d'instances ======= */

    /**
     * Constructeur de la classe Profile
     * @param pseudo Pseudonyme du profil
     */
    public Profile(String pseudo) {
        this.pseudo = pseudo;
    }

    /* ====== Getter & Setter ====== */

    /**
     * Getter: Recuperation du pseudonyme
     * @return Pseudonyme du profil
     */
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /* ====== Methodes d'instance ====== */

    @Override
    public String toString() {
        return "<Profile: " + getPseudo() + ">";
    }

    /***
     * Permet de sauvegarder un profil dans la base de donnees.
     * Avant d'appeler cette methode, la base de donnees doit d'abord etre initialise.
     * @throws SQLException Lève une exception en cas d'erreur de connexion
     * @see DBManager
     */
    public void save() throws SQLException{
        DBManager.saveProfile(this);
    }

}
