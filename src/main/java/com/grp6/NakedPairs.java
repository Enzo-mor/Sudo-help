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

    private boolean detectPairs(int num,Grid grille) {

        //tableau de pairs
        int[][] tabPair = new int[9][2];
        int cpt = 0;

        
        // On récupère les cellules de la région
        int[] tab2 = grille.numToPosForSubGrid(num);
        Cell mat[][] = grille.getSubGrid(tab2[0], tab2[1]);

        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les pairs de la région
        for (int i =0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
                if (mat[i][j].getAnnotations().length == 2){
                        tabBool = mat[i][j].getAnnotations();
                            tabPair[cpt] = donnerPair(tabBool);
                            cpt++;                      
                    }
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
        for(int i = 0; i<9;i++){
            if(detectPairs(i,grille)){
                return true;
            }
        }
        return false;
    
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

        PointingPairs pointingPairs = new PointingPairs();
        System.out.println(pointingPairs.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.PointingPairs"
        }
    
    }
