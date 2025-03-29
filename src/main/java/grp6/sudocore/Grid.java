package grp6.sudocore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Cette classe represente une grille de Sudoku.
 * 'Grid' est iterable, cela veut dire qu'elle peut etre utilisee dans un for-each.
 * 
 * Les cellules ne pourront etre modifiees que par le biais d'une action dans un Jeu.
 * 
 * @author POUSSE Kilian
 * @author NGANGA YABIE Taïse de These
 * 
 * @see Cell   Interface des cellules contenant toutes les methodes en ecriture.
 * @see Action Interface permettant de modifier les cellules d'un jeu.
 * @see Game   Classe representant un jeu de Sudoku.
 */
public final class Grid implements Iterable<Cell> {

    /* ======= Constantes de Classes ======= */

    /** 
     * Nombre de valeurs que peut prendre un chiffre [1, 9] 
     */
    public static final int NB_NUM = 9; 
    
    /** 
     * Nombre de cellules sur une ligne/colonne d'une sous-grille 
     */
    public static final int NB_SUBGRID = 3;   

    /* ======= Variables d'instance ======= */
    
    /** 
     * Liste des cellules de la grille 
     */
    private final List<Cell> cells;
    
    /** 
     * Liste des cellules de la grille resolue 
     */
    private List<Cell> solvedCells;
    
    /** 
     * Identifiant unique de la grille 
     */
    private final Integer id;        
    
    /** 
     * Difficulte de la grille (facile, moyen, difficile, etc.) 
     */
    private final SudoTypes.Difficulty difficulty;   

    /** 
     * Type de forme qu'on peut avoir dans un grille 
     */
    public static enum Shape {LINE, COLUMN, SQUARE;}    

    /* ======= Methodes de Classe ======= */

    /**
     * Permet de savoir si un chiffre est valide :
     * s'il est inclus dans ]0, Grid.NB_NUM].
     * 
     * @param number Chiffre a tester pour savoir s'il est valide [int]
     * @return Vrai si la valeur est valide, FAUX sinon.
     */
    public static boolean isValidNumber(int number) {
        return 0 <= number && number <= Grid.NB_NUM;
    }

    /**
     * Permet de savoir si un indice est valide :
     * s'il est inclus dans [0, Grid.NB_NUM[.
     * 
     * @param index Indice a tester pour savoir s'il est valide [int]
     * @return Vrai si l'indice est valide, FAUX sinon.
     */
    public static boolean isValidIndex(int index) {
        return 0 <= index && index < Grid.NB_NUM;
    }

    /* ======= Methodes d'instance ======= */

    /**
     * Constructeur de la classe 'Grid'.
     * 
     * @param id Identifiant de la grille
     * @param difficulty Difficulte de la grille
     * @param data Donnees de la grille sous forme de chaine
     */
    public Grid(int id, SudoTypes.Difficulty difficulty, String data) {
        this.id = id;
        this.cells = parseCells(data);
        this.difficulty = difficulty;
        this.solvedCells = SudokuSolver.solveCells(this.cells);
    }

    /**
     * Constructeur prive pour creer une nouvelle instance de grille,
     * generalement utilise pour cloner une grille.
     * 
     * @param id Identifiant de la grille
     * @param difficulty Difficulte de la grille
     */
    private Grid(int id, SudoTypes.Difficulty difficulty) {
        this.id = id;
        this.difficulty = difficulty;
        this.cells = new ArrayList<Cell>();
        this.solvedCells = new ArrayList<Cell>();
    }

    /**
     * Constructeur de la classe 'Grid' pour les tests.
     */
    public Grid(int[] data) {
        SudoLog.debug("Creation d'une grille");
        this.id = 0;
        this.cells = new ArrayList<>();

        for(int i=0; i<data.length; i++) {
            int num = data[i];
            int x = i/Grid.NB_NUM;
            int y = i%Grid.NB_NUM;
            SudoLog.debug("Construction cellule de la grille ("+x+","+y+")");
            Cell cell;
            System.out.println("num"+num);
            if(num==0){
                cell = new FlexCell(x, y);
            }else{
                cell = new FixCell(num,x, y);
            }
            //cell.setNumber(num);
            cells.add(cell);
        } 
        SudoLog.debug("Fin creation des cellules");
        this.difficulty = SudoTypes.Difficulty.EASY;
        SudoLog.debug("Resolution de la grille");
        this.solvedCells = SudokuSolver.solveCells(this.cells);
        SudoLog.debug("Fin creation de la grille");
    }

