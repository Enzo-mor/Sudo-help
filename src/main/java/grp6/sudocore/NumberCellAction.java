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
 * Cette classe permet de gerer les actions de modification d'une cellule
 * dans un jeu de Sudoku. Elle permet de modifier la valeur d'une cellule.
 * 
 * @author NGANGA YABIE Taïse de These
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @see ActionCell
 * @see SudoTypes
 */
public final class NumberCellAction extends ActionCell {

    /**
     * Le nombre a ajouter en annotation.
     */
    private int number;

    /**
     * L'ancienne valeur de la cellule avant modification.
     */
    private final int old_number;

    /**
     * La valeur de la cellule pour la fonction de redo.
     */
    private final int redo_number;

    /**
     * Constructeur permettant de creer une action pour modifier la valeur d'une cellule
     * specifique dans la grille.
     * 
     * @param game Le jeu contenant la grille à modifier.
     * @param x Coordonnée X de la cellule.
     * @param y Coordonnée Y de la cellule.
     * @param number La nouvelle valeur à attribuer à la cellule.
     * @param old_number L'ancienne valeur présente à cette position.
     */
    public NumberCellAction(Game game, int x, int y, int number, int old_number) {
        super(game, x, y);
        this.number = number;
        this.old_number = old_number;
        this.redo_number = number;
    }

    /**
     * Retourne une chaîne representant l'action effectuée.
     * 
     * @return La chaîne decrivant l'action de modification de la cellule.
     */
    @Override
    public String toString() {
        return "Modification de la valeur de la cellule à la position : x = " + x + " et y = " + y 
                + " de " + old_number + " à " + number;
    }

    /**
     * Effectue l'action de modification de la cellule.
     */
    @Override
    protected void doAction() {
        game.grid.getCell(x, y).setNumber(redo_number);
    }

    /**
     * Annule l'action effectuee, remettant la cellule à son ancienne valeur.
     */
    @Override
    protected void undoAction() {
        game.grid.getCell(x, y).setNumber(old_number);
    }

    /**
     * Retourne le type de l'action (NUMBER_CELL_ACTION).
     * 
     * @return Le type de l'action.
     */
    @Override
    public SudoTypes.ActionType actionType() {
        return ActionType.NUMBER_CELL_ACTION;
    }

    /**
     * Serialise l'action NumberCellAction en un objet JSON.
     * 
     * @return Un objet JSON representant l'action.
     */
    public JsonObject serialise() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", actionType().getDescription());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("number", number);
        jsonObject.addProperty("old_number", old_number);
        return jsonObject;
    }

    /**
     * Encode l'action NumberCellAction en une chaine JSON.
     * 
     * @return Une chaine JSON représentant l'action.
     */
    @Override
    public String jsonEncode() {
        // Creation de l'adaptateur Gson permettant de lier la classe NumberCellAction
        // avec le serialiseur NumberCellActionSerialiser.
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NumberCellAction.class, new NumberCellActionSerialiser())
                .create();
        return gson.toJson(this);
    }

    /**
     * Permet de deserialiser une action a partir d'une chaine JSON.
     * 
     * @param json La chaine JSON a decoder.
     * @param game Le jeu dans lequel l'action doit être appliquee.
     * @return L'action deserialisee sous forme de NumberCellAction.
     * @throws Exception Si le JSON est incompatible avec l'action ou incorrect.
     */
    public static NumberCellAction jsonDecode(String json, Game game) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(NumberCellAction.class, new NumberCellActionDeserialiser(game))
                    .create();
            return gson.fromJson(json, NumberCellAction.class);
        } catch (JsonParseException e) {
            throw new JsonParseException("Erreur de désérialisation : " + e.getMessage(), e);
        }
    }

    /**
     * Serialise l'action NumberCellAction en un objet JSON.
     */
    private class NumberCellActionSerialiser implements JsonSerializer<NumberCellAction> {
        public JsonElement serialize(NumberCellAction action, Type vartype, JsonSerializationContext context) {
            return action.serialise();
        }
    }

    /**
     * Deserialise un objet JSON en une action NumberCellAction.
     */
    private static class NumberCellActionDeserialiser implements JsonDeserializer<NumberCellAction> {

        private Game game;

        /**
         * Constructeur de la classe.
         * 
         * @param game Le jeu contenant la grille pour l'action.
         */
        public NumberCellActionDeserialiser(Game game) {
            this.game = game;
        }

        /**
         * Deserialise l'element JSON en une action NumberCellAction.
         * 
         * @param jsonElement L'element JSON à désérialiser.
         * @param vartype Le type attendu pour la deserialisation.
         * @param context Le contexte de deserialisation.
         * @return L'action NumberCellAction correspondante.
         * @throws JsonParseException Si des erreurs sont rencontrees lors de la deserialisation.
         */
        public NumberCellAction deserialize(JsonElement jsonElement, Type vartype, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("number") || !jsonObject.has("old_number") || !jsonObject.has("type")) {
                throw new JsonParseException("Le JSON ne contient pas tous les champs requis : 'x', 'y', 'number', 'old_number', 'type'");
            }
            return new NumberCellAction(
                    game,
                    jsonObject.get("x").getAsInt(),
                    jsonObject.get("y").getAsInt(),
                    jsonObject.get("number").getAsInt(),
                    jsonObject.get("old_number").getAsInt());
        }
    }

    /**
     * Retourne l'ancienne valeur de la cellule.
     * 
     * @return L'ancienne valeur de la cellule.
     */
    @Override
    public int getOldNumber() {
        return old_number;
    }

    /**
     * Retourne la valeur de la cellule pour redo.
     * 
     * @return La valeur pour redo.
     */
    @Override
    public int getRedoNumber() {
        return redo_number;
    }

    /**
     * Retourne la nouvelle valeur de la cellule.
     * 
     * @return La nouvelle valeur de la cellule.
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Modifie la valeur de la cellule.
     * 
     * @param nb La nouvelle valeur a attribuer à la cellule.
     */
    @Override
    public void setNumber(int nb) {
        this.number = nb;
    }
}

