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
 *      } catch (SQLException e) {
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
    private static final String SQL_SCRIPT_PATH = "/init.sql"; // Fichier dans resources
    private static Connection conn = null; // Connection persistante

    /**
     * Initialise la base de données et exécute le script SQL si nécessaire.
     * @throws SQLException en cas d'erreur de connexion
     */
    public static void init() throws SQLException {
        File databaseFile = new File(DATABASE_PATH);

        System.out.println("Base de données SQLite : " + DATABASE_PATH);

        if (databaseFile.exists()) {
            System.out.println("La base de données existe déjà, le script ne sera pas exécuté.");
        } else {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
                System.out.println("Connexion à la base de données SQLite réussie.");
                executeSqlScript(connection, SQL_SCRIPT_PATH);
            }
        }
    }

    /**
     * Retourne une connexion SQLite persistante.
     * @return Connection SQLite
     * @throws SQLException en cas d'erreur de connexion
     */
    private static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DATABASE_URL);
        }
        return conn;
    }

    /**
     * Ferme proprement la connexion SQLite.
     */
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connexion SQLite fermée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    private static void executeSqlScript(Connection connection, String scriptPath) {
        try (InputStream inputStream = DBManager.class.getResourceAsStream(scriptPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             Statement statement = connection.createStatement()) {

            if (inputStream == null) {
                throw new RuntimeException("Fichier SQL non trouvé : " + scriptPath);
            }

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if (line.trim().endsWith(";")) { // Exécute une commande complète
                    statement.execute(sql.toString());
                    sql.setLength(0); // Réinitialise le buffer
                }
            }
            System.out.println("Script SQL exécuté avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du script SQL : " + e.getMessage());
        }
    }


    /**
     * Récupère une grille depuis la base de données en fonction de son ID.
     * @param id Identifiant de la grille
     * @return Grid l'objet Grid correspondant à l'ID
     * @throws RuntimeException en cas d'erreur SQL ou si la grille est introuvable
     */
    public static Grid getGrid(int id) {
        String query = "SELECT * FROM grid WHERE id_grid = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String difficulty = rs.getString("difficulty");
                String data = rs.getString("cells");

                // Vérification des données
                if (data.length() != Grid.NB_NUM * Grid.NB_NUM) {
                    throw new IllegalArgumentException("Taille des données incorrecte pour la grille ID: " + id);
                }

                // Création de la grille
                Grid grid = new Grid(id, difficulty, data);
                return grid;
            } else {
                throw new IllegalArgumentException("Aucune grille trouvée avec l'ID: " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la grille avec l'ID: " + id, e);
        }
    }

    
}