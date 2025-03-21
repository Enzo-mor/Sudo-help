package grp6.sudocore;

/**
 * Classe qui represent les log de Sudo-help
 * @author Kilian POUSSE
 */
public class SudoLog {

    public static final String COLOR_RESET = "\u001B[0m";
    public static final String COLOR_BLACK = "\u001B[30m";
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_GREEN = "\u001B[32m";
    public static final String COLOR_YELLOW = "\u001B[33m";
    public static final String COLOR_BLUE = "\u001B[34m";
    public static final String COLOR_PURPLE = "\u001B[35m";
    public static final String COLOR_CYAN = "\u001B[36m";
    public static final String COLOR_WHITE = "\u001B[37m";
    public static final String COLOR_GRAY_LIGHT = "\u001B[90m"; 
    public static final String COLOR_GRAY_MEDIUM = "\u001B[37m";
    
    /**
     * 
     * @param title
     * @param color
     * @param msg
     */
    private static void write(String title, String color, String msg) {
        String target = StackWalker.getInstance()
                .walk(frames -> frames.skip(2).findFirst().get().getClassName());

        System.out.println(COLOR_RESET+"["+color+title+COLOR_RESET+"]["+COLOR_GRAY_LIGHT+target+COLOR_RESET+"]: "+msg);
    }

    public static void log(String msg) {
        write("LOG", COLOR_BLUE, msg);
    }

    public static void warning(String msg) {
        write("WARNING", COLOR_YELLOW, msg);
    }

    public static void erreur(String msg) {
        write("ERREUR", COLOR_RED, msg);
    }

    public static void debug(String msg) {
        write("DEBUG", COLOR_GRAY_LIGHT, msg);
    }

}
