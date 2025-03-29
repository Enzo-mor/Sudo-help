package grp6.sudocore;

/**
 * Classe qui représente les logs pour Sudo-help.
 * Elle permet de générer des logs avec différents niveaux de priorité (LOG, WARNING, ERREUR, DEBUG).
 * 
 * @author Kilian POUSSE
 */
public class SudoLog {

    /** 
     * Réinitialise la couleur du texte au style par défaut. 
     */
    protected static final String COLOR_RESET = "\u001B[0m";

    /** 
     * Texte en noir. 
     */
    protected static final String COLOR_BLACK = "\u001B[30m";

    /** 
     * Texte en rouge. 
     */
    protected static final String COLOR_RED = "\u001B[31m";

    /** 
     * Texte en vert. 
     */
    protected static final String COLOR_GREEN = "\u001B[32m";

    /** 
     * Texte en jaune. 
     */
    protected static final String COLOR_YELLOW = "\u001B[33m";

    /** 
     * Texte en bleu. 
     */
    protected static final String COLOR_BLUE = "\u001B[34m";

    /** 
     * Texte en violet. 
     */
    protected static final String COLOR_PURPLE = "\u001B[35m";

    /** 
     * Texte en cyan. 
     */
    protected static final String COLOR_CYAN = "\u001B[36m";

    /** 
     * Texte en blanc. 
     */
    protected static final String COLOR_WHITE = "\u001B[37m";

    /** 
     * Texte en gris clair. 
     */
    protected static final String COLOR_GRAY_LIGHT = "\u001B[90m"; 

    /** 
     * Texte en gris moyen (identique à blanc standard). 
     */
    protected static final String COLOR_GRAY_MEDIUM = "\u001B[37m";

    /** 
     * Permet de savoir si il faut affichier les messages de debug 
     */
    private static boolean debug = false;
    
    /**
     * Méthode privée pour écrire un log dans le terminal avec des informations détaillées sur l'appelant.
     * 
     * @param title Titre du log (par exemple, "LOG", "WARNING", "ERREUR", "DEBUG").
     * @param color Couleur du titre du log (par exemple, `COLOR_BLUE`, `COLOR_YELLOW`).
     * @param msg Message du log à afficher dans le terminal.
     */
    private static void write(String title, String color, String msg) {
        // Récupérer la classe et la méthode appelante à l'aide de StackWalker
        String pckg = StackWalker.getInstance()
                .walk(frames -> frames.skip(2).findFirst().get().getClassName());
        String method = StackWalker.getInstance()
                .walk(frames -> frames.skip(2).findFirst().get().getMethodName());

        // Afficher le log formaté
        System.out.println(COLOR_RESET + "[" + color + title + COLOR_RESET + "]" 
                + "[" + COLOR_GRAY_LIGHT + pckg + "." + method + COLOR_RESET + "]: " + msg);
    }

    /**
     * Écrit un log dans le terminal avec le niveau "LOG".
     * 
     * @param msg Message du log.
     */
    public static void log(String msg) {
        write("INFO", COLOR_BLUE, msg);
    }

    /**
     * Écrit un log dans le terminal avec le niveau "WARNING".
     * 
     * @param msg Message du log.
     */
    public static void warning(String msg) {
        write("WARNING", COLOR_YELLOW, msg);
    }

    /**
     * Écrit un log dans le terminal avec le niveau "ERROR".
     * 
     * @param msg Message du log.
     */
    public static void error(String msg) {
        write("ERROR", COLOR_RED, msg);
    }

    /**
     * Écrit un log dans le terminal avec le niveau "DEBUG".
     * 
     * @param msg Message du log.
     */
    public static void debug(String msg) {
        if(SudoLog.debug)
            write("DEBUG", COLOR_GRAY_LIGHT, msg);
    }

    /**
     * Setter: Affectation du booleen qui indique si il faut afficher
     * les logs de type debug
     * @param debug booleen qui indique si il faut afficher les logs de type debug
     */
    public static void setDebug(boolean debug) {
        log("Activation de l'affichage des log de type 'DEBUG'");
        SudoLog.debug = debug;
    }

    /**
     * Getter: Recuperation du booleen qui indique si il faut afficher
     * les logs de type debug
     * @return booleen qui indique si il faut afficher les logs de type debug
     */
    public static boolean getDebug() {
        return SudoLog.debug;
    }
}
