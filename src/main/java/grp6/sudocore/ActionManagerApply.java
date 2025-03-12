package grp6.sudocore;

import java.lang.reflect.Type;
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
 * Cette classe permet de gerer la serialisation et la deserialisation d'une liste actions
 * 
 * @author Taise de Thèse
 * @version 1.0 
 */
public class ActionManagerApply {
    

    /**
     *  cette methode permet de serialiser une liste d'action
     * @param actions
     * @return une chaine sous forme de json obtenu contenant un ensemble d'actions en serialiser
     */
    public static String serializeList(List<Action> actions) {
        return new GsonBuilder()
               .registerTypeHierarchyAdapter(Action.class, new ActionManagerserialiser())
               .create()
               .toJson(actions);
    }
   
    /**
     * cette methode permet de deserialiser une liste d'action
     * @param json represente la chaine json contenant les actions serialiser
     * @param game represente le jeu  sur lequel les actions seront appliquées
     * @return une liste d'action deserialiser
     */
    public static List<Action> deserializeList(String json,Game game) {
        Type listType = new TypeToken<List<Action>>() {}.getType();

        return new GsonBuilder()
               .registerTypeAdapter(Action.class, new ActionManagerDeserialiser(game))
               .create()
               .fromJson(json, listType);
    }

    /**
    * Gestion  désérialisation des objets Action
    */
    private static class ActionManagerDeserialiser implements JsonDeserializer<Action> {
        private Game game;
        public ActionManagerDeserialiser(Game game){
        this.game=game;
        }
         @Override
        public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

             try {
                 Class <? extends Action> subAction=ActionFactory.getClassFromType(json.getAsJsonObject().get("type").getAsString());
                 java.lang.reflect.Method method=subAction.getMethod("jsonDecode", String.class,Game.class);
                 return (Action) method.invoke(subAction, json.toString(), game);
                 } catch (Exception e) {
                    e.printStackTrace();
                    throw new JsonParseException("echec lors de le deserialisation : ", e);
                }   
         }
    }
     private static  class ActionManagerserialiser implements JsonSerializer<Action> {


    public  JsonElement serialize(Action action, Type vartype, JsonSerializationContext context){
     return action.serialise();
    }
  }


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

        /* / Exécution des actions désérialisées
        for (Action action : deserializedActions) {
            action.doAction();
            System.out.println("Action exécutée : " + action);
        }*/
        } catch (Exception e) {
            // TODO: handle exception
        }

        
    }
}
