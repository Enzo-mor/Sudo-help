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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import grp6.sudocore.SudoTypes.Difficulty;

/**
 * Classe: Permet l'initialisation et/ou la creation de la Base de donnees.
 * @author Kilian POUSSE 
 * @author Taise de these
 * 
 * <p>Exemple d'utilisation :</p>
 * <pre>{@code
 *      try {
 *         // Initialiser la base de donnees
 *         DBManager.init();
 *      } catch(SQLException e) {
 *          // Gestion des exceptions liees a l'initialisation
 *          System.err.println("Erreur : " + e.getMessage());
 *      }
 * 
 *      // Utilisation de la Base de Donnees
 *      // Exemple: Recuperation de la grille a l'identifiant n°3
 *      Grid grid = DBManager.getGrid(3);
 * }</pre>
 */
public final class DBManager {

    private static final String DATABASE_PATH = System.getProperty("user.home") + "/db_sudohelp.db"; // Stocke dans le dossier Home
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;
    private static Connection conn = null; // Connexion persistante

    /**
     * Initialise la base de donnees et execute le script SQL si necessaire.
     * @throws SQLException en cas d'erreur de connexion
     */
    public static void init() throws SQLException {
        File databaseFile = new File(DATABASE_PATH);

        System.out.println("Base de donnees SQLite : " + DATABASE_PATH);

        if(databaseFile.exists()) {
            System.out.println("La base de donnees existe deja, le script ne sera pas execute.");
        } else {
            try(Connection connection = DriverManager.getConnection(DATABASE_URL)) {
                System.out.println("Connexion a la base de donnees SQLite reussie.");
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
            throw new SQLException("Tentative d'acces a une base de donnees fermee.");
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
                System.out.println("Connexion SQLite fermee.");
            }
        } catch(SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Execute un script SQL pour initialiser une table
     * dans la Base de donnees.
     * @param connection La connexion SQLite
     * @param scriptName Nom du fichier de script SQL a executer
     * 
     * @throws SQLException leve une exception de type SQL en cas de probleme de connexion
     * @throws RuntimeException leve cette exception si le fichier du script SQL est introuvable
     */
    private static void executeSqlScript(Connection connection, String scriptName) throws SQLException, RuntimeException {
        String scriptPath = "/bdd/" + scriptName + ".sql";
        try(InputStream inputStream = DBManager.class.getResourceAsStream(scriptPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             Statement statement = connection.createStatement()) {

            if(inputStream == null) {
                throw new RuntimeException("Fichier SQL non trouve : " + scriptPath);
            }

            StringBuilder sql = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if(line.trim().endsWith(";")) { // Execute une commande complete
                    statement.execute(sql.toString());
                    sql.setLength(0); // Reinitialise le buffer
                }
            }
            System.out.println("Script SQL execute avec succes.");
        } catch(Exception e) {
            System.err.println("Erreur lors de l'execution du script SQL : " + e.getMessage());
        }
    }

    /**
     * Verifie si une table existe dans la Base de donnees.
     * @param tableName Nom de la table dans la Base de donnees
     * @return vrai si la table existe, faux sinon
     * 
     * @throws SQLException leve une exception en cas d'erreur de connexion
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
            System.err.println("Erreur lors de la verification de l'existence de la table : " + e.getMessage());
            return false;
        }
    }

    


    /**
     * Recupere une grille depuis la base de donnees en fonction de son ID.
     * @param id Identifiant de la grille
     * @return Grid l'objet Grid correspondant a l'ID
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
                        throw new IllegalArgumentException("Taille des donnees incorrecte pour la grille ID: " + id);
                    }

                    return new Grid(id, difficulty, data);
                } else {
                    throw new IllegalArgumentException("Aucune grille trouvee avec l'ID: " + id);
                }
            }
        } catch(SQLException e) {
            System.err.println("Erreur SQL lors de l'execution du script : " + e.getMessage());
            throw new RuntimeException("Erreur SQL lors du chargement de la grille avec l'ID: " + id, e);
        } catch(IllegalArgumentException e) {
            System.err.println("Argument illegal : " + e.getMessage());
            throw new RuntimeException("Argument illegal lors du chargement de la grille avec l'ID: " + id, e);
        }
    }

    /**
     * Cette methode retourne l'ID du dernier jeu.
     * @return ID du dernier jeu
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
                System.err.println("Erreur lors de la recuperation du dernier ID de partie : " + e.getMessage());
            }
            return 0;
        }
        catch(SQLException | RuntimeException e) {
            System.err.println("Erreur lors de la recuperation du dernier ID de partie : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'execution du script: " + e);
        }
    }

    /**
     * Recupere toutes les grilles sous forme de liste.
     * @return Liste comportant les grilles presentes dans la BdD
     * @throws RuntimeException leve une exception si les grilles n'ont pas ete chargees, par une erreur de connexion
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
                        System.err.println("Taille des donnees incorrecte pour la grille ID: " + id);
                        continue;
                    }

                    res.add(new Grid(id, difficulty, data));
                }
            }
        }
        catch(SQLException e) {
            System.err.println("Erreur lors de la recuperation des jeux : " + e.getMessage());
            throw new RuntimeException("Erreur lors du chargement des grilles: " + e);
        }

        return res;
    }

    /**
     * Recupere tous les profils sous forme de liste.
     * @return Liste comportant les profils presentes dans la BdD
     * @throws RuntimeException leve une exception si les profils n'ont pas ete charges, par une erreur de connexion
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
            throw new RuntimeException("Erreur lors du chargement des profils: " + e);
        }

        return res;
    }

    /**
     * Methode permettant de sauvegarder ou ajouter un profil dans la Base de Donnees.
     * Le nom du profil reste insensible a la casse.
     * @param profile Represente le profil a sauvegarder.
     * @throws SQLException leve une exception de type SQLException en cas d'erreur de connexion.
     *  Il faut donc l'executer dans un bloc try/catch.
     */
    public static void saveProfile(Profile profile) throws SQLException {

        if(!tableExists("profile")) {
            System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
            executeSqlScript(getConnection(), "profile");
        }

        if(!(DBManager.getProfiles()).stream()
            .anyMatch(p->p.getPseudo().equalsIgnoreCase(profile.getPseudo()))) {

            String query = "INSERT INTO profile(pseudo) VALUES(?)";
            Connection connexion = getConnection();
            PreparedStatement stmt = connexion.prepareStatement(query);
            stmt.setString(1, profile.getPseudo());
            stmt.executeUpdate();
        }
    }

    /**
     * Verifie si un profil existe dans la base de donnees.
     * @param profileName Le nom du profil a verifier.
     * @return true si le profil existe, sinon false.
     * @throws SQLException leve une exception de type SQLException en cas d'erreur de connexion.
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
                return rs.getInt(1) > 0; // Retourne true si au moins un profil est trouve
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la verification de l'existence du profil : " + e.getMessage());
        }
        return false;
    }

     

     /**
     * Recupere tous les jeux presents dans la base de donnees et les retourne sous forme de liste.
     * 
     * @return Une liste de jeux presentes dans la base de donnees.
     * @throws RuntimeException Si une erreur survient lors de la connexion ou de l'execution de la requete.
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
                    Game game = new Game(
                        rs.getLong("id_game"),
                        DBManager.getGrid(rs.getInt("grid")),
                        DBManager.getProfile(rs.getString("player")),
                        rs.getString("created_date"),
                        rs.getString("last_modifed_date"),
                        rs.getInt("score"),
                        rs.getLong("elapsed_time"),
                        rs.getString("game_state"),
                        rs.getDouble("progress_rate")
                    );
                    game.applyActions(ActionManagerApply.deserializeList(rs.getString("actions"), game));
                    res.add(game);
                }
            }
        } catch(SQLException e) {
            System.err.println("Erreur lors de la suppression des jeux pour le profil : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'execution du script: " + e);
        }

        return res;
    }

    /**
     * Retourne toutes les parties presentes dans la base de donnees appartenant a un profil.
     * 
     * @param pseudo Le pseudo du profil pour lequel recuperer les parties.
     * @return Une liste des parties appartenant au profil specifie.
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
            return new ArrayList<>();
        }
    }

    /**
     * Permet de supprimer un jeu dans la base de donnees en utilisant son ID.
     * 
     * @param id L'ID du jeu a supprimer.
     * @return true si le jeu a ete supprime, sinon false.
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
     */
    public static Boolean deleteGame(long id) throws SQLException {
        String query = "DELETE FROM game WHERE id_game = ?";
        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Permet de supprimer tous les jeux appartenant a un profil specifie dans la base de donnees.
     * 
     * @param pseudo Le pseudo du profil pour lequel supprimer les jeux.
     * @return true si les jeux ont ete supprimes, sinon false.
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
     */
    public static Boolean deleteAllGamesForProfile(String pseudo) throws SQLException {
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
     * Permet de supprimer tous les jeux de la base de donnees.
     * 
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
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
     * Methode permettant de supprimer tous les profils de la base de donnees.
     * 
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
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
     * Methode permettant de supprimer un profil et tous les jeux qui lui sont associes.
     * 
     * @param pseudo Le pseudo du profil a supprimer.
     * @return true si le profil a ete supprime, sinon false.
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
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
    }
    /* 
     * 
     * @param game Le jeu a sauvegarder.
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
     */
    protected static void saveGame(Game game) throws SQLException {

        if(!tableExists("game")) {
            System.err.println("La table 'profile' n'existe pas. Initialisation en cours...");
            executeSqlScript(getConnection(), "game");
        }
        if(!DBManager.profileExists(game.getProfile().getPseudo()))
            game.getProfile().save();

        String sql = "INSERT INTO game (id_game, grid, player, created_date, last_modifed_date, progress_rate, score, actions, elapsed_time, game_state) " +
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
            pstmt.setLong(9, game.getElapsedTime());
            pstmt.setString(10, game.getGameState().getName());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode permet de retourner le profil present dans la base de donnees a travers son pseudo.
     * 
     * @param pseudo Le pseudo du profil a rechercher.
     * @return Le profil correspondant au pseudo.
     * @throws SQLException Si une erreur survient lors de la connexion ou de l'execution de la requete.
     * @throws NoSuchElementException Si aucun profil n'a ete trouve avec le pseudo donne.
     */
    public static Profile getProfile(String pseudo) throws SQLException, NoSuchElementException {
        return DBManager.getProfiles().stream()
                .filter(p -> p.getPseudo().equalsIgnoreCase(pseudo))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Aucun profil n'a ete trouve avec le pseudo : " + pseudo));
    }

    /**
     * Recupere le nombre de grilles correspondant a une difficulte donnee.
     * 
     * @param difficulty La difficulte des grilles a rechercher.
     * @return Le nombre de grilles correspondant a la difficulte donnee.
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
     * Recupere le nombre de grilles correspondant a une difficulte donnee.
     * 
     * @param difficulty La difficulte des grilles a rechercher.
     * @return Le nombre de grilles correspondant a la difficulte donnee.
     */
    public static int getGridSizeWithDifficulty(SudoTypes.Difficulty difficulty) {
        return (int) DBManager.getGrids().stream()
                .filter(g -> g.getDifficulty().equals(difficulty))
                .count();
    }

    /**
     * Retourne le nombre total de grilles dans la base de donnees.
     * 
     * @return Le nombre total de grilles dans la base de donnees.
     */
    public static int getGridsSize() {
        return (int) DBManager.getGrids().stream()
                .count();
    }

    /**
     * Renomme un profil avec un nouveau nom.
     * 
     * @param profile Le profil a renommer.
     * @param name Le nouveau nom du profil.
     * @return true si le profil a ete renomme avec succes, sinon false.
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
                // Met a jour l'objet apres succes
                profile.setPseudo(name); 
                return true;
            }
            return false;
            
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retourne les 5 meilleurs joueurs pour une grille donnee.
     * 
     * @param gridId L'ID de la grille pour laquelle on veut les meilleurs joueurs.
     * @return Une liste des 5 meilleurs joueurs et leurs scores.
     */
    public static List<Player> getTop5PlayersForGrid(int gridId) {
        List<Player> topPlayers = new ArrayList<>();
        String query = "SELECT player, score FROM game WHERE grid = ? ORDER BY score DESC LIMIT 5";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, gridId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                topPlayers.add(new Player(rs.getString("player"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topPlayers;
    }

    /**
     * Retourne les grilles d'une difficulte donnee avec leur ID et nom.
     * 
     * @param difficulty La difficulte des grilles a rechercher ("facile", "moyen", "difficile").
     * @return Une map contenant l'ID et le nom des grilles correspondant a la difficulte donnee.
     */
    public static Map<Integer, String> getGridsByDifficulty(String difficulty) {
        Map<Integer, String> grids = new LinkedHashMap<>();
        
        String prefix;
        switch (difficulty.toLowerCase()) {
            case "facile":
                prefix = "F";
                break;
            case "moyen":
                prefix = "M";
                break;
            case "difficile":
                prefix = "D";
                break;
            default:
                prefix = "?"; 
        }

        // Requete SQL pour recuperer les grilles triees par ID croissant
        String query = "SELECT id_grid FROM grid WHERE difficulty = ? ORDER BY id_grid ASC";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, difficulty);
            ResultSet rs = pstmt.executeQuery();
            
            int num = 1; 
            while (rs.next()) {
                int id = rs.getInt("id_grid");
                String name = "Sudoku " + prefix + "-" + num; 
                grids.put(id, name);
                num++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (grids.isEmpty()) {
            System.out.println("Aucune grille trouvee pour la difficulte : " + difficulty);
        }

        return grids;
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