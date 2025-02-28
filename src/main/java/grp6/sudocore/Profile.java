package grp6.sudocore;

import java.sql.SQLException;

/**
 * Classe représentant les profiles.
 * @author Kilian POUSSE
 * @author Taise de Thèse
 */
public class Profile {
    
    /* ====== Variables d'instance ====== */

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
  /***
   *  cette methode permet de sauvegarder un profile dans la base de données
   *  avant d'utliser cette methode la base de donnée devra d'abord etre initialiser
   * @throws SQLException leve une exeception en cas d'erreur de connection
   * @see DBManager
   */
    public void save() throws SQLException{
        DBManager.saveProfile(this);
    }

}
