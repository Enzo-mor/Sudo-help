package com.grp6;
import java.util.ArrayList;
import java.util.List;


public class SingletonsNus implements InterfaceTech {

    public boolean detect(Grid grille){
        for (int i = 0; i < Grid.NB_NUM; i++) {
            Column colonne = new Column(i, grille);
            Line ligne = new Line(i, grille);
            Square carre = new Square(i / 3, i % 3, grille);
    
            if (colonne.emptyCell().size() == 1 || ligne.emptyCell().size() == 1) {
                List<Cell> listColonneNum = colonne.emptyCell();
    
                for (Cell cell : listColonneNum) {
                    if (ToolBox.OnlyOneAnnotation(cell.getAnnotations())) {
                        return true;  
                    }
                }
            }
        }
        return false;
    }

    public void applique(Grid Grille){
        
        
    }
    
}
