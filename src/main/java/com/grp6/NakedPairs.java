package com.grp6;

public class NakedPairs implements InterfaceTech {



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
             //   if (mat[i][j].getAnnotations().length == 2){

                    for (int k = 0; k<9;k++){
                       // System.out.println("Annotation de la case  "+ i +" "+j+ " et de annotation : "+ k+" :"+ mat[i][j].getAnnotations()[k]);
                        if(mat[i][j].getAnnotations()[k]){
                            cptAnnotation++;
                        }
                    }
                    if (cptAnnotation == 2) {
                        tabBool = mat[i][j].getAnnotations();
                        tabPair[cptPair] = donnerPair(tabBool);
                        cptPair++;                      
                        
                        }
                    }
            }
        
        //  System.out.println("verif tabPair");
        for(int i = 0; i<9;i++){
            int[] temp = tabPair[i];
            //affichage du tableau temporaire
            //System.out.println("tabTemp["+i+"] : "+temp[0]+" "+temp[1]);
            //System.out.println("tabPair["+i+"] : "+tabPair[i][0]+" "+tabPair[i][1]);
            for(int j = 0; j<9;j++){
                //if de la violence (désolée)
                if(temp[0] == tabPair[j][0]  && temp[1] == tabPair[j][1] && temp[0] !=0 && i!=j){
                    System.out.println("true de région");

                    return true;
                }
            }
        }
        return false;
    }

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
                if(line[i].getAnnotations()[k]){
                    cptAnnotation++;
                }
            }
            if (cptAnnotation == 2) {
                tabBool = line[i].getAnnotations();
                tabPair[cptPair] = donnerPair(tabBool);
                cptPair++;                      
                
            }


            cptAnnotation = 0;
            //vérfication des annotations dans les colonnes
            for (int k = 0; k<9;k++){
                if(col[i].getAnnotations()[k]){
                    cptAnnotation++;
                }
            }
            if (cptAnnotation == 2) {
                tabBool = col[i].getAnnotations();
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
    public boolean detect(Grid grille) {
        System.out.println("detect");
        for(int i = 0; i<9;i++){
            if(detectPairsCarre(i,grille) || detectPairs(i,grille)){
                return true;
            }
        }
        return false;
    
}


    private static void addAnnotations(Cell cell, int[] numbers) {
        if (cell instanceof FlexCell) {
            for (int num : numbers) {
                ((FlexCell) cell).addAnnotation(num);
            }
        }
    }   

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique des paires pointantes
    }

    public static void main(String[] args) {
        // Exemple de grille où une paire pointante peut être trouvée
        int[] data = {
            0,0,0,0,8,5,0,0,0,
            0,0,0,0,3,0,0,0,0,
            0,0,0,2,1,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());
        /* Cell cellule = grille.getCell(0, 3);

        if (cellule instanceof FlexCell) {
            grille.getCell(0, 3).addAnnotation(6);
            System.out.println("FlexCell");
        }
        else {
            System.out.println("Cell");
            
        }*/


        // annotations pour les régions
        
        /*grille.getCell(0, 3).addAnnotation(6);
        grille.getCell(0, 3).addAnnotation(7);
        grille.getCell(0, 3).addAnnotation(9);

        grille.getCell(1, 3).addAnnotation(7);
        grille.getCell(1, 3).addAnnotation(9);

        grille.getCell(1, 5).addAnnotation(4);
        grille.getCell(1, 5).addAnnotation(7);
        grille.getCell(1, 5).addAnnotation(9);

        grille.getCell(2, 5).addAnnotation(7);
        grille.getCell(2, 5).addAnnotation(9);*/
        
        
        //annotations pour les colonnes
        /*grille.getCell(0, 2).addAnnotation(3);
        grille.getCell(0, 2).addAnnotation(6);

        grille.getCell(1, 2).addAnnotation(7);
        grille.getCell(1, 2).addAnnotation(5);

        grille.getCell(5, 2).addAnnotation(3);
        grille.getCell(5, 2).addAnnotation(6);
        grille.getCell(5, 2).addAnnotation(4);
        grille.getCell(5, 2).addAnnotation(5);

        grille.getCell(7, 2).addAnnotation(3);
        grille.getCell(7, 2).addAnnotation(6);
        grille.getCell(7, 2).addAnnotation(4);
        grille.getCell(7, 2).addAnnotation(7);

        grille.getCell(8, 2).addAnnotation(3);
        grille.getCell(8, 2).addAnnotation(6);*/

        //annotations pour les lignes
        grille.getCell(1, 0).addAnnotation(3);        
        grille.getCell(1, 0).addAnnotation(2);

        grille.getCell(1, 1).addAnnotation(3);
        grille.getCell(1, 1).addAnnotation(2);
        grille.getCell(1, 1).addAnnotation(5);
        grille.getCell(0, 1).addAnnotation(7);

        grille.getCell(1, 2).addAnnotation(3);
        grille.getCell(1, 2).addAnnotation(5);
        grille.getCell(1, 2).addAnnotation(7);

        grille.getCell(1, 7).addAnnotation(2);
        grille.getCell(1, 7).addAnnotation(3);
        
        /*for(int i=0; i<9; i++){
            System.out.print(grille.getCell(0,3).getAnnotations()[i] + " ");
        }
        System.out.println("\n");
        for(int i=0; i<9; i++){
            System.out.print(grille.getCell(1,3).getAnnotations()[i] + " ");
        }
        System.out.println("\n");
        for(int i=0; i<9; i++){
            System.out.print(grille.getCell(1,5).getAnnotations()[i] + " ");
        }
        System.out.println("\n");
        for(int i=0; i<9; i++){
            System.out.print(grille.getCell(2,5).getAnnotations()[i] + " ");
        }*/
    


        NakedPairs nakedPairs = new NakedPairs();
        System.out.println(nakedPairs.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.PointingPairs"
        }
    
    }
