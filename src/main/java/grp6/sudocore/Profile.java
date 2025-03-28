package grp6.sudocore;

import java.sql.SQLException;
import grp6.syshelp.Technique;
import java.util.*;
import java.sql.*;

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
     * @throws SQLException Leve une exception en cas d'erreur de connexion
     * @see DBManager
     */
    public void save() throws SQLException{
        DBManager.saveProfile(this);
    }

    /**
     * Recuperation des techniques debloquees par un joueur
     * 
     * @return Liste des techniques debloquees par un joueur
     */
    public List<Technique> getUnlockedTechniques() {
        List<Technique> tech = new ArrayList<>();

        String query = "SELECT t.id_tech, t.name, t.short_desc, t.long_desc, t.cells, t.cells_final, t.annot_final " +
                   "FROM possedeTech p " +
                   "JOIN tech t ON p.id_tech = t.id_tech " +
                   "JOIN profile pr ON p.player = pr.id_profile " +
                   "WHERE pr.pseudo = ?;";
        try(Connection connexion = DBManager.getConnection();
            PreparedStatement pstmt = connexion.prepareStatement(query)) {

            pstmt.setString(1, this.pseudo); 
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id_tech");
                String name = rs.getString("name");
                String shortDesc = rs.getString("short_desc");
                String longDesc = rs.getString("long_desc");
                String data = rs.getString("cells");
                String cellsFinal = rs.getString("cells_final");
                String annot = rs.getString("annot_final");

                tech.add(new Technique(id, name, shortDesc, longDesc, data, cellsFinal, annot));
            }
        }
        catch(Exception e) {
            SudoLog.error(e.toString());
        }

        return tech;
    }

    /**
     * Ajoute une technique au profil en mettant a jour la base de donnees.
     * Si la technique existe deja, le compteur est incremente.
     * Sinon, une nouvelle entree est creee.
     * 
     * @param name Nom de la technique a ajouter.
     */
    public void addTech(String name) {
        String findQuery = "SELECT p.count FROM possedeTech p " +
                        "JOIN tech t ON p.id_tech = t.id_tech " +
                        "JOIN profile pr ON p.player = pr.id_profile " +
                        "WHERE pr.pseudo = ? AND t.name = ?;";

        String updateQuery = "UPDATE possedeTech SET count = count + 1 " +
                            "WHERE player = (SELECT id_profile FROM profile WHERE pseudo = ?) " +
                            "AND id_tech = (SELECT id_tech FROM tech WHERE name = ?);";

        String insertQuery = "INSERT INTO possedeTech (player, id_tech, count) " +
                            "VALUES ((SELECT id_profile FROM profile WHERE pseudo = ?), " +
                            "(SELECT id_tech FROM tech WHERE name = ?), 1);";

        try(Connection connexion = DBManager.getConnection();
            PreparedStatement findStmt = connexion.prepareStatement(findQuery);
            PreparedStatement updateStmt = connexion.prepareStatement(updateQuery);
            PreparedStatement insertStmt = connexion.prepareStatement(insertQuery)) {

            findStmt.setString(1, this.pseudo);
            findStmt.setString(2, name);
            ResultSet rs = findStmt.executeQuery();

            if(rs.next()) {
                // La technique existe deja = on met a jour 'count'
                updateStmt.setString(1, this.pseudo);
                updateStmt.setString(2, name);
                updateStmt.executeUpdate();
                SudoLog.debug("Ajout dans la bdd");
            }
            else {
                // La technique n'existe pas encore = on l'insere
                insertStmt.setString(1, this.pseudo);
                insertStmt.setString(2, name);
                insertStmt.executeUpdate();
                SudoLog.debug("Creer dans la bdd");
            }

        }
        catch(SQLException e) {
            SudoLog.error("Erreur SQL : " + e.getMessage());
        }
    }

    /**
     * Supprime toutes les techniques associees au profil dans la base de donnees.
     */
    public void removeAllTech() {
        String deleteQuery = "DELETE FROM possedeTech " +
                            "WHERE player = (SELECT id_profile FROM profile WHERE pseudo = ?);";

        try(Connection connexion = DBManager.getConnection();
            PreparedStatement pstmt = connexion.prepareStatement(deleteQuery)) {

            pstmt.setString(1, this.pseudo);
            int affectedRows = pstmt.executeUpdate();

            if(affectedRows > 0) {
                System.out.println("Toutes les techniques ont ete supprimees pour le profil: " + this.pseudo);
            }
            else {
                System.out.println("Aucune technique trouvee pour le profil: " + this.pseudo);
            }

        }
        catch(SQLException e) {
            SudoLog.error("Erreur SQL lors de la suppression des techniques : " + e.getMessage());
        }
    }

    /**
     * Verifie si une technique a deja ete apprise par le profil actuel.
     * 
     * @param tech La technique a verifier.
     * @return true si la technique a deja ete apprise (already = true), false sinon.
     */
    public boolean getAlreadyLearn(Technique tech) {
        String query = "SELECT already FROM possedeTech p " +
                    "JOIN profile pr ON p.player = pr.id_profile " +
                    "WHERE pr.pseudo = ? AND p.id_tech = ?;";
        
        try(Connection connexion = DBManager.getConnection();
            PreparedStatement pstmt = connexion.prepareStatement(query)) {
            
            pstmt.setString(1, this.pseudo);
            pstmt.setInt(2, tech.getId());

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getBoolean("already");
            }

        }
        catch(SQLException e) {
            SudoLog.error("Erreur SQL dans getAlreadyLearn : " + e.getMessage());
        }

        return false;
    }


}
