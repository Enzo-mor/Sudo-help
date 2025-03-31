package grp6.syshelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;


/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de HiddenPairs.
 * @author NGANGA YABIE Taise de These
 * @see InterfaceTech
 * @see Help
 * @see Cell
 * @see Grid
 */
public class HiddenPairs implements InterfaceTech{

    /** 
    *Constructeur de la classe HiddenPairs 
    */
    public HiddenPairs(){}

    /**
     * Verifie si une cellule peut contenir des candidats caches.
     * @param cell cellule concernee
     * @return true si la cellule peut contenir des candidats caches, false sinon
     */
    protected Boolean hasPossibleCellHidden(Cell cell){
        return cell.isEditable() && cell.getAnnotations().size() >= 2;
    }

    /**
     * Renvoie les candidats caches dans une cellule.
     * @param cell1 : la cellule a partir de laquelle on cherche les candidats caches
     * @param cell2 : la cellule a partir de laquelle on cherche les candidats caches
     * @return les candidats caches si ils existent, null sinon
     */
    protected int[] getCandidate(Cell cell1,Cell cell2){
           List<Integer>candidates= cell1.getAnnotations();
           candidates.retainAll(cell2.getAnnotations());
           if(candidates.size()==2){
               return new int[]{candidates.get(0),candidates.get(1)};
           }
           return null;
    }
    /**
     * Envoie le nombre autorise de candidats caches qui doit être trouve dans une cellule.
     * @return le nombre de candidats caches
     */
    protected int getHiddenLength(){
        return 2;
    }

    /**
     * Renvoie la cellule cachee.
     * @param cell : la cellule a partir de laquelle on cherche la cellule cachee
     * @param otherCells : les autres cellules de la ligne, colonne ou sous grille de la cellule
     * @return les cellules cachees si elles existent, null sinon
     */
    private ArrayList<Cell> getHiddenCell(Cell cell,Cell[] otherCells) {
        ArrayList<Integer> candidates = new ArrayList<>();
        ArrayList<Cell> hiddenCells = new ArrayList<>();
        //recherche des candidats cachés
        int compteur=0;
        for(int candidat:cell.getAnnotations()){
           
            for(Cell otherCell:otherCells){
                if(cell!=otherCell&&otherCell.isEditable() && otherCell.getAnnotations().contains(candidat)){
                    compteur++;
                }
            }
            if(compteur==getHiddenLength()-1){
                candidates.add(candidat);
               

            }
            compteur=0;
        }

        if(candidates.size()==this.getHiddenLength()){
            for(Cell otherCell:otherCells){
                if(cell!=otherCell&&otherCell.isEditable() && otherCell.getAnnotations().containsAll(candidates)){
                    hiddenCells.add(otherCell);
                }
            }

            if(hiddenCells.size()==this.getHiddenLength()-1){
                return hiddenCells;
            }
            else{
                return null;
            }
        }

        return null;
    }

    /**
     * Retourne le nom de la technique
     * @return nom de la technique
     */
    public String getName(){
        return "Paires cachées";
    }

    /**
     * Classe implementant la technique des paires cachees dans une grille Sudoku.
     * Cette technique permet d'identifier des annotations cachees dans une ligne, 
     * une colonne ou une sous-grille.
     * @param grille Grille sur laquelle chercher l'aide
     * @return l'aide HiddenPairs
     */
    @Override
    public Help getHelp(Grid grille){
        for(int i=0;i<Grid.NB_NUM;++i){
            for(int j=0;j<Grid.NB_NUM;++j){
                if(this.hasPossibleCellHidden(grille.getCell(i, j))){

                    ArrayList<Cell> LineCell = getHiddenCell(grille.getCell(i, j), grille.getLine(i));
                    ArrayList<Cell>  ColumnCell = getHiddenCell(grille.getCell(i, j), grille.getColumn(j));
                    ArrayList<Cell>  SubgridCell = getHiddenCell(grille.getCell(i, j), grille.getFlatSubGrid(i, j));

                    if (Arrays.asList(LineCell, ColumnCell).stream().filter(cell -> cell != null).count() == 1||SubgridCell!=null) {
                    Help help = new Help(getName());
                    help.addPos(i, j);

                        if(SubgridCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j), SubgridCell.getLast())).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotations  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", ")) +"\ndans sous les grilles("+grille.NB_SUBGRID+"*"+grille.NB_SUBGRID  +")");
                        
                        help.setMessage(3,"veuillez appliquer la technique de "+getName()+ "\n aux cellules dans la sous grille ("+grille.NB_SUBGRID+"*"+grille.NB_SUBGRID  +") \nou se trouve la cellule situé à la ligne "+(i+1)+" et colonne "+(j+1));
                        help.addSquare(SubgridCell.getLast().getPosition()[0]/Grid.NB_SUBGRID, SubgridCell.getLast().getPosition()[1]/Grid.NB_SUBGRID);
                        return help;
                    }

                    else if(LineCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j), LineCell.getLast())).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotations  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                        

                        String message="veuillez appliquer la technique de "+getName()+ "\n aux cellules suivante située  : \nla ligne "+(i+1)+"et colonne "+(j+1)+"\n";
                        for(Cell cell:LineCell){
                            message+="la ligne "+(i+1)+" et colonne "+(cell.getPosition()[1]+1)+"\n";
                            help.addPos(cell.getPosition()[0], cell.getPosition()[1]);
                        }
                        help.setMessage(3,message);
                        
                        return help;
                    }else if(ColumnCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j), ColumnCell.getLast())).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotations  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", ")));
                        
                        String message="veuillez appliquer la technique de "+getName()+ " aux cellules suivantes situées  : \nla ligne "+(i+1)+"et colonne "+(j+1)+"\n";
                        for(Cell cell:ColumnCell){
                            message+="la ligne "+(cell.getPosition()[0]+1)+" et colonne "+(j+1)+"\n";
                            help.addPos(cell.getPosition()[0], cell.getPosition()[1]);
                        }
                        help.setMessage(3,message);
                        return help;
                    }
                    
                    }
                }
            }
        }
        return null;
      }

    /**
     * Methode principale pour tester la detection des paires cachees dans une grille Sudoku.
     * Cette methode cree une grille, y ajoute des annotations, puis teste la detection.
     * 
     * @param args Arguments de ligne de commande (non utilises).
     */
    public static void main(String[] args) {
        int[] data = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        Grid grille = new Grid(data);
        System.out.println("Grille initiale :");
        grille.getCell(0, 0).addAnnotation(2);
        grille.getCell(0, 0).addAnnotation(4);
        grille.getCell(0, 0).addAnnotation(5);
        grille.getCell(0, 1).addAnnotation(2);
        
        grille.getCell(0, 1).addAnnotation(4);
        //grille.getCell(0, 2).addAnnotation(2);
        //grille.getCell(0, 2).addAnnotation(4);

        // grille.printAnnotationsGrid();

        
        System.out.println("\n");

        // Création d'une instance de la technique des paires pointantes
        HiddenPairs pairs = new HiddenPairs();

        // Détection
        System.out.println("triple pointantes  détectées ? " + pairs.getHelp(grille));
    }
}




