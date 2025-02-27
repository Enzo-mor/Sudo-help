package com.example;
import java.lang.reflect.Type;

import com.example.SudoTypes.ActionType;
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
 * elle permet d'ajouter une annotation à une cellule flexible
 * 
 * @version 1.0
 * @see Game
 * @see ActionManagerApply
 * @see Action
 * @see ActionCell
 * @see SudoTypes
 */
public  class AnnotationCellAction extends ActionCell  {
    /**
     * represente le nombre à ajouter en annotation
     */
    protected int annotation;

    /**
     * constructeur permettant de creer une action qui permet d'ajouter une annotation à une cellule specifique de la grille
     * @param grid represente  la grille à modifier
     * @param x represente les coordonnées X de la cellule
     * @param y represente les coordonnées Y de la cellule
     * @param annotation represente la nouvelle annotation à ajouter
     */ 
    public AnnotationCellAction(Game game, int x, int y, int annotation) {
        super(game, x, y);
        this.annotation = annotation;
    }

    @Override
    protected void doAction() {
        this.game.getGrid().getMutableCell(x, y).addAnnotation(annotation);
    }

    @Override
    protected void undoAction() {
        this.game.getGrid().getMutableCell(x, y).removeAnnotation(annotation);
    }
    @Override
    public SudoTypes.ActionType actionType(){
        return ActionType.ANNOTATION_CELL_ACTION;
    }
    @Override
    public JsonObject serialise(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type",actionType().getDescription());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("number", annotation);
        return jsonObject; 

    }
    /**
     * methode permettant de retourner la chaine representant l'action
     * @return
     */
    @Override
    public String toString(){
        return " ajout de l'annotation  au jeu de valeur : "+annotation+ " à la position : x = "+x+" et y = "+y;
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
