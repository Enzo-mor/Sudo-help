package grp6.syshelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;


/**
 * Cette classe permet d'obtenir une aide sur la grille de sudoku en se basant de  la technique de HiddenPairs.
 * @see InterfaceTech
 * @see Help
 * @see Cell
 * @see Grid
 * @author Taïse De Thèse
 * @version 1.0 
 * @since 2025-03-25
 */
public class HiddenPairs implements InterfaceTech{

    

   

    /**
     * cette methode permet de verifier si une cellule peut contiennir des  candidats caché.
     * @param cell
     * @return true si la cellule peut contienir des candidats caché, false sinon
     */
    protected Boolean hasPossibleCellHidden(Cell cell){
        return cell.isEditable() && cell.getAnnotations().size() >= 2;
    }

    /**
     * cette methode permet de retourner les candidats cachés dans une cellule.
     * @param cell1 : la cellule à partir de laquelle on cherche les candidats cachés
     * @param cell2 : la cellule à partir de laquelle on cherche les candidats cachés
     * @return les candidats cachés si ils existent, null sinon
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
     * cette permet de retouner le nombre autorisé de candidats cachés qui doit être trouvé dans une cellule.
     * @return
     */
    protected int getHiddenLength(){
        return 2;
    }

    /**
     * cette methode permet de retourner la cellule cachée.
     * @param cell : la cellule à partir de laquelle on cherche la cellule cachée
     * @param otherCells : les autres cellules de la ligne, colonne ou sous grille de la cellule
     * @return la cellule cachée si elle existe, null sinon
     */
    private Cell getHiddenCell(Cell cell,Cell[] otherCells) {
        ArrayList<Integer> candidates = new ArrayList<>();
        //recherche des candidats cachés
        int compteur=0;
        for(int candidat:cell.getAnnotations()){
           
            for(Cell otherCell:otherCells){
                if(cell!=otherCell&&otherCell.isEditable() && otherCell.getAnnotations().contains(candidat)){
                    compteur++;
                }
            }
            if(compteur==1){
                candidates.add(candidat);
               

            }
            compteur=0;
        }

        if(candidates.size()==this.getHiddenLength()){
            for(Cell otherCell:otherCells){
                //System.err.println("coucou");
                if(cell!=otherCell&&otherCell.isEditable() && otherCell.getAnnotations().containsAll(candidates)){
                    return otherCell.clone();
                }
            }
        }

        return null;
    }

     
    public String getName(){
        return "Paires cachées";
    }



      public Help getHelp(Grid grille){
      for(int i=0;i<Grid.NB_NUM;++i){
            for(int j=0;j<Grid.NB_NUM;++j){
                if(this.hasPossibleCellHidden(grille.getCell(i, j))){

                  Cell LineCell = getHiddenCell(grille.getCell(i, j), grille.getLine(i));
                  Cell ColumnCell = getHiddenCell(grille.getCell(i, j), grille.getColumn(j));
                  Cell SubgridCell = getHiddenCell(grille.getCell(i, j), grille.getFlatSubGrid(i, j));
                  System.err.println(LineCell);

                  if (Arrays.asList(LineCell, ColumnCell).stream().filter(cell -> cell != null).count() == 1||SubgridCell!=null) {
                    Help help = new Help(getName());
                    help.addPos(i, j);

                     if(SubgridCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j), SubgridCell)).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotions  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", ")) +" dans la sous grille à laquelle se trouve la cellule à la ligne "+(i+1)+" et colonne "+(j+1));
                        help.setMessage(2,"veuillez appliquer la technique de "+getName()+ " aux cellules dans la sous grille situé à la ligne "+(i+1)+" et colonne "+(j+1)+" de même pour  la cellule situé à la ligne "+(SubgridCell.getPosition()[0]+1)+" et la colonne "+(SubgridCell.getPosition()[1]+1));
                        help.addSquare(SubgridCell.getPosition()[0], SubgridCell.getPosition()[1]);
                        return help;
                    }

                    else if(LineCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j), LineCell)).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotions  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", ")) +" sur la ligne "+(i+1));
                        help.setMessage(2,"veuillez appliquer la technique de "+getName()+ " aux cellules dans la ligne "+(i+1)+"et colonnes "+(j+1)+","+(LineCell.getPosition()[1]+1));
                        help.addPos(LineCell.getPosition()[0], LineCell.getPosition()[1]);
                        return help;
                    }else if(ColumnCell!=null){
                        ArrayList<Integer> candidates = new ArrayList<>(Arrays.stream(getCandidate(grille.getCell(i, j),ColumnCell )).boxed().toList());
                        help.setMessage(1,"veuillez faire attention aux annotions  "+candidates.stream().map(String::valueOf).collect(Collectors.joining(", "))+" sur la colonne "+(j+1));
                        help.setMessage(2,"veuillez appliquer la technique de "+getName()+ " aux cellules dans la colonne "+(j+1)+"et lignes "+(i+1)+","+(ColumnCell.getPosition()[0]+1));
                        help.addPos(ColumnCell.getPosition()[0], ColumnCell.getPosition()[1]);
                        return help;
                    }
                    
                  }
                }
            }
      } 

        return null;
      }


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




