package grp6.sudocore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * Cette classe permet de gerer la serialisation et la deserialisation d'une liste d'actions.
 * 
 * @author NGANGA YABIE Taïse de These
 * @see Action
 * @see Game
 */
public class ActionManagerApply {

    /**
     * Cette methode permet de serialiser une liste d'actions.
     * 
     * @param actions La liste des actions à sérialiser.
     * @return Une chaîne de caractères au format JSON contenant les actions sérialisées.
     */
    public static String serializeList(List<Action> actions) {
        return new GsonBuilder()
               .registerTypeHierarchyAdapter(Action.class, new ActionManagerserialiser())
               .create()
               .toJson(actions);
    }

    /**
     * Cette methode permet de deserialiser une liste d'actions à partir d'une chaîne JSON.
     * 
     * @param json La chaîne JSON contenant les actions serialisees.
     * @param game Le jeu sur lequel les actions seront appliquees.
     * @return Une liste d'actions deserialisee.
     */
    public static List<Action> deserializeList(String json, Game game) {
        Type listType = new TypeToken<List<Action>>() {}.getType();
        return new GsonBuilder()
               .registerTypeAdapter(Action.class, new ActionManagerDeserialiser(game))
               .create()
               .fromJson(json, listType);
    }

    /**
     * Gestion de la deserialisation des objets Action.
     */
    private static class ActionManagerDeserialiser implements JsonDeserializer<Action> {
        private final Game game;

        /**
         * Constructeur de la classe deserialiseur.
         * 
         * @param game Le jeu sur lequel les actions seront appliquees.
         */
        public ActionManagerDeserialiser(Game game) {
            this.game = game;
        }

        /**
         * Deserialise un objet JSON en une instance de {@link Action}.
         *
         * @param json    L'element JSON à deserialiser.
         * @param typeOfT Le type de l'objet attendu (ignore dans cette implementation).
         * @param context Le contexte de deserialisation (non utilise ici).
         * @return Une instance de {@link Action} correspondant aux donnees JSON.
         * @throws JsonParseException Si une erreur survient lors de la deserialisation.
         */
        @Override
        public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                Class<? extends Action> subAction = ActionFactory.getClassFromType(json.getAsJsonObject().get("type").getAsString());
                java.lang.reflect.Method method = subAction.getMethod("jsonDecode", String.class, Game.class);
                return (Action) method.invoke(subAction, json.toString(), game);
            } catch (JsonParseException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                System.err.println("Erreur lors de la désérialisation : " + e.getMessage());
                throw new JsonParseException("Échec lors de la désérialisation.", e);
            }
        }
    }

    /**
     * Serialisation des objets Action.
     */
    private static class ActionManagerserialiser implements JsonSerializer<Action> {

        @Override
        public JsonElement serialize(Action action, Type vartype, JsonSerializationContext context) {
            return action.serialise();
        }
    }

    /**
     * Point d'entree du programme. Cette methode teste la serialisation et la deserialisation 
     * d'actions en JSON, puis tente d'executer les actions deserialisees.
     *
     * @param args Arguments de la ligne de commande (non utilises).
     */
    public static void main(String[] args) {
        try {
            // Création de quelques actions pour tester
            Action action1 = new AnnotationCellAction(null, 1, 1, 5, 0);
            Action action2 = new NumberCellAction(null, 2, 2, 8, 3);

            // Ajout des actions à une liste
            List<Action> actions = new ArrayList<>();
            actions.add(action1);
            actions.add(action2);
            System.out.println(action1.actionType());

            // Sérialisation de la liste d'actions
            String json = ActionManagerApply.serializeList(actions);
            System.out.println("Liste d'actions sérialisée : " + json);

            // Désérialisation de la liste d'actions
            List<Action> deserializedActions = ActionManagerApply.deserializeList(json, new Game(DBManager.getGrid(2), new Profile("jean")));
            System.out.println("Liste d'actions désérialisée : " + deserializedActions);

            /* Exécution des actions désérialisées
            for (Action action : deserializedActions) {
                action.doAction();
                System.out.println("Action exécutée : " + action);
            }*/
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désérialisation : " + e.getMessage());
        }
    }
}
