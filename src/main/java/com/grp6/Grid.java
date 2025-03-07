package com.grp6;

import java.util.ArrayList;
/* ====== Importation des libreries java ====== */
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

/**
 * Cette classe represente une grille de Sudoku.
 * 'Grid' est iterable, cele veut dire qu'elle peut être utilisé dans un for each.
 */
public class Grid implements Iterable<Cell> {

    /* ======= Constantes de Classes ======= */
    public static final int NB_NUM     = 9;   // Nombre de valeurs que peut prendre un chiffre [1, 9]
    public static final int NB_SUBGRID = 3;   // Nombre de cellules sur une ligne/colonne d'une sous-grille

    /* ======= Type abstrait ======= */
    public enum Shape {
        LINE, COLUMN, SQUARE;
    }


    /* ======= Variables d'instance ======= */
    private ArrayList<Cell> cells;     // tableau des cellules de la grille


    /* ======= Méthodes de Classe ======= */
    /**
     * Méthode permettant de savoir si un chiffre est valide:
     * si il inclut dans ]0, Grid.NB_NUM]
     * @param number Chiffre à tester si il est valide [int]
     * @return Vrai si la valeur est valide FAUX sinon
     */
    public static boolean isValidNumber(int number) {
        return 0 < number && number <= Grid.NB_NUM;
    }

    /**
     * Méthode permettant de savoir si un indice est valide:
     * si elle inclut dans [0, Grid.NB_NUM[
     * @param index Indice à tester si il est valide [int]
     * @return Vrai si l'indice est valide FAUX sinon
     */
    public static boolean isValidIndex(int index) {
        return 0 <= index && index < Grid.NB_NUM;
    }


    /* ======= Méthodes d'instance ======= */

    public Grid(int id) {
        //Main.
    }

    /**
     * Constructeur de la classe 'Grid'.
     * /!\/!\LE CONSTRUCTEUR EST TEMPORAIRE /!\/!\
     */
    public Grid(int[] data) {
        if(data.length != NB_NUM * NB_NUM){
            throw new IllegalArgumentException("Taille des donnnees incorrect");
        }
        this.cells = new ArrayList<>();

        // TEMPORAIRE: en attente du chargement par la BdD
        // Initialisation de la tableau avec les donnees
        for(int i=0; i<NB_NUM*NB_NUM; i++) {
            if(data[i] == 0)
                this.cells.add(new FlexCell(i));
            // Si la callule est remplit alors elle est fixe
            else
                this.cells.add(new FixCell(data[i],i));
        }
    }

    /**
     * Permet de récupérer la cellule à la position (i, j)
     * @param i Indice de la ligne [int]
     * @param j Indice de la colonne [int]
     * @return La cellule à la ieme ligne et jeme colonne [Cell]
     */
    public Cell getCell(int i, int j) {
        try {
            return this.cells.get(NB_NUM*i+j);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Indice incorrect : " + e);
        }
    }

    /**
     * Recupere la ieme ligne de la grille
     * @param i Indice de la ligne [int]
     * @return La ieme ligne [Cell[]]
     */
    public Cell[] getLine(int i) {
        if(!isValidIndex(i)) {
            throw new IllegalArgumentException("Indice de ligne invalide: " + i);
        }

        Cell[] line = new Cell[NB_NUM];
        for(int j=0; j<NB_NUM; j++) {
            line[j] = this.getCell(i, j);
        }
        return line;
    }

    /**
     * Recupere la jeme colunne de la grille
     * @param j Indice de la colunne [int]
     * @return La jeme colunne [Cell[]]
     */
    public Cell[] getColumn(int j) {
        if(!isValidIndex(j)) {
            throw new IllegalArgumentException("Indice de colonne invalide: " + j);
        }

        Cell[] column = new Cell[NB_NUM];
        for(int i=0; i<NB_NUM; i++) {
            column[i] = this.getCell(i, j);
        }
        return column;
    }

    /**
     * Recupere la sous-grille de la cellule à la position (i, j)
     * Une sous-grille est les carrés de 3*3 d'une grille de Sudoku
     * @param i Indice de la ligne de la cellule cible
     * @param j Indice de la colonne de la cellule cible
     * @return La sous-grille ou se trouve la cellule en (i, j) [Cell[][]]
     */
    public Cell[][] getSubGrid(int i, int j) {
        if(!isValidIndex(i) || !isValidIndex(j)) {
            throw new IllegalArgumentException("Indices de sous-grille invalides : (" + i + ", " + j + ")");
        }

        Cell[][] subGrid = new Cell[NB_SUBGRID][NB_SUBGRID];
        // Calcule les coordonnées de la sous-grille
        int n_line = (i/NB_SUBGRID)*NB_SUBGRID;
        int n_column = (j/NB_SUBGRID)*NB_SUBGRID;

        // Création de la sous-grille
        for(int di=0; di<NB_SUBGRID; di++) {
            for(int dj=0; dj<NB_SUBGRID; dj++) {
                subGrid[di][dj] = this.getCell(n_line + di, n_column + dj);
            }
        }
        return subGrid;
    }

    public Cell[] getFlatSubGrid(int a, int b){
        Cell[][] subGrid = this.getSubGrid(a,b);
        Cell[] res = new Cell[NB_NUM];
        int j = 0;
        for(int i = 0;i < 3;i++){
            for(int y = 0;y < 3;y++){
                res[j++] = subGrid[i][y];
            }
        }
        return res;
    }

