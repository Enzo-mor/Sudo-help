package grp6.syshelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe représente la technique LastCell.
 * 
 * Elle permet de trouver la dernière cellule vide d'une ligne, colonne ou carré.
 * @author GRAMMONT Dylan
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class LastCell implements InterfaceTech {

    @Override
    public Help getHelp(Grid grille) {
        Help aide = new Help(getClass().getSimpleName());

        for (int i = 0; i < Grid.NB_NUM; i++) {
            // Vérifie les colonnes
            if (grille.numberOfFullCell(Grid.Shape.COLUMN, i) == 8) {
                Cell emptyCell = findEmptyCell(grille.getColumn(i));
                if (emptyCell != null) {
                    //int pos[] = emptyCell.getPosition();
                    //aide.addPos(pos[0], pos[1]);
                    aide.addColumn(i);
                    aide.setMessage(1, "Fais attention aux "+findLastNumber(grille.getColumn(i)));
                    aide.setMessage(2, "Fais attention aux colonnes");
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
            // Vérifie les lignes
            else if (grille.numberOfFullCell(Grid.Shape.LINE, i) == 8) {
                Cell emptyCell = findEmptyCell(grille.getLine(i));
                if (emptyCell != null) {
                    //int pos[] = emptyCell.getPosition();
                    //Message d'aide
                    //aide.addPos(pos[0], pos[1]);
                    aide.addLine(i);
                    aide.setMessage(1, "Fais attention aux "+findLastNumber(grille.getLine(i)));
                    aide.setMessage(2, "Fais attention aux lignes");
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
            // Vérifie les carrés
            else if (grille.numberOfFullCell(Grid.Shape.SQUARE, i) == 8) {
                int[] temp = grille.numToPosForSubGrid(i);
                Cell emptyCell = findEmptyCell(grille.getFlatSubGrid(temp[0],temp[1]));
                if (emptyCell != null) {
                    //int pos[] = emptyCell.getPosition();
                    //aide.addPos(pos[0], pos[1]);
                    aide.addSquare(i);
                    aide.setMessage(1, "Fais attention aux "+findLastNumber(grille.getFlatSubGrid(temp[0], temp[1])));
                    aide.setMessage(2, "Fais attention aux carrés");
                    aide.setMessage(3, "Regarde ici");
                    return aide;
                }
            }
        }
        return null; // Aucun coup trouvé
    }

    /**
     * Trouve la cellule vide dans une liste de cellules.
     * @param cells Liste des cellules (ligne, colonne ou carré)
     * @return La cellule vide trouvée, sinon null.
     */
    private Cell findEmptyCell(Cell[] cells){
        for (Cell cell : cells){
            if (cell.isEmpty()){
                return cell;
            }
        }
        return null;
    }

    /**
     * Trouve le nombre manquant dans une liste de Cell.
     * @param cells Liste des cellules (ligne, colonne ou carré)
     * @return Le number qui doit aller dans la cellule vide.
     */
    private int findLastNumber(Cell[] cells){
        List<Integer> list = new ArrayList<>(Collections.nCopies(10, 0));
        for (Cell cell : cells){
            int c = cell.getNumber();
            list.set(cell.getNumber(),list.get(c)+1);
        }
        for(int i=0;i<10;i++){
            if(list.get(i) == 0)return i;
        }
        return 0;
    }
}
