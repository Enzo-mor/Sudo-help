package grp6.sudocore;

/* ====== Importation des libreries java ====== */
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
 * @author DE THESE Taise
 * 
 * @see Cell   Interface des cellules contenant toutes les methodes en ecriture.
 * @see Action Interface permettant de modifier les cellules d'un jeu.
 * @see Game   Classe representant un jeu de Sudoku.
 */
public final class Grid implements Iterable<Cell> {

    /* ======= Constantes de Classes ======= */
    /** Nombre de valeurs que peut prendre un chiffre [1, 9] */
    public static final int NB_NUM = 9; 
    
    /** Nombre de cellules sur une ligne/colonne d'une sous-grille */
    public static final int NB_SUBGRID = 3;   

    /* ======= Variables d'instance ======= */
    
    /** Liste des cellules de la grille */
    private final List<Cell> cells;
    
    /** Liste des cellules de la grille resolue */
    private List<Cell> solvedCells;
    
    /** Identifiant unique de la grille */
    private final Integer id;        
    
    /** Difficulte de la grille (facile, moyen, difficile, etc.) */
    private final SudoTypes.Difficulty difficulty;       

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

    /***
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
                cells.add(new FlexCell());
            } else {
                cells.add(new FixCell((int) data.charAt(i) - '0'));
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


    public static void main(String[] args) {

        try{
            Grid g = DBManager.getGrid(1);
            System.out.println(g);
            System.out.println("\n\n\n\n\n");
            System.out.println(g.clone());
            
        }catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
         }
    }


}
