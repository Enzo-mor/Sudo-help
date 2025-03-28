package grp6.sudocore;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import grp6.sudocore.SudoTypes.ActionType;

/**
 * Cette classe gere les actions d'annotation dans un jeu de sudoku.
 * Elle permet d'ajouter une annotation a une cellule flexible.
 * 
 * @author DE THESE Taise
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @see ActionCell
 * @see SudoTypes
 * @see AnnotationRemoveCellAction
 * @see NumberCellAction
 * @see ActionFactory
 * @see ActionManager
 * @see AnnotationCellAction
 * @see AnnotationRemoveCellAction
 * @see NumberCellAction
 * 
 */
public class AnnotationCellAction extends ActionCell {

    /**
     * Nombre a ajouter en annotation.
     */
    protected int annotation;

    /**
     * Valeur initiale de l'annotation de la cellule avant l'action.
     */
    private final int old_annotation;

    /**
     * Valeur de l'annotation pour l'action de "redo".
     */
    private final int redo_annotation;

    /**
     * Constructeur permettant de creer une action pour ajouter une annotation a une cellule specifique de la grille.
     * 
     * @param game Jeu contenant la grille a modifier.
     * @param x Coordonee X de la cellule.
     * @param y Coordonee Y de la cellule.
     * @param annotation Nouvelle annotation a ajouter.
     * @param old_annotation Valeur initiale de l'annotation avant l'ajout.
     */
    public AnnotationCellAction(Game game, int x, int y, int annotation, int old_annotation) {
        super(game, x, y);
        this.annotation = annotation;
        this.old_annotation = old_annotation;
        this.redo_annotation = annotation;
    }

    /**
     * Applique l'action en ajoutant l'annotation a la cellule.
     */
    @Override
    protected void doAction() {
        game.grid.getCell(x, y).addAnnotation(redo_annotation);
    }

    /**
     * Annule l'action d'annotation en retirant l'annotation ajoutee.
     * Si l'annotation est deja presente, elle sera supprimee, sinon l'action sera reappliquee.
     */
    @Override
    protected void undoAction() {
        if (game.grid.getCell(x, y).getAnnotations().contains(annotation)) {
            game.grid.getCell(x, y).removeAnnotation(annotation);
        } else {
            doAction();
        }
    }

    /**
     * Retourne le type d'action : AnnotationCellAction.
     * 
     * @return Le type d'action.
     */
    @Override
    public SudoTypes.ActionType actionType() {
        return ActionType.ANNOTATION_CELL_ACTION;
    }

    /**
     * Serialise l'action en un objet JSON.
     * 
     * @return L'objet JSON representant l'action.
     */
    @Override
    public JsonObject serialise() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", actionType().getDescription());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("number", annotation);
        jsonObject.addProperty("old_number", old_annotation);
        return jsonObject;
    }

    /**
     * Retourne une chaine representant l'action.
     * 
     * @return La chaine decrivant l'action.
     */
    @Override
    public String toString() {
        return "Ajout de l'annotation de valeur " + annotation + " a la position : x = " + x + " et y = " + y;
    }

    /**
     * Encode l'action en JSON.
     * 
     * @return La chaine JSON representant l'action.
     */
    @Override
    public String jsonEncode() {
        // Creation de l'adaptateur Gson pour serialiser l'action
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AnnotationCellAction.class, new AnnotationCellActionSerialiser())
                .create();
        return gson.toJson(this);
    }

    /**
     * Deserialise une action a partir d'une chaine JSON.
     * 
     * @param json Chaine JSON contenant les donnees a decoder.
     * @param game Le jeu auquel l'action sera associee.
     * @return L'action deserialisee.
     * @throws Exception Si le JSON est incorrect ou incompatible avec l'action.
     */
    public static AnnotationCellAction jsonDecode(String json, Game game) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AnnotationCellAction.class, new AnnotationCellActionDeserialiser(game))
                    .create();
            return gson.fromJson(json, AnnotationCellAction.class);
        } catch (JsonParseException e) {
            throw new JsonParseException("Erreur de deserialisation : " + e.getMessage(), e);
        }
    }

    /**
     * Adaptateur Gson pour serialiser l'action.
     */
    private class AnnotationCellActionSerialiser implements JsonSerializer<AnnotationCellAction> {

        /**
         * Serialise l'action en un objet JSON.
         * 
         * @param action Action a serialiser.
         * @param vartype Type de la variable.
         * @param context Contexte de serialisation.
         * @return L'objet JSON representant l'action.
         */
        @Override
        public JsonElement serialize(AnnotationCellAction action, Type vartype, JsonSerializationContext context) {
            return action.serialise();
        }
    }

    /**
     * Adaptateur Gson pour deserialiser l'action.
     */
    private static class AnnotationCellActionDeserialiser implements JsonDeserializer<AnnotationCellAction> {
        private final Game game;

        /**
         * Constructeur de la classe deserialiseur.
         * 
         * @param game Le jeu auquel l'action sera associee.
         */
        public AnnotationCellActionDeserialiser(Game game) {
            this.game = game;
        }

        /**
         * Deserialise une action a partir d'un element JSON.
         * 
         * @param jsonElement Element JSON a deserialiser.
         * @param vartype Type de la variable.
         * @param context Contexte de deserialisation.
         * @return L'action deserialisee.
         * @throws JsonParseException Si le JSON est mal forme ou incomplet.
         */
        @Override
        public AnnotationCellAction deserialize(JsonElement jsonElement, Type vartype, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("number") || !jsonObject.has("old_number") || !jsonObject.has("type")) {
                throw new JsonParseException("Le JSON ne contient pas tous les champs requis : 'x', 'y', 'number', 'old_number', 'type'");
            }
            return new AnnotationCellAction(
                    game,
                    jsonObject.get("x").getAsInt(),
                    jsonObject.get("y").getAsInt(),
                    jsonObject.get("number").getAsInt(),
                    jsonObject.get("old_number").getAsInt());
        }
    }

    /**
     * Retourne l'ancienne annotation de la cellule.
     * 
     * @return L'ancienne annotation de la cellule.
     */
    @Override
    public int getOldAnnotation() {
        return old_annotation;
    }

    /**
     * Retourne la valeur de l'annotation pour l'action de redo.
     * 
     * @return La valeur de l'annotation pour redo.
     */
    @Override
    public int getRedoAnnotation() {
        return redo_annotation;
    }

    /**
     * Retourne l'annotation actuelle de la cellule.
     * 
     * @return L'annotation actuelle de la cellule.
     */
    @Override
    public int getAnnotation() {
        return annotation;
    }
}
