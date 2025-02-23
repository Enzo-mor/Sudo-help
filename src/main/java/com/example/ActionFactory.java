package com.example;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParseException;

/***
 * cette classe permet de stocker les classe qui implemente l'interface action.
 *  le but est de les recuperer dynamiquement en connaissant seulement le type(type action) des classes implementant l'interface Action
 * dans le but d'invoquer dynamiquent les methodes des ces classé implementant l'interface Action par le biais de la
 * reflexion
 * 
 * @author Taise de Thèse
 * @version 1.0
 */
public class ActionFactory {

    /***
     * variable contenant le type d'action 
     */
    private static final Map<String, Class<? extends Action>> actionMap = new HashMap<>();

    static {
        actionMap.put(SudoTypes.ActionType.ANNOTATION_CELL_ACTION.getDescription(), AnnotationCellAction.class);
        actionMap.put(SudoTypes.ActionType.NUMBER_CELL_ACTION.getDescription(), NumberCellAction.class);
    }

    /**Récupère dynamiquement la classe associée à un type donné
     * @param type represente le type ou la clé  permettant de recuperer la classe correspond
     * @return  la classe correspondant
     * */
    public static Class<? extends Action> getClassFromType(String type) throws JsonParseException {

        if(!actionMap.keySet().contains(type))
         throw new JsonParseException("le type :"+type+" n'a pas été trouvée ");
        return actionMap.get(type);
    }
}
