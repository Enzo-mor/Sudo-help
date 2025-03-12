package com.grp6;

/**
 * Technique : Triplets nus (Naked Triples)
 * Objectif : Réduire le nombre d’annotations dans un carré, une ligne ou une colonne.
 * Détection : 
 * - Si dans un bloc (carré, ligne ou colonne), il y a 3 cases contenant exactement 3 chiffres possibles, formant ainsi une combinaison unique (ex : {1,2,3}, {1,2}, {2,3}).
 * - Ces chiffres n'apparaissent pas ailleurs dans ce bloc.
 * - On peut alors supprimer ces 3 chiffres des annotations des autres cases du même bloc.
 */
public class NakedTriples implements InterfaceTech {

    private int[] donnerTriple(boolean[] tabBool){
        int[] tab = new int[3];
        int j = 0;
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab[j] = i;
                j++;
            }
        }
        return tab;
    }

    private boolean detectTripletsCarre(int num,Grid grille) {

        //tableau de triplets
        int[][] tabTriple = new int[9][3];
        int cptTriple = 0;

        int cptAnnotation = 0;
        
        // On récupère les cellules de la région
        int[] indiceCell = grille.numToPosForSubGrid(num);
        Cell mat[][] = grille.getSubGrid(indiceCell[0], indiceCell[1]);

        //tableau de booléen pour les annotations
        boolean[] tabBool = new boolean[9];

        // On récupère les triplets de la région
        for (int i =0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
                if (mat[i][j].getAnnotations().length == 3){

                    for (int k = 0; k<9;k++){
                        if(mat[i][j].getAnnotations()[k]){
                            cptAnnotation++;
                        }
                    }
                    if (cptAnnotation == 3) {
                        tabBool = mat[i][j].getAnnotations();
                        tabTriple[cptTriple] = donnerTriple(tabBool);
                        cptTriple++;                      

                
                        
                        }
                    }
                }
            }

    

    


        return false;
    }

    private boolean detectTriplets(int num,Grid grille){
        return false;
    }

    @Override
    public boolean detect(Grid grille) {
        System.out.println("detect");
        for(int i = 0; i<9;i++){
            if(detectTripletsCarre(i,grille) || detectTriplets(i,grille)){
                return true;
            }
        }
        return false;        
    }

    @Override
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique des triplets nus
    }

    public static void main(String[] args) {
        // Exemple de grille où un triplet nu peut être trouvé
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

        NakedTriples nakedTriples = new NakedTriples();
        System.out.println(nakedTriples.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.NakedTriples"
    }
}
