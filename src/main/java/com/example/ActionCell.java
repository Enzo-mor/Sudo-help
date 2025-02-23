package com.example;

/**
 * cette classe permet d'effectuer les diffrente actions  au niveau de la cellule du jeu de sudoku
 * elle permet de faire l'action et de l'annuler
 * elle permet aussi de serialiser et deserialiser les actions
 * 
 * @version 1.0
 * @see Game
 * @see ActionManagerApply
 * @author Taise De Thèse
 */
abstract public class ActionCell extends Action {

 /**
 * represente les coordonnées X de la cellule
 */
protected final int x;

/**
 * represente les coordonnées Y de la cellule
 */
protected final int y;


/**
 * constructeur des actions sur les cellules
 * @param game represente le jeu sur lequel les actions seront appliquées
 * @param x represente les coordonnées X  de la cellule
 * @param y represente les coordonnées Y de la cellule
 */
public ActionCell(Game game, int x, int y) {
super(game);  
this.x = x;
this.y = y;
    
}
}