package grp6.sudocore;

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
import java.util.NoSuchElementException;

import grp6.sudocore.SudoTypes.Difficulty;

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
public final class DBManager {

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
                executeSqlScript(connection, "game");
            }
        }
    }

    /**
     * Retourne une connexion SQLite persistante.
     * @return Connection SQLite
     * @throws SQLException en cas d'erreur de connexion
     */
    private static Connection getConnection() throws SQLException {
        conn = DriverManager.getConnection(DATABASE_URL);
        if(conn == null || conn.isClosed()) {
            throw new SQLException("Tentative d'accès à une base de données fermée.");
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
     * 
     * @throws SQLException leve une exeception de type sql en cas de probleme de connection 
     * @throws RuntimeException leve cette execption si le ficher est sql scriptName est introuvable 
     */
    private static void executeSqlScript(Connection connection, String scriptName) throws SQLException, RuntimeException {
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
     * 
     * @throws SQLException leve l'exeption en cas d'erreur de connection
     */
    private static boolean tableExists(String tableName) throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
    
        try(Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
    
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
            try(Connection connection = getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
    
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
    
                if(rs.next()) {
                    Difficulty difficulty = Difficulty.fromDescription(rs.getString("difficulty"));
                    String data = rs.getString("cells");
    
                    if(data.length() != Grid.NB_NUM * Grid.NB_NUM) {
                        throw new IllegalArgumentException("Taille des données incorrecte pour la grille ID: " + id);
                    }
    
                    return new Grid(id, difficulty, data);
                } else {
                    throw new IllegalArgumentException("Aucune grille trouvée avec l'ID: " + id);
                }
            }
        } catch(SQLException e) {
            System.err.println("Erreur SQL lors de l'exécution du script : " + e.getMessage());
            throw new RuntimeException("Erreur SQL lors du chargement de la grille avec l'ID: " + id, e);
        } catch(IllegalArgumentException e) {
            System.err.println("Argument illégal : " + e.getMessage());
            throw new RuntimeException("Argument illégal lors du chargement de la grille avec l'ID: " + id, e);
        }
    }

   /**
    * cette methode retourne l'id du dernier jeu
    * @return
    * @throws SQLException leve une exception de type SQLException
    */
    protected static int getLastIdGame() throws SQLException {
        
        try {
            if(!tableExists("game")) {
                System.err.println("La table 'game' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "game");
            }

            String query = "SELECT COALESCE(MAX(id_game), 0) FROM game";
            try(Connection connexion = getConnection();
                PreparedStatement pstmt = connexion.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();
                if(rs.next())
                return rs.getInt(1);
            } catch(SQLException e) {
                System.err.println("Erreur lors de la récupération du dernier ID de partie : " + e.getMessage());
            }
            return 0;
            }
        catch(SQLException | RuntimeException e) {
            System.err.println("Erreur lors de la récupération du dernier ID de partie : " + e.getMessage());
            throw new RuntimeException("Erreur lors du l'execution du script: " + e);
        }
    }

    
    /**
     * Récupérer toutes les grilles sous forme de liste
     * @return Liste comportant les grilles présentes dans la BdD
     * @throws RuntimeException leve une execeptions si les grilles n'ont pas été chargé du par 
     *  par une erreur de connexion
     */
    public static List<Grid> getGrids() throws RuntimeException {
        List<Grid> res = new ArrayList<>();

        try {
            if(!tableExists("grid")) {
                System.out.println("La table 'grid' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "grid");
            }
    
            String query = "SELECT * FROM grid";
            try(Connection connexion = getConnection();
                 PreparedStatement pstmt = connexion.prepareStatement(query)) {
    
                ResultSet rs = pstmt.executeQuery();
    
                while(rs.next()) {
                    Integer id = rs.getInt("id_grid");
                    Difficulty difficulty = Difficulty.fromDescription(rs.getString("difficulty"));
                    String data = rs.getString("cells");
    
                    if(data.length() != Grid.NB_NUM * Grid.NB_NUM) {
                        System.err.println("Taille des données incorrecte pour la grille ID: " + id);
                        continue;
                    }
    
                    res.add(new Grid(id, difficulty, data));
                }
            }
        }
        catch(SQLException e) {
            System.err.println("Erreur lors de la récupération des jeux : " + e.getMessage());
            throw new RuntimeException("Erreur lors du chargement des grilles: " + e);
        }


        return res;
    }
    
    /**
     * Récupérer tous les profiles sous forme de liste
     * @return Liste comportant les profiles présentes dans la BdD
     * @throws RuntimeException leve une execeptions si les profiles n'ont pas été chargé du par 
     *  par une erreur de connexion
     * 
     */
    public static List<Profile> getProfiles() throws RuntimeException {
        List<Profile> res = new ArrayList<>();

        try {
            if(!tableExists("profile")) {
                System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "profile");
            }
    
            String query = "SELECT * FROM profile";
            try(Connection connexion = getConnection();
                 PreparedStatement pstmt = connexion.prepareStatement(query)) {
    
                ResultSet rs = pstmt.executeQuery();
    
                while(rs.next()) {
                    String pseudo = rs.getString("pseudo");
    
                    res.add(new Profile(pseudo));
                }
            }
        }
        catch(SQLException e) {
            System.err.println("Erreur lors de la suppression des jeux pour le profil : " + e.getMessage());
            throw new RuntimeException("Erreur lors du chargement des profiles: " + e);
        }


        return res;
    }
    
    /**
     *  methode permettant de sauvegarder ou ajouter  un profil dans la Base de Données
     *  le nom du profile reste insemsible à la casse
     * @param profile represente le profile à sauvegarder
     * @throws SQLException leve une execption de type SqlExecption en cas d'erreur de connection.
     *  donc if faut l'executer dans un bloc try/cacht
     */
    public static void saveProfile(Profile profile) throws SQLException{

        if(!tableExists("profile")) {
            System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
            executeSqlScript(getConnection(), "profile");
         }

     if(!(DBManager.getProfiles()).stream()
         .anyMatch(p->p.getPseudo().equalsIgnoreCase(profile.getPseudo()))){

      String query = "INSERT INTO profile(pseudo) VALUES(?)";
       Connection connexion = getConnection();
       PreparedStatement stmt =connexion.prepareStatement(query);
        stmt.setString(1, profile.getPseudo());
        stmt.executeUpdate();

     }
    }

    /**
     * Vérifie si un profil existe dans la base de données.
     * @param profileName Le nom du profil à vérifier.
     * @return true si le profil existe, sinon false.
     * @throws SQLException leve une execption de type SqlExecption en cas d'erreur de connection.
     */
    public static boolean profileExists(String profileName) throws SQLException {

        if(!tableExists("profile")) {
            System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
            executeSqlScript(getConnection(), "profile");
         }

        String query = "SELECT COUNT(*) FROM profile WHERE LOWER(pseudo) = LOWER(?)";

        try (Connection connexion = getConnection();
             PreparedStatement pstmt = connexion.prepareStatement(query)) {

            pstmt.setString(1, profileName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si au moins un profil est trouvé
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence du profil : " + e.getMessage());
        }
        return false; 
    }
     

     /**
     * Récupérer tous les jeux present sous forme de liste
     * @return Liste comportant les jeux présentes dans la BdD
     * 
     * @throws Runtimexception leve une execption en cas d'erreur de connection.
     */
    public static List<Game> getGames() throws RuntimeException {
        List<Game> res = new ArrayList<>();

        try {
            if(!tableExists("game")) {
                System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
                executeSqlScript(getConnection(), "game");
            }
    
            String query = "SELECT * FROM game";
            try(Connection connexion = getConnection();
                 PreparedStatement pstmt = connexion.prepareStatement(query)) {
    
                ResultSet rs = pstmt.executeQuery();
    
                while(rs.next()) {
                    Game game=new Game(
                        rs.getLong("id_game")
                      , DBManager.getGrid(rs.getInt("grid"))
                      , DBManager.getProfile(rs.getString("player"))
                      , rs.getString("created_date")
                      , rs.getString("last_modifed_date")
                      , rs.getInt("score")
                      , rs.getLong("elapsed_time")
                      , rs.getString("game_state")
                      , rs.getDouble("progress_rate")
                      );
                     game.applyActions(ActionManagerApply.deserializeList(rs.getString("actions"), game));
                    res.add(game );
                }
            }
        }
        catch(SQLException e) {
            System.err.println("Erreur lors de la suppression des jeux pour le profil : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'execution du script: " + e);
        }


        return res;
    }
   
    /***
     * cette retourne toutes les parties present dans la DB appartant à un profil
     * @param pseudo : represnte le profil
     * @return
     */
    public static List<Game> getGamesForProfile(String pseudo) {
        try {
             List<Game> games = new ArrayList<>();
            if (DBManager.profileExists(pseudo)) {
                games = DBManager.getGames().stream()
                        .filter(game -> game.getProfile() != null && game.getProfile().getPseudo().equalsIgnoreCase(pseudo))
                        .toList();
            }
            return games;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des jeux pour le profil : " + e.getMessage());
            return new ArrayList<Game>();
        }
    }
    
    /**
     *  methode permettant de supprimer un jeu dans la base de donnée
     * @param id represente id du jeu
     * @return vrai si le jeu a été supprimé
     * @throws SQLException leve une exception en cas d'erreur de connection
     */
    public static Boolean deleteGame(long id) throws SQLException {
        String query = "DELETE FROM game WHERE id_game = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * methode permettant de supprimer tous les jeux appartenant à un profile
     * @param pseudo represente le pseudo du profile
     * @return vrai si les jeux ont été supprimé
     * @throws SQLException leve une exception en cas d'erreur de connection
     */
    public static Boolean deleteAllGamesForProfile(String pseudo) throws SQLException{
        String query = "DELETE FROM game WHERE player = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, pseudo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * methode permettant de supprimer tous les jeux
     * @throws SQLException leve une exception en cas d'erreur de connection
     */
    public static void deleteAllGames() throws SQLException {
        String query = "DELETE FROM game";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   /**
    * methode permettant de supprimer tous les profiles
    * @throws SQLException leve une exception en cas d'erreur de connection
    */
    public static void deleteAllProfiles() throws SQLException {
        String query = "DELETE FROM profile";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * methode permettant de supprimer un profile et tous les jeux qui lui sont associés
     * @param pseudo represente le pseudo du profile
     * @return vrai si le profile a été supprimé
     * @throws SQLException leve une exception en cas d'erreur de connection
     */
    public static boolean deleteProfile(String pseudo) throws SQLException {

        deleteAllGamesForProfile(pseudo);
        String query = "DELETE FROM profile WHERE pseudo = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, pseudo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Supprimmer les techniques sauvegardées liées au profile
        // TODOTODOTODOTODOTODOTODOTODO
    }
/**
 * cette methode permet de sauvegarder un jeu
 * @param game
 * @throws SQLException leve une execption en cas d'erreur de connection
 */
protected static void saveGame(Game game) throws SQLException{

    if(!tableExists("game")) {
        System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
        executeSqlScript(getConnection(), "game");
    }
    if(!DBManager.profileExists(game.getProfile().getPseudo()))
    game.getProfile().save();

        String sql = "INSERT INTO game (id_game, grid,player,created_date, last_modifed_date,progress_rate,score,actions,elapsed_time, game_state) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT(id_game) DO UPDATE SET " +
                    "player = excluded.player, " +
                    "last_modifed_date = excluded.last_modifed_date, " +
                    "progress_rate = excluded.progress_rate, " +
                    "score = excluded.score, " +
                    "actions = excluded.actions, " +  
                    "elapsed_time = excluded.elapsed_time, " +  
                    "game_state = excluded.game_state";
            try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, game.getId());
            pstmt.setInt(2, game.getGrid().getId());
            pstmt.setString(3, game.getProfile().getPseudo());
            pstmt.setString(4, game.getCreatedDate());
            pstmt.setString(5, game.getLastModifDate());
            pstmt.setDouble(6, game.getProgressRate());
            pstmt.setInt(7, game.getScore());
            pstmt.setString(8, game.JsonEncodeActionsGame());
            pstmt.setLong(9,game.getElapsedTime());
            pstmt.setString(10, game.getGameState().getName());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            }

}

       
/**
 * cette methode permet de retourner le profile present dans la base de données à travers son pseudo
 * @param pseudo
 * @return  profile
 * @throws SQLException leve une exception en cas d'erreur de connection
 * @throws NoSuchElementException leve une exception si aucun profile contenant ce pseudo n'a été trouvé dans la base de donnée
 */
public static Profile getProfile(String pseudo) throws SQLException ,NoSuchElementException{
    return DBManager.getProfiles().stream()
    .filter(p->p.getPseudo().equalsIgnoreCase(pseudo))
    .findFirst()
    .orElseThrow(() -> new NoSuchElementException("Aucun profil n'a été trouvé avec le pseudo : " + pseudo));
}


/**
 * Récupérer le nombre de grilles correspondant à une difficulté donnée.
 * @param difficulty Difficulté donnée
 * @return Le nombre de grilles
 */
public static int getGridSizeWithDifficulty(String difficulty) {

    SudoTypes.Difficulty diff;

    if ("facile".equals(difficulty)) {
    diff = SudoTypes.Difficulty.EASY;
    }
    else if("difficile".equals(difficulty)) {
        diff = SudoTypes.Difficulty.HARD;
    }
    else {
        diff = SudoTypes.Difficulty.MEDIUM;
    }
    
    return DBManager.getGridSizeWithDifficulty(diff);
} 

/**
 * Récupérer le nombre de grilles correspondant à une difficulté donnée.
 * @param difficulty Difficulté donnée
 * @return Le nombre de grilles
 */
public static int getGridSizeWithDifficulty(SudoTypes.Difficulty difficulty) {
    return (int) DBManager.getGrids().stream()
            .filter(g -> g.getDifficulty().equals(difficulty))
            .count();
}

/**
 * Nombre de grille dans la bdd
 * @return nombre de grille
 */
public static int getGridsSize() {
    return (int) DBManager.getGrids().stream()
            .count();
}

/**
 * Renomme un profile 
 * @param profile Profile à renommer
 * @param name Nouveau nom du profile
 */
public static boolean renameProfile(Profile profile, String name) {
    try {
        if(DBManager.profileExists(name)) {
            return false;
        }
    } catch(Exception e) {
        System.err.println("renameProfile: " + e);
        return false;
    }

    System.out.println(profile.getPseudo() + " --> " + name);
    
    String query = "UPDATE profile SET pseudo = ? WHERE pseudo = ?";
    
    try(Connection conn = DBManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
         
        pstmt.setString(1, name);
        pstmt.setString(2, profile.getPseudo());

        int n = pstmt.executeUpdate();
        if(n > 0) {
            // Met à jour l'objet après succès
            profile.setPseudo(name); 
            return true;
        }
        return false;
        
    } catch(SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public static void main(String[] args) {
    try {
        DBManager.init();
       // DBManager.getGames();
        DBManager.getGamesForProfile("jaques").forEach(t->System.out.println(t.getHistoActions()));;
       // System.out.println(DBManager.getProfile("jea"));
        //DBManager.saveGame(new Game(DBManager.getGrid(2),new Profile("pierre")));
    } catch (Exception e) {
        System.err.println(e.getMessage());
        // TODO: handle exception

}
}
}