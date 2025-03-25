package grp6.syshelp;

import grp6.sudocore.Cell;
import grp6.sudocore.FlexCell;
import grp6.sudocore.Grid;
/**
 * Cette classe représente la technique NakedPairs.
 * 
 * Elle permet de trouver les pairs nus(2 chiffres apparaissant uniquement dans deux cases d'une unité(ligne colones carré)). 
 * @author GRAMMONT Dylan
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class NakedPairs implements InterfaceTech {
    
    Help aide = new Help(getClass().getSimpleName());

     private int[] donnerPair(boolean[] tabBool){
        int[] tab = new int[2];
        int j = 0;
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab[j] = i;
                j++;
            }
        }
        return tab;
    }
    /**
     * Permet de détecter les pairs dans un carré
     * @param num
     * @param grille
     * @return boolean si on a trouvé une paire dans un carré
     */
    private boolean detectPairsCarre(int num,Grid grille) {

        //tableau de pairs
        int[][] tabPair = new int[9][2];
        int cptPair = 0;

        int cptAnnotation;
        
        // On récupère les cellules de la région
        int[] indiceCell = grille.numToPosForSubGrid(num);
        Cell mat[][] = grille.getSubGrid(indiceCell[0], indiceCell[1]);


        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les pairs de la région
        for (int i =0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
                cptAnnotation = 0;

                    for (int k = 0; k<9;k++){
                        if(mat[i][j].getAnnotationsBool()[k]){
                            cptAnnotation++;
                        }
                    }
                    if (cptAnnotation == 2) {
                        tabBool = mat[i][j].getAnnotationsBool();
                        tabPair[cptPair] = donnerPair(tabBool);
                        cptPair++;                      
                        
                        }
                    }
            }
        
        //  System.out.println("verif tabPair");
        for(int i = 0; i<9;i++){
            int[] temp = tabPair[i];
            for(int j = 0; j<9;j++){
                //if de la violence (désolée)
                if(temp[0] == tabPair[j][0]  && temp[1] == tabPair[j][1] && temp[0] !=0 && i!=j){

                    System.out.println("true de région");
                    aide.addSquare(num);
                    aide.setMessage(1, "Fait attention aux carrés");
                    aide.setMessage(2, "Fait attention aux "+temp[0]+" "+temp[1]);
                    aide.setMessage(3, "Regarde les annotations"+temp[0]+" "+temp[1]+"le carré : "+num);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Permet de détecter les pairs dans une ligne ou une colonne
     * @param num
     * @param grille
     * @return boolean si on a trouvé une paire dans une ligne ou une colonne
     */
    private boolean detectPairs(int num,Grid grille){


        //tableau de pairs
        int [][] tabPair = new int[9][2];
        int cptPair = 0;

        int cptAnnotation;


        // On récupère les cellules dans les lignes et colonnes
        Cell line[] = grille.getLine(num);
        Cell col[] = grille.getColumn(num);

        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les pairs dans les lignes
        for (int i = 0; i<9;i++){

            cptAnnotation = 0;
          
            //vétification des annotations dans les lignes
            for (int k = 0; k<9;k++){
                if(line[i].getAnnotationsBool()[k]){
                    cptAnnotation++;
                }
            }
            if (cptAnnotation == 2) {
                aide.addLine(num);
                tabBool = line[i].getAnnotationsBool();
                tabPair[cptPair] = donnerPair(tabBool);
                cptPair++;                      
                
            }


            cptAnnotation = 0;
            //vérfication des annotations dans les colonnes
            for (int k = 0; k<9;k++){
                if(col[i].getAnnotationsBool()[k]){
                    cptAnnotation++;
                }
            }
            if (cptAnnotation == 2) {
                aide.addColumn(num);
                tabBool = col[i].getAnnotationsBool();
                tabPair[cptPair] = donnerPair(tabBool);
                cptPair++;                      
                
            }
                    
            
        }
        // On vérifie si on a trouvé une paire
        for(int i = 0; i<9;i++){
            int[] temp = tabPair[i];
            System.out.println("tabTemp["+i+"] : "+temp[0]+" "+temp[1]);
            System.out.println("tabPair["+i+"] : "+tabPair[i][0]+" "+tabPair[i][1]);
            for(int j = 0; j<9;j++){
                //if de la violence (désolée)
                if(temp[0] == tabPair[j][0]  && temp[1] == tabPair[j][1] && temp[0] !=0 && i!=j){
                    System.out.println("true de ligne et colonne");
                    return true;
                }
            }
        }
        return false;
    }
        
    @Override
    public Help getHelp(Grid grille) {
        System.out.println("detect");
        for(int i = 0; i<9;i++){
            if(detectPairsCarre(i,grille) || detectPairs(i,grille)){
                return  aide;
            }
        }
        return null;
    
}

}
