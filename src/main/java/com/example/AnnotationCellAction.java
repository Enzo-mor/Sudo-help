package com.example;
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

/**
 * cette classe permet de gerer les actions d'annotation d'un jeu de sudoku
 * elle permet d'ajouter une annotation à une cellule
 */
public class AnnotationCellAction extends Action  {

    private int x, y;
    private int number;

    public AnnotationCellAction(Game game, int x, int y, int number) {
        super(game);
        this.x = x;
        this.y = y;
        this.number = number;
    }

    @Override
    protected void doAction() {
        this.game.getGrid().getMutableCell(x, y).addAnnotation(number);
    }

    @Override
    protected void undoAction() {
        this.game.getGrid().getMutableCell(x, number).removeAnnotation(number);
    }
    @Override
    public String actionType(){
        return "actionAnontation";
    }
    @Override
    public JsonObject serialise(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type",actionType());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("number", number);
        return jsonObject; 

    }
    /**
     * methode permettant de retourner la chaine representant l'action
     * @return
     */
    @Override
    public String toString(){
        return " ajout de l'annotation  au jeu de valeur : "+number+ " à la position : x = "+x+" et y = "+y;
    }
    @Override
    public String jsonEncode(){
        //creation de l'adaptateur Gson qui permet de matcher notre classe  NumberCellAction avec la classe NumberCellActionSerialiser 
        Gson gson=new GsonBuilder()
                  .registerTypeAdapter(AnnotationCellAction.class, new AnnotationCellActionSerialiser() )
                  .create();
        return gson.toJson(this);
    }
    /***
     * cette methode permet de déserialiser une action
     * 
     * @param  json represente la chaine contenant le json à decoder
     * @
     * @throws Exception leve une exception si le json est incompatible à l'action ou incorrect
     */
    public  static AnnotationCellAction jsonDecode(String json,Game game) throws Exception{
        try{
        Gson gson=new GsonBuilder()
        .registerTypeAdapter(AnnotationCellAction.class, new AnnotationCellActionDeserialiser(game) )
        .create();
        return gson.fromJson(json, AnnotationCellAction.class);
        }catch ( JsonParseException e) {
            throw new JsonParseException("Erreur de désérialisation : " + e.getMessage(), e);
        }
    }

    private class AnnotationCellActionSerialiser implements JsonSerializer<AnnotationCellAction>{
       private JsonObject obj;
        /**
         * methode de serialiser la l'action
         */
       public  JsonElement serialize(AnnotationCellAction action, Type vartype, JsonSerializationContext context){
        return action.serialise();
       }
    }

     private static class AnnotationCellActionDeserialiser implements JsonDeserializer<AnnotationCellAction> {
        private final Game game;
     public   AnnotationCellActionDeserialiser(Game game){
        this.game=game;
     }
    public AnnotationCellAction  deserialize(JsonElement jsonElement, Type vartype, JsonDeserializationContext context) throws JsonParseException{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("number")||!jsonObject.has("type")) {
            throw new JsonParseException("Le JSON ne contient pas tous les champs requis : 'x', 'y', 'number','type'");
        }
         return new AnnotationCellAction(
           game, 
           jsonObject.get("x").getAsInt(),
           jsonObject.get("y").getAsInt(),
           jsonObject.get("number").getAsInt());
       }


    }

    public static void main(String[] args) {
         Action a = new AnnotationCellAction(null, 0, 0, 0);
          System.out.println(a.serialise());
    }
    
}
