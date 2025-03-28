package grp6.sudocore;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParseException;

/**
 * Cette classe permet de stocker les classes qui implementent l'interface Action.
 * 
 * @author DE THESE Taise
 * @see Action
 * @see AnnotationCellAction
 * @see NumberCellAction
 * @see AnnotationRemoveCellAction
 */
public class ActionFactory {

    /***
     * Variable contenant le type d'action.
     */
    private static final Map<String, Class<? extends Action>> actionMap = new HashMap<>();

    static {
        actionMap.put(SudoTypes.ActionType.ANNOTATION_CELL_ACTION.getDescription(), AnnotationCellAction.class);
        actionMap.put(SudoTypes.ActionType.NUMBER_CELL_ACTION.getDescription(), NumberCellAction.class);
        actionMap.put(SudoTypes.ActionType.ANNOTATION_REMOVE_CELL_ACTION.getDescription(), AnnotationRemoveCellAction.class);
    }

    /***
     * Recupere dynamiquement la classe associee a un type donne.
     * 
     * @param type Le type ou la cle permettant de recuperer la classe correspondante.
     * @return La classe correspondant au type donne.
     * @throws JsonParseException Si le type n'a pas ete trouve.
     */
    public static Class<? extends Action> getClassFromType(String type) throws JsonParseException {

        if (!actionMap.containsKey(type)) {
            throw new JsonParseException("Le type : " + type + " n'a pas ete trouve.");
        }
        return actionMap.get(type);
    }
}
