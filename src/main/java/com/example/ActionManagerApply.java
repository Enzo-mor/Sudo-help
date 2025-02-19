package com.example;

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


public class ActionManagerApply {
    

    /**
     *  cette methode permet de serialiser une liste d'action
     * @param actions
     * @return une chaine sous forme de json obtenu en serialisant la liste
     */
    public static String serializeList(List<Action> actions) {
        return new GsonBuilder()
               .registerTypeHierarchyAdapter(Action.class, new ActionManagerserialiser())
               .setPrettyPrinting()
               .create()
               .toJson(actions);
    }


    public static List<Action> deserializeList(String json,Grid grid) {
        Type listType = new TypeToken<List<Action>>() {}.getType();

        return new GsonBuilder()
               .registerTypeAdapter(Action.class, new ActionManagerDeserialiser(grid))
               .create()
               .fromJson(json, listType);
    }

    /**
    * Gestion  désérialisation des objets Action
    */
    private static class ActionManagerDeserialiser implements JsonDeserializer<Action> {
        private Grid grid;
        public ActionManagerDeserialiser(Grid grid){
        this.grid=grid;
        }
         @Override
        public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

             try {
                 Class <? extends Action> subAction=ActionFactory.getClassFromType(json.getAsJsonObject().get("type").getAsString());
                 java.lang.reflect.Method method=subAction.getMethod("jsonDecode", String.class,Grid.class);
                 return (Action) method.invoke(subAction, json.toString(), grid);
                 } catch (Exception e) {
                    e.printStackTrace();
                    throw new JsonParseException("Failed to deserialize Action", e);
                }   
         }
    }
     private static  class ActionManagerserialiser implements JsonSerializer<Action> {


    public  JsonElement serialize(Action action, Type vartype, JsonSerializationContext context){
     return action.serialise();
    }
  }


    public static void main(String[] args) {

        // Création de quelques actions pour tester
        Action action1 = new AnnotationCellAction(null, 1, 1, 5);
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
        List<Action> deserializedActions = ActionManagerApply.deserializeList(json, null);
        System.out.println("Liste d'actions désérialisée : " + deserializedActions);

        /* / Exécution des actions désérialisées
        for (Action action : deserializedActions) {
            action.doAction();
            System.out.println("Action exécutée : " + action);
        }*/
    }
}
