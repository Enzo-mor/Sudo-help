package com.grp6;

public class LastCell implements InterfaceTech  {
 
    public boolean detect(Grid grille) {

        for(int i = 0; i < Grid.NB_NUM;i++){
                Column colonne = new Column(i,grille);
                if(!colonne.complete()){
                    if (colonne.emptyCell().length == 1){
                        return true;
                }
            }
        }
        return false;   
    }
}
