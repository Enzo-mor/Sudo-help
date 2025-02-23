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

public class NumberCellAction extends ActionCell  {
    private int number;
    private int old_number;
   /**
    * methode permettant de creer une action qui permet de modifier une valeur à une cellule specifique de la grille
    * @param grid represente  la grille à modifier
    * @param x represente les coordonnées X 
    * @param y represente les coordonnées Y
    * @param number represente la nouvelle valeur à modifier
    * @param old_number represente l'anciene valeur presente à  cette position
    */
    public NumberCellAction(Game game, int x, int y, int number, int old_number) {
        super(game,x,y);
        this.number = number;
        this.old_number = old_number;
    }
    
    /**
     * methode permettant de retourner la chaine representant l'action
     * @return
     */
    @Override
    public String toString(){
        return "modification de la valeur de la cellule à la position : x = "+x+" et y = "+y+" de "+old_number+" à "+number;
    }
    @Override
     protected void doAction() {
        game.getGrid().getMutableCell(x, y).setNumber(number);
    }

    @Override
    protected void undoAction() {
        game.getGrid().getMutableCell(x, y).setNumber(old_number);
    }
    @Override
    public String actionType(){
        return "actionNombre";
    }
    public JsonObject serialise(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type",actionType());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("number", number);
        jsonObject.addProperty("old_number", old_number);
        return jsonObject; 

    }
    @Override
    public String jsonEncode(){
        //creation de l'adaptateur Gson qui permet de matcher notre classe  NumberCellAction avec la classe NumberCellActionSerialiser 
        Gson gson=new GsonBuilder()
                  .registerTypeAdapter(NumberCellAction.class, new NumberCellActionSerialiser() )
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
    public  static NumberCellAction jsonDecode(String json,Game game) throws Exception{
        try{
            Gson gson=new GsonBuilder()
            .registerTypeAdapter(NumberCellAction.class, new NumberCellActionDeserialiser(game) )
            .create();
             return gson.fromJson(json, NumberCellAction.class);
        }catch ( JsonParseException e) {
            throw new JsonParseException("Erreur de désérialisation : " + e.getMessage(), e);
        }
    }

    /**
         * methode de serialiser la l'action NumberCellAction en objet json
         * qui nous permettra de genrer le String plus tard 
         */
    private class NumberCellActionSerialiser implements JsonSerializer<NumberCellAction>{

       
       public  JsonElement serialize(NumberCellAction action, Type vartype, JsonSerializationContext context){
        return action.serialise();
       }
    }
    /**
         * methode de déserialiser en objet json en une action de type 
         * NumberCellAction>
         */
    private static class NumberCellActionDeserialiser implements JsonDeserializer<NumberCellAction> {
        /**
         * represente le jeu dans lequel qui sera contenu dans l'annotation
         */
        private Game game;
        /**
         *  constructeur de la classe 
         * @param grid represente la grille qui sera contenu dans l'annotation
         */
     public   NumberCellActionDeserialiser(Game game){
        this.game=game;
     }
    public NumberCellAction  deserialize(JsonElement jsonElement, Type vartype, JsonDeserializationContext context) throws JsonParseException{
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (!jsonObject.has("x") || !jsonObject.has("y") || !jsonObject.has("number")|| !jsonObject.has("old_number")||!jsonObject.has("type")) {
            throw new JsonParseException("Le JSON ne contient pas tous les champs requis : 'x', 'y', 'number','old_number','type");
        }
         return new NumberCellAction(
           game, 
           jsonObject.get("x").getAsInt(),
           jsonObject.get("y").getAsInt(),
           jsonObject.get("number").getAsInt(), 
           jsonObject.get("old_number").getAsInt());
    }


    
        
    }
}