    private boolean forComplete(Cell [] tab){
        for(int i = 0; i < 9; i++){
            if(tab[i].isEmpty()){
                return false;
            }
        }
        return true;
    }

    public boolean complete(Shape shape,int num){
        switch(shape){
            case LINE -> {
                return forComplete(this.getLine(num));
            }
            case COLUMN -> {
                return forComplete(this.getColumn(num));
            }
            case SQUARE -> {
                int [] tabNum = numToPosForSubGrid(num);
                return forComplete(this.getFlatSubGrid(tabNum[0],tabNum[1]));
            }
        }
        return false;
    }

    public int numberOfFullCell(Shape shape,int num){
        int res = 0;
        switch(shape){
            case LINE -> {
                Cell [] line = this.getLine(num);
                for(int i = 0; i < 9; i++){
                    if(!line[i].isEmpty()){
                        res++;
                    }
                }
            }
            case COLUMN -> {
                Cell [] column = this.getColumn(num);
                for(int i = 0; i < 9; i++){
                    if(!column[i].isEmpty()){
                        res++;
                    }
                }
            }
            case SQUARE -> {
                int [] tabNum = numToPosForSubGrid(num);
                Cell [][] square = this.getSubGrid(tabNum[0],tabNum[1]);
                for(int i = 0; i < NB_SUBGRID; i++){
                    for(int j = 0; j < NB_SUBGRID;j++){
                        if(!square[i][j].isEmpty()){
                            res++;
                        }
                    }
                }
            }
        }
        return res;
    }

    public int[] numToPosForSubGrid(int num) {
        if (num < 0 || num >= NB_NUM) {
            throw new IllegalArgumentException("Numéro de sous-grille invalide: " + num);
        }
        
        int row = (num / NB_SUBGRID) * NB_SUBGRID;
        int col = (num % NB_SUBGRID) * NB_SUBGRID; 

        return new int[]{row, col};
    }

    private List<Cell> forFullCell(Cell [] tab){
        List<Cell> cellPlein = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            if(!tab[i].isEmpty()){
                cellPlein.add(tab[i]);
            }
        }
        return cellPlein;
    }

    /**
     * Permet de savoir combien de cellule pleine reste-t-il
     * @return Le nombre de cellules pleines restantes [cell[]]
     * @param une colonne de cellule [Cell[]] 
     */
    public List<Cell> fullCell(Shape shape,int num){
        return switch(shape){
            case LINE -> {
                yield forFullCell(this.getLine(num));
            }
            case COLUMN -> {
                yield forFullCell(this.getColumn(num));
            }
            case SQUARE -> {
                int [] tabNum = numToPosForSubGrid(num);
                yield forFullCell(this.getFlatSubGrid(tabNum[0],tabNum[1]));
            }
        };
    }


    /**
     * Methode permettant de gener l'iteration de la classe
     * @return Retourne une instance de la sous-classe iterable
     */
    @Override
    public Iterator<Cell> iterator() {
        return new GridIterator();
    }

    /**
     * Sous-classe de 'Grid' permettant l'iteration de la classe mere.
     */
    private class GridIterator implements Iterator<Cell> {
        /* ==== Variables d'instance ==== */
        private int i=0;    // indice des lignes
        private int j=0;    // indice des colonnes

        /**
         * Permet de savoir si il y a d'autre element dans la collection
         */
        @Override
        public boolean hasNext() {
            return i * NB_NUM + j < cells.size();
        }

        /**
         * Generer la prochaine cellule
         */
        @Override
        public Cell next() {
            // Check si il y a encore des elements dans la collection
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // Recupere la cellule à la position (i, j)
            Cell cell = getCell(i, j);
            j++;
            if(j >= NB_NUM) {
                j=0;
                i++;
            }

            return cell;
        }
    }

    /**
     * Permet de savoir combien de cellule vide reste-t-il
     * @return Le nombre de cellules vides restantes [int]
     */
    public int emptyCellNumber() {
        int count = 0;
        // for each de la class 'Grid'
        for(Cell cell: this) {
            if(cell.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /** 
     * Transforme la cellule en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    @Override
    public String toString() {
        String result = "";
        int i = 0, j = 0;

        for(Cell cell: this) {
            i++;
            result += cell.toString() + " ";

            // Ajouter une séparation verticale tous les 3 colonnes
            if(i%3 == 0 && i%NB_NUM != 0) {
                result += "| ";
            }

            // Aller à la ligne après chaque ligne
            if(i%NB_NUM == 0) {
                result += "\n";
                j++;

                // Ajouter un séparateur horizontal tous les 3 lignes
                if(j%3 == 0 && j != NB_NUM) {
                    result += "------+-------+------\n";
                }
            }
        }
        return result;
    }




    
    /*@Entity
    @Table(name = "grid")
    public class GridData {*/

        /* ======= Variables d'instance ======= */
        /*@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "cells", length = 81, nullable = false)
        private String cells;

        @Column(name = "difficulty", nullable = false)
        @Enumerated(EnumType.STRING)
        private String difficulty;

        // Getters et setters
        public Integer getId() {
            return id;
        }

        public String getCells() {
            return cells;
        }
        
    }*/
}
