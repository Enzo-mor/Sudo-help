package com.example;

import java.util.ArrayList;
/* ====== Importation des libreries java ====== */
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Cette classe represente une grille de Sudoku.
 * 'Grid' est iterable, cele veut dire qu'elle peut être utilisé dans un for each.
 * @author Kilian POUSSE
 * @version 1.2
 */
public class Grid implements Iterable<Cell> {

    /* ======= Constantes de Classes ======= */
    /** Nombre de valeurs que peut prendre un chiffre [1, 9] */
    public static final int NB_NUM     = 9; 
    /** Nombre de cellules sur une ligne/colonne d'une sous-grille */
    public static final int NB_SUBGRID = 3;   


    /* ======= Variables d'instance ======= */
    /** Tableau des cellules de la grille */
    private List<Cell> cells;
    /** Tableau des cellules de la grille résolue */
    private List<Cell> solvedCells;
    /** Identifiant de la grille */
    private Integer id;        
    /** Difficulté de la grille */     
    private String difficulty;       


    /* ======= Méthodes de Classe ======= */
    /**
     * Méthode permettant de savoir si un chiffre est valide:
     * si il inclut dans ]0, Grid.NB_NUM]
     * @param number Chiffre à tester si il est valide [int]
     * @return Vrai si la valeur est valide FAUX sinon
     */
    public static boolean isValidNumber(int number) {
        return 0 <= number && number <= Grid.NB_NUM;
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

    /**
     * Constructeur de la classe 'Grid'.
     */
    public Grid(int id, String difficulty, String data) {
        this.id = id;
        this.cells = parseCells(data);
        this.difficulty = difficulty;
        this.solvedCells = SudokuSolver.solveCells(this.cells);
    }
    /**
     * cette methode permet de creer une grille à partir d'un id et de la difficulter
     * cette methode est utileé pour cloner une grille
     * @param id
     * @param difficulty
     */
    private Grid(int id,String difficulty ){
        this.id = id;
        this.difficulty = difficulty;
        this.cells=new ArrayList<Cell>();

    };
   /***
    * methode permettant de cloner une grille
    * cette methode permettant de creer une nouvelle instance copie  de grille, afin d'eviter d'affecter la grille de depart
    * @return Grid une copie de la grille de depart
    */
    public Grid clone(){
         Grid gridclone=new Grid(this.id,this.difficulty);
        this.cells.forEach((cell)->gridclone.cells.add(cell.clone()));
        return gridclone;
    }

    /**
     * Convertit une chaîne de caractères en liste de cellules.
     * @param data Données sous forme de chaîne
     * @return Liste des cellules de la grille
     */
    private static List<Cell> parseCells(String data) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '0') {
                cells.add(new FlexCell());
            } else {
                cells.add(new FixCell((int) data.charAt(i) - '0'));
            }
        }
        return cells;
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
     * @return La iemme ligne [Cell[]]
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
     * @return La jemme colunne [Cell[]]
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
     * Transforme les cellules en chaîne de caractères
     * @param grid Liste de cellules representant une grille
     * @return La chaîne de caractères correspondante
     */
    public static String toString(List <Cell> grid) {
        String result = "";
        int i = 0, j = 0;

        for(Cell cell: grid) {
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
    
    /** 
     * Transforme la grille en chaîne de caractères
     * @return La chaîne de caractères correspondante [String]
     */
    @Override
    public String toString() {
        return Grid.toString(cells);
    }

    /**
     * Getter: Identifiant de la grille
     * @return Identifiant de la grille
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter: Difficulté de la grille
     * @return Difficulté de la grille
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Getter: Liste de cellules correspondante à la
     * grille de solution
     * @return Liste de cellules (résolue)
     */
    public List<Cell> getSolvedCells() {
        return solvedCells;
    }

    /**
     * Evaluer une grille (verifier si elle bonne)
     * @return Nombre d'erreur
     */
    public List<int[]> evaluate() {
        List<int[]> res = new ArrayList<>();

        for(int i=0; i<Grid.NB_NUM; i++) {
            for(int j=0; j<Grid.NB_NUM; j++) {
                int idx = NB_NUM*i+j;
                if(cells.get(idx).getNumber() != solvedCells.get(idx).getNumber()) {
                    int[] error = new int[2];
                    error[0] = i;
                    error[1] = j;
                    res.add(error);
                }
            }
        }
        return res;
    }
}