    /**
     * Initialisation d'une grille avec une chaine de caracteres
     * @param cells chaine de caracteres des cellules
     */
    public Grid(String cells) {
        this.id = 0;
        this.cells = parseCells(cells);
        this.difficulty = SudoTypes.Difficulty.EASY;
    }

    /**
     * Permet de cloner une grille.
     * Cette operation cree une nouvelle instance de la grille, 
     * permettant d'obtenir une copie sans affecter la grille d'origine.
     * 
     * @return Grid Une copie de la grille de depart.
     */
    public Grid clone() {
        Grid gridClone = new Grid(this.id, this.difficulty);
        this.cells.forEach((cell) -> gridClone.cells.add(cell.clone()));
        gridClone.solvedCells = SudokuSolver.solveCells(gridClone.cells);
        return gridClone;
    }

    /**
     * Convertit une chaine de caracteres en liste de cellules.
     * 
     * @param data Donnees sous forme de chaine
     * @return Liste des cellules de la grille
     */
    private static List<Cell> parseCells(String data) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '0') {
                cells.add(new FlexCell(i/9,i%9));
            } else {
                cells.add(new FixCell((int) data.charAt(i) - '0',i/9,i%9));
            }
        }
        return cells;
    }

    /**
     * Permet de recuperer la cellule a la position (i, j).
     * 
     * @param i Indice de la ligne [int]
     * @param j Indice de la colonne [int]
     * @return La cellule a la ieme ligne et jeme colonne [Cell]
     */
    public Cell getCell(int i, int j) {
        try {
            return this.cells.get(NB_NUM * i + j);
        } catch (Exception e) {
            throw new IllegalArgumentException("Indice incorrect : " + e);
        }
    }

    /***
     * Permet de reinitialiser la grille.
     */
    protected void resetGrid(){
        this.cells.forEach((e)->{
            if(e.isEditable() && e instanceof FlexCell) {
                e.clear();
                ((FlexCell)e).trashAnnotation();
            }
        });
    }

    /**
     * Recupere la ieme ligne de la grille.
     * 
     * @param i Indice de la ligne [int]
     * @return La ieme ligne [Cell[]]
     * 
     * @throws IndexOutOfBoundsException si l'indice est invalide
     */
    public Cell[] getLine(int i) {
        if(!isValidIndex(i)) {
            throw new IndexOutOfBoundsException("Indice de ligne invalide: " + i);
        }
        Cell[] line = new Cell[NB_NUM];
        for(int j=0; j<NB_NUM; j++) {
            line[j] = this.getCell(i, j);
        }
        return line;
    }

    /**
     * Permet de determiner le nombre total de cellules flexibles presentes dans la grille.
     * 
     * @return Le nombre total de cellules flexibles
     */
    public Integer getNumberFlexCell(){
        Integer somme = 0;

        for(Cell c: this)
            if(c.isEditable())
                somme += 1;
        return somme;
    }

    /**
     * Recupere la jeme colonne de la grille.
     * Permet l'acces aux cellules en lecture seule.
     * 
     * @param j Indice de la colonne [int]
     * @return La jemme colonne [Cell[]]
     * 
     * @throws IndexOutOfBoundsException si l'indice est invalide
     */
    public Cell[] getColumn(int j) throws IndexOutOfBoundsException {
        if(!isValidIndex(j)) {
            throw new IndexOutOfBoundsException("Indice de colonne invalide: " + j);
        }

        Cell[] column = new Cell[NB_NUM];
        for(int i = 0; i < NB_NUM; i++) {
            column[i] = this.getCell(i, j);
        }
        return column;
    }

    /**
     * Recupere la sous-grille de la cellule a la position (i, j).
     * Une sous-grille est un carre de 3x3 dans une grille de Sudoku.
     * Toutes les cellules retournees par cette methode sont en lecture seule.
     * 
     * @param i Indice de la ligne de la cellule cible
     * @param j Indice de la colonne de la cellule cible
     * @return La sous-grille ou se trouve la cellule en (i, j) [Cell[][]]
     * 
     * @throws IndexOutOfBoundsException si les indices sont invalides
     */
    public Cell[][] getSubGrid(int i, int j) throws IndexOutOfBoundsException {
        if(!isValidIndex(i) || !isValidIndex(j)) {
            throw new IndexOutOfBoundsException("Indices de sous-grille invalides : (" + i + ", " + j + ")");
        }

        Cell[][] subGrid = new Cell[NB_SUBGRID][NB_SUBGRID];
        // Calcule les coordonnees de la sous-grille
        int n_line = (i / NB_SUBGRID) * NB_SUBGRID;
        int n_column = (j / NB_SUBGRID) * NB_SUBGRID;

        // Creation de la sous-grille
        for(int di = 0; di < NB_SUBGRID; di++) {
            for(int dj = 0; dj < NB_SUBGRID; dj++) {
                subGrid[di][dj] = this.getCell(n_line + di, n_column + dj);
            }
        }
        System.out.println("==================================================");
        for(int di = 0; di < NB_SUBGRID; di++) {
            for(int dj = 0; dj < NB_SUBGRID; dj++) {
                System.out.println(subGrid[di][dj]);
            }
        }
        System.out.println("==================================================");
        return subGrid;
    }

    /**
     * Permet de generer l'iteration de la classe.
     * Seules les cellules en lecture seule peuvent etre parcoures.
     * 
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
        private int i = 0;    // indice des lignes
        private int j = 0;    // indice des colonnes

        /**
         * Permet de savoir s'il y a d'autres elements dans la collection.
         */
        @Override
        public boolean hasNext() {
            return i * NB_NUM + j < cells.size();
        }

        /**
         * Generer la prochaine cellule.
         */
        @Override
        public Cell next() {
            // Verifie s'il y a encore des elements dans la collection
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // Recupere la cellule a la position (i, j)
            Cell cell = getCell(i, j);
            j++;
            if(j >= NB_NUM) {
                j = 0;
                i++;
            }

            return cell;
        }
    }

    /**
     * Permet de savoir combien de cellules vides restent dans la grille.
     * 
     * @return Le nombre de cellules vides restantes [int]
     */
    public int emptyCellNumber() {
        int count = 0;
        // for-each de la classe 'Grid'
        for(Cell cell : this) {
            if(cell.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Transforme les cellules en chaîne de caractères.
     * @param grid Liste de cellules représentant une grille
     * @return La chaîne de caractères correspondante
     */
    public static String toString(List<Cell> grid) {
        String result = "";
        int i = 0, j = 0;

        for(Cell cell: grid) {
            i++;
            result += cell.toString() + " ";

            // Ajouter une séparation verticale tous les 3 colonnes
            if(i % 3 == 0 && i % NB_NUM != 0) {
                result += "| ";
            }

            // Aller à la ligne après chaque ligne
            if(i % NB_NUM == 0) {
                result += "\n";
                j++;

                // Ajouter un séparateur horizontal tous les 3 lignes
                if(j % 3 == 0 && j != NB_NUM) {
                    result += "------+-------+------\n";
                }
            }
        }
        return result;
    }

    /** 
     * Transforme la grille en chaîne de caractères.
     * @return La chaîne de caractères correspondante [String]
     */
    @Override
    public String toString() {
        return Grid.toString(cells);
    }

    /**
     * Getter: Identifiant de la grille.
     * @return Identifiant de la grille [Integer]
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter: Difficulté de la grille.
     * @return Difficulté de la grille [SudoTypes.Difficulty]
     */
    public SudoTypes.Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Getter: Liste des cellules correspondant à la grille de solution.
     * @return Liste de cellules (résolue) [List<Cell>]
     */
    public List<Cell> getSolvedCells() {
        return solvedCells;
    }

    /**
     * Vérifie si la grille est entièrement remplie et correctement résolue.
     * @return true si la grille est totalement identique à solvedCells, false sinon.
     */
    public boolean isFinished() {
        for (int i = 0; i < Grid.NB_NUM; i++) {
            for (int j = 0; j < Grid.NB_NUM; j++) {
                int idx = Grid.NB_NUM * i + j;
                if (cells.get(idx).getNumber() != solvedCells.get(idx).getNumber()) {
                    return false; // Une erreur détectée, la grille n'est pas totalement correcte
                }
            }
        }
        return true; // La grille est complètement correcte
    }

    /**
     * Évalue une grille (vérifier si elle est correcte).
     * @return Liste des coordonnées des chiffres mal placés [List<int[]>]
     */
    public List<int[]> evaluate() {
        List<int[]> res = new ArrayList<>();
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                if(!isCorrectCell(i, j)) {
                    int[] error = new int[2];
                    error[0] = i;
                    error[1] = j;
                    res.add(error);
                }
            }
        }
        return res;
    }

    /**
     * Évalue une grille (vérifier combien de chiffres sont correctement placés).
     * @return Nombre de chiffres bien placés dans la grille [int]
     */
    public int nbCorrectCells() {
        int res = 0;
        int idx;
        for(int i = 0; i < Grid.NB_NUM; i++) {
            for(int j = 0; j < Grid.NB_NUM; j++) {
                idx = NB_NUM * i + j;
                if((cells.get(idx).getNumber() == solvedCells.get(idx).getNumber()) && cells.get(idx) instanceof FlexCell) {
                    res += 1;
                }
            }
        }
        return res;
    }

    /**
     * Vérifie si une cellule donnée (r, c) est correcte.
     * @param r Indice de la ligne [int]
     * @param c Indice de la colonne [int]
     * @return true si la cellule est correcte, false sinon.
     */
    public boolean isCorrectCell(int r, int c) {
        int idx = NB_NUM * r + c;
        if(cells.get(idx).getNumber() == 0)
            return true;
        return (cells.get(idx).getNumber() == solvedCells.get(idx).getNumber());
    }

    /**
     * Obtient la taille de la liste des cellules.
     * @return Taille de la liste des cellules [int]
     */
    public int getSizeCells() {
        return cells.size();
    }

    /**
     * Recuperation d'une sous grille applati
     * @param a Indice de la ligne de la cellule cible
     * @param b Indice de la colonne de la cellule cible
     * @return sous grille applati
     */
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
    
    /**
     * Convertit un numero de sous-grille en coordonnees de depart (ligne, colonne) dans la grille principale.
     *
     * @param num Le numero de la sous-grille (0 a NB_NUM - 1).
     * @return Un tableau contenant la ligne et la colonne de depart de la sous-grille.
     * @throws IllegalArgumentException Si le numero de sous-grille est invalide.
     */
    public int[] numToPosForSubGrid(int num) {
        if (num < 0 || num >= NB_NUM) {
            throw new IllegalArgumentException("Numéro de sous-grille invalide: " + num);
        }
        
        int row = (num / NB_SUBGRID) * NB_SUBGRID;
        int col = (num % NB_SUBGRID) * NB_SUBGRID; 

        return new int[]{row, col};
    }

    /**
     * Calcule le nombre de cellules remplies dans une ligne, une colonne ou une sous-grille.
     *
     * @param shape La forme (ligne, colonne ou carré) a analyser.
     * @param num   L'index de la ligne, colonne ou sous-grille.
     * @return Le nombre de cellules non vides dans la forme spécifiée.
     */
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

    /**
     * Affiche la grille des annotations avec un formatage structure.
     * 
     * - Les cellules editables affichent leurs annotations.
     * - Les cellules fixes restent vides.
     * - Des separateurs verticaux et horizontaux sont ajoutes pour delimiter les sous-grilles.
     */
    public void printAnnotationsGrid() {
        // On fixe une largeur pour chaque cellule 
        final int cellWidth = 9;
        
        for (int row = 0; row < NB_NUM; row++) {
            for (int col = 0; col < NB_NUM; col++) {
                Cell cell = getCell(row, col);
                String annStr = "";
                // Pour les cellules flexibles, on affiche les annotations
                if (cell.isEditable()) {
                    StringBuilder sb = new StringBuilder();
                    for (int candidate = 1; candidate <= NB_NUM; candidate++) {
                        if (cell.getAnnotationsBool()[candidate - 1]) {
                            if (sb.length() > 0) {
                                sb.append(" | ");
                            }
                            sb.append(candidate);
                        }
                    }
                    annStr = sb.toString();
                    // S'il n'y a aucune annotation, on affiche un espace vide
                    if (annStr.isEmpty()) {
                        annStr = " ";
                    }
                } else {
                    // Pour une cellule fixe, on affiche rien (ou eventuellement "FIX")
                    annStr = " ";
                }
                
                // Affichage formate sur une largeur fixe
                System.out.print(String.format("%-" + cellWidth + "s", annStr));
                // Separateur vertical entre les blocs (3 colonnes par bloc)
                if ((col + 1) % NB_SUBGRID == 0 && col < NB_NUM - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            // Separateur horizontal entre les blocs (3 lignes par bloc)
            if ((row + 1) % NB_SUBGRID == 0 && row < NB_NUM - 1) {
                // Construction d'une ligne de separation adaptee a la largeur totale
                String separator = "";
                for (int i = 0; i < NB_NUM; i++) {
                    separator += String.format("%" + cellWidth + "s", "").replace(" ", "-");
                    if ((i + 1) % NB_SUBGRID == 0 && i < NB_NUM - 1) {
                        separator += "+";
                    }
                }
                System.out.println(separator);
            }
        }
    }
}