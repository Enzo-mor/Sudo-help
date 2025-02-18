package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe: Permet l'initialisation et/ou la création de la Base de données
 * @author Kilian POUSSE 
 * @author Taise de thèse
 * @version 1.0 
 * 
 * <p>Exemple d'utilisation :</p>
 * <pre>{@code
 *      try {
 *         // Initialiser la base de données
 *         DBManager.init();
 *      } catch(SQLException e) {
 *          // Gestion des execptions lié à l'initilisation
 *          System.err.println("Erreur : " + e.getMessage());
 *      }
 * 
 *      // Utilisation de la Base de Données
 *      // Exemple: Récupération de la grille à l'identifiant n°3
 *      Grid grid = DBManager.getGrid(3);
 * }</pre>
 */
public class DBManager {

    private static final String DATABASE_PATH = System.getProperty("user.home") + "/db_sudohelp.db"; // Stocké dans le dossier Home
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;
    private static Connection conn = null; // Connection persistante

    /**
     * Initialise la base de données et exécute le script SQL si nécessaire.
     * @throws SQLException en cas d'erreur de connexion
     */
    public static void init() throws SQLException {
        File databaseFile = new File(DATABASE_PATH);

        System.out.println("Base de données SQLite : " + DATABASE_PATH);

        if(databaseFile.exists()) {
            System.out.println("La base de données existe déjà, le script ne sera pas exécuté.");
        } else {
            try(Connection connection = DriverManager.getConnection(DATABASE_URL)) {
                System.out.println("Connexion à la base de données SQLite réussie.");
                executeSqlScript(connection, "grid");
                executeSqlScript(connection, "profile");
            }
        }
    }

    /**
     * Retourne une connexion SQLite persistante.
     * @return Connection SQLite
     * @throws SQLException en cas d'erreur de connexion
     */
    private static Connection getConnection() throws SQLException {
        if(conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DATABASE_URL);
        }
        return conn;
    }

    /**
     * Ferme proprement la connexion SQLite.
     */
    public static void close() {
        try {
            if(conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connexion SQLite fermée.");
            }
        } catch(SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Execute un scripte SQL pour initialiser une table
     * dans la BdD
     * @param connection
     * @param scriptName
     */
    private static void executeSqlScript(Connection connection, String scriptName) {
        String scriptPath = "/bdd/" + scriptName + ".sql";
        try(InputStream inputStream = DBManager.class.getResourceAsStream(scriptPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             Statement statement = connection.createStatement()) {

            if(inputStream == null) {
                throw new RuntimeException("Fichier SQL non trouvé : " + scriptPath);
            }

            StringBuilder sql = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if(line.trim().endsWith(";")) { // Exécute une commande complète
                    statement.execute(sql.toString());
                    sql.setLength(0); // Réinitialise le buffer
                }
            }
            System.out.println("Script SQL exécuté avec succès.");
        } catch(Exception e) {
            System.err.println("Erreur lors de l'exécution du script SQL : " + e.getMessage());
        }
    }

    /**
     * Verifie si une table existe dans la Base de Données
     * @param tableName Nom de la table dans la BdD
     * @return Vrai si la table existe, Faux sinon
     */
    private static boolean tableExists(String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
    
        try(Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de la table : " + e.getMessage());
            return false;
        }
    }
    


    /**
     * Récupère une grille depuis la base de données en fonction de son ID.
     * @param id Identifiant de la grille
     * @return Grid l'objet Grid correspondant à l'ID
     * @throws RuntimeException en cas d'erreur SQL ou si la grille est introuvable
     */
    public static Grid getGrid(int id) {
        try {
            if(!tableExists("grid")) {
                System.out.println("La table 'grid' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "grid");
            }
    
            String query = "SELECT * FROM grid WHERE id_grid = ?";
            try(Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
    
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
    
                if(rs.next()) {
                    String difficulty = rs.getString("difficulty");
                    String data = rs.getString("cells");
    
                    if(data.length() != Grid.NB_NUM * Grid.NB_NUM) {
                        throw new IllegalArgumentException("Taille des données incorrecte pour la grille ID: " + id);
                    }
    
                    return new Grid(id, difficulty, data);
                } else {
                    throw new IllegalArgumentException("Aucune grille trouvée avec l'ID: " + id);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la grille avec l'ID: " + id, e);
        }
    }
    
    /**
     * Récupérer toutes les grilles sous forme de liste
     * @return Liste comportant les grilles présentes dans la BdD
     */
    public static List<Grid> getGrids() {
        List<Grid> res = new ArrayList<>();

        try {
            if(!tableExists("grid")) {
                System.out.println("La table 'grid' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "grid");
            }
    
            String query = "SELECT * FROM grid";
            try(Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
    
                ResultSet rs = pstmt.executeQuery();
    
                while(rs.next()) {
                    Integer id = rs.getInt("id_grid");
                    String difficulty = rs.getString("difficulty");
                    String data = rs.getString("cells");
    
                    if(data.length() != Grid.NB_NUM * Grid.NB_NUM) {
                        System.err.println("Taille des données incorrecte pour la grille ID: " + id);
                        continue;
                    }
    
                    res.add(new Grid(id, difficulty, data));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement des grilles: " + e);
        }


        return res;
    }
    
    /**
     * Récupérer tous les profiles sous forme de liste
     * @return Liste comportant les profiles présentes dans la BdD
     */
    public static List<Profile> getProfiles() {
        List<Profile> res = new ArrayList<>();

        try {
            if(!tableExists("profile")) {
                System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "profile");
            }
    
            String query = "SELECT * FROM profile";
            try(Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
    
                ResultSet rs = pstmt.executeQuery();
    
                while(rs.next()) {
                    Integer id = rs.getInt("id_profile");
                    String pseudo = rs.getString("pseudo");
    
                    res.add(new Profile(id, pseudo));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement des profiles: " + e);
        }


        return res;
    }

    /**
     *  methode permettant de sauvegarder ou ajouter  un profil dans la Base de Données
     * @param profile reprsente le profile à sauvegarder
     * @throws Exception leve une execption de type SqlExecption ou autre.
     *  donc if faut l'executer dans un bloc try/catch
     */
    public static void saveProfile(Profile profile) throws Exception{

        if(!tableExists("profile")) {
            System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
            executeSqlScript(getConnection(), "profile");
         
    }

     if(DBManager.getProfiles().size()>0&&!(DBManager.getProfiles()).stream()
         .anyMatch(p->p.getPseudo().equalsIgnoreCase(profile.getPseudo()))){

      String query = "INSERT INTO profile(pseudo) VALUES(?)";
       Connection conn=getConnection();
       PreparedStatement stmt =conn.prepareStatement(query);
        stmt.setString(1, profile.getPseudo());
        stmt.executeUpdate();

     }
    
}
public static void main(String[] args) {
    try {
        DBManager.saveProfile(new Profile(0,"jean"));
        System.out.println(DBManager.getProfiles().stream().findFirst().get().getId());
    } catch (Exception e) {
        // TODO: handle exception
    }
    
}
}