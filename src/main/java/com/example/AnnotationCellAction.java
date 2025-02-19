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

public class AnnotationCellAction implements Action  {

    private int x, y;
    private int number;
    private Grid grid;

    public AnnotationCellAction(Grid grid, int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.grid = grid;
    }

    @Override
    public void doAction() {
        grid.getCell(x, y).addAnnotation(number);
    }

    @Override
    public void undoAction() {
        grid.getCell(x, y).removeAnnotation(number);
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
    public  static AnnotationCellAction jsonDecode(String json,Grid grid) throws Exception{
        try{
        Gson gson=new GsonBuilder()
        .registerTypeAdapter(AnnotationCellAction.class, new AnnotationCellActionDeserialiser(grid) )
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
        private final Grid grid;
     public   AnnotationCellActionDeserialiser(Grid grid){
        this.grid=grid;
     }
    public AnnotationCellAction  deserialize(JsonElement jsonElement, Type vartype, JsonDeserializationContext context) throws JsonParseException{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("number")||!jsonObject.has("type")) {
            throw new JsonParseException("Le JSON ne contient pas tous les champs requis : 'x', 'y', 'number','type'");
        }
         return new AnnotationCellAction(
           grid, 
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
