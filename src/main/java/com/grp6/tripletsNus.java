package com.grp6;
import java.util.ArrayList;
import java.util.List;

public class tripletsNus implements InterfaceTech {

    @Override
    public boolean detect(Grid grille){

        for (int i = 0; i < Grid.NB_NUM; i++) {
            Square carre = new Square(i / 3, i % 3, grille);
        }
        return false;
    }   

    @Override
    public void applique(Grid Grille){
        
        
    }
}