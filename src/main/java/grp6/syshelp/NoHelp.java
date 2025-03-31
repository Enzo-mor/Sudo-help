package grp6.syshelp;

/**
 * Classe qui represente le faite de ne pas avoir d'aide
 * @author POUSSE Kilian
 * @see Help
 * @see SysHelp
 */
public class NoHelp extends Help {

    /**
     * Constructeur de la Non-aide
     */
    public NoHelp() {
        super(NoHelp.class.getSimpleName());
        setMessage(1, "Aucune aide a été trouvé !");
        setMessage(2, "Aucune aide a été trouvé !");
        setMessage(3, "Aucune aide a été trouvé !");
    }
    
}
