package com.grp6;

import java.util.List;


public class NakedPairs implements InterfaceTech {


    private int[] donnerPair(boolean[] tabBool){
        System.err.println("donnerPair");
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

        System.out.println("detectPairsCarre");
        //tableau de pairs
        int[][] tabPair = new int[9][2];
        int cptPair = 0;

        int cptAnnotation = 0;
        
        // On récupère les cellules de la région
        int[] indiceCell = grille.numToPosForSubGrid(num);
        Cell mat[][] = grille.getSubGrid(indiceCell[0], indiceCell[1]);


        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les pairs de la région
        for (int i =0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
             //   if (mat[i][j].getAnnotations().length == 2){

                    for (int k = 0; k<9;k++){
                        System.out.println("Annotation de la case  "+ i +" "+j+ " et de annotation : "+ k+" :"+ mat[i][j].getAnnotations()[k]);
                        if(mat[i][j].getAnnotations()[k]){
                            cptAnnotation++;
                        }
                    }
                //    System.out.println("cptAnnotation : "+ cptAnnotation);
                    //System.out.println("Annotation : "+ mat[i][j].getAnnotations());
                    if (cptAnnotation == 2) {
                        System.out.println("annotation de  taille 2 (Carre)");
                        tabBool = mat[i][j].getAnnotations();
                        tabPair[cptPair] = donnerPair(tabBool);
                        cptPair++;                      
                        
                        }
                    }
               // }
            }
        
        // On vérifie si on a trouvé une paire   
      //  System.out.println("verif tabPair");
        for(int i = 0; i<9;i++){
            int[] temp = tabPair[i];
            for(int j = 0; j<9;j++){
                if(temp == tabPair[j] && i!=j){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean detectPairs(int num,Grid grille){

        //System.out.println("detectPairs");
        //tableau de pairs
        int [][] tabPair = new int[9][2];
        int cpt = 0;

        // On récupère les cellules dans les lignes et colonnes
        Cell line[] = grille.getLine(num);
        Cell col[] = grille.getColumn(num);

        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les pairs dans les lignes
        for (int i = 0; i<9;i++){
            if (line[i].getAnnotations().length == 2){
                System.out.println("annotation de  taille 2 (ligne)");
                tabBool = line[i].getAnnotations();
                tabPair[cpt] = donnerPair(tabBool);
                cpt++;
            }
        }

        // On récupère les pairs dans les colonnes
        for (int i = 0; i<9;i++){
            if (col[i].getAnnotations().length == 2){
                System.out.println("annotation de  taille 2 (colonne)");
                tabBool = col[i].getAnnotations();
                tabPair[cpt] = donnerPair(tabBool);
                cpt++;
            }
        }

        // On vérifie si on a trouvé une paire
        for(int i = 0; i<9;i++){
            int[] temp = tabPair[i];
            for(int j = 0; j<9;j++){
                if(temp == tabPair[j] && i!=j){
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
            2,5,0,4,7,3,6,1,8,
            6,1,3,8,2,9,4,7,5,
            7,8,4,5,6,1,9,2,3,
            9,3,1,2,5,7,8,6,4,
            5,4,7,6,8,3,1,9,2,
            8,6,2,1,9,4,7,5,3,
            1,7,8,3,4,2,5,9,6,
            3,9,5,7,1,6,2,4,0,
            4,2,6,9,0,5,3,8,7
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());



        // Ajout d'annotations sur les cellules vides (indices connus)
        //grille.getCell(0, 3).addAnnotation(6);
        addAnnotations(grille.getCell(0, 3), new int[]{6,7,9});  // Seul 9 possible
        addAnnotations(grille.getCell(1, 3), new int[]{7,9});
        addAnnotations(grille.getCell(1, 5), new int[]{4,7,8});
        addAnnotations(grille.getCell(2, 5), new int[]{7,9});
        //addAnnotations(grille.getCell(8, 5), new int[]{3, 4, 9});
        //addAnnotations(grille.getCell(8, 2), new int[]{3, 4, 9});

        for(int i=0; i<9; i++){
            System.out.print(grille.getCell(0,3).getAnnotations()[i] + " ");
        }
        System.out.println();


        NakedPairs nakedPairs = new NakedPairs();
        //System.out.println(nakedPairs.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.PointingPairs"
        }
    
    }
