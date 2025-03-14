package com.grp6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Technique : Triplets nus (Naked Triples)
 * Objectif : Réduire le nombre d’annotations dans un carré, une ligne ou une colonne.
 * Détection : 
 * - Si dans un bloc (carré, ligne ou colonne), il y a 3 cases contenant exactement 3 chiffres possibles, formant ainsi une combinaison unique (ex : {1,2,3}, {1,2}, {2,3}).
 * - Ces chiffres n'apparaissent pas ailleurs dans ce bloc.
 * - On peut alors supprimer ces 3 chiffres des annotations des autres cases du même bloc.
 */
public class NakedTriples implements InterfaceTech {
    

    private ArrayList<Integer> remplirList(boolean[] tabBool){
        ArrayList<Integer> tab = new ArrayList<Integer>();
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab.add(i+1);
            }
        }
        return tab;
    }

    private int[] remplirTableau(boolean[] tabBool){
        int[] tab = new int[9];
        int j = 0;
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab[j] = i+1;
                j++;
            }
        }
        return tab;
    }

    private boolean moinQueTrois(int[] tab){
        int cpt = 0;
        for(int i = 0; i<9;i++){
            if(tab[i] != 0){
                cpt++;
            }
        }
        if(cpt < 3){
            return true;
        }
        return false;

       
    }

    private boolean detectTripleCarre(int num,Grid grille) {

       /* //tableau de pairs
        int[][] tabTriple = new int[9][3];
        int cptTriple = 0;

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
             //   if (mat[i][j].getAnnotationsBool().length == 2){

                    for (int k = 0; k<9;k++){
                       // System.out.println("Annotation de la case  "+ i +" "+j+ " et de annotation : "+ k+" :"+ mat[i][j].getAnnotationsBool()[k]);
                        if(mat[i][j].getAnnotationsBool()[k]){
                            cptAnnotation++;
                        }
                    }
                    if (cptAnnotation == 3) {
                        tabBool = mat[i][j].getAnnotationsBool();
                        tabTriple[cptTriple] = donnerTriple(tabBool);
                        cptTriple++;                      
                        
                        }
                    }
            }
        
        //  System.out.println("verif tabTriple");
        for(int i = 0; i<9;i++){
            int[] temp = tabTriple[i];
            //affichage du tableau temporaire
         //   System.out.println("tabTemp["+i+"] : "+temp[0]+" "+temp[1]+" "+temp[2]);
        //  System.out.println("tabTriple["+i+"] : "+tabTriple[i][0]+" "+tabTriple[i][1] +" "+tabTriple[i][2]);
            for(int j = 0; j<9;j++){
                //if de la violence (désolée)
                if(temp[0] == tabTriple[j][0]  && temp[1] == tabTriple[j][1] && temp[2] == tabTriple[j][2] && temp[0] !=0 && i!=j){
                    System.out.println("true de région");

                    return true;
                }
            }
        }
            */
        return false;
    }

    // traitement 0 : on récupère les annotations des lignes et colonnes
    private boolean detectTriple(int num,Grid grille){

        //tableau des annotations des colonnes et des lignes
        ArrayList<List<Integer>> tabCol = new ArrayList<List<Integer>>();
        ArrayList<List<Integer>> tabLine = new ArrayList<List<Integer>>();

        System.out.println("test : "+tabLine);

        // On récupère les cellules dans les lignes et colonnes
        Cell line[] = grille.getLine(num);
        Cell col[] = grille.getColumn(num);

        //tableau de triple
        ArrayList<List<Integer>> tabTriple = new ArrayList<List<Integer>>();

        //Convertion des annotations en tableau d'entier
        //récupération des annotations 
        for(int i= 0; i<9;i++){
            tabCol.add(remplirList(col[i].getAnnotationsBool()));
            tabLine.add(remplirList(line[i].getAnnotationsBool()));
        }



        //affichage des annotations
        //validé
        for(int i = 0; i<9;i++){
            System.out.println("tabCol["+i+"] : "+tabCol.get(i));
            System.out.println("tabLine["+i+"] : "+tabLine.get(i));
        }

         // traitement 1 : retitré les annotations qui on moin que 3 chiffres
        //validé
        for(int i=0;i<9;i++){

            if(tabCol.get(i) != null){
                System.out.println("Taille du tableau col "+i+" : "+tabCol.get(i).size());
                if(tabCol.get(i).size() > 0 && tabCol.get(i).size() <= 3){
                    tabTriple.add(tabCol.get(i));
                }
            }

            if(tabLine.get(i)!= null ){
                if (tabLine.get(i).size() >0 && tabLine.get(i).size() <= 3){
                    tabTriple.add(tabLine.get(i));
                    
                }
            }
        }
        
        //affichage des triplets
        //validé
        for(int i = 0; i<tabTriple.size();i++){
            System.out.println("tabTriple["+i+"] : "+tabTriple.get(i));
        }

        //traitement 2 : mettre dans un tableau les triplets
        List<Integer> tabTriplets = new ArrayList<Integer>();
        
        for(int i = 0; i<tabTriple.size();i++){
            if (tabTriple.get(i).size() == 3){
                for(int j = 0; j<tabTriple.get(i).size();j++){
                    if(!tabTriplets.contains(tabTriple.get(i).get(j))){
                        tabTriplets.add(tabTriple.get(i).get(j));
                    }
                }
            }
        }

        System.out.println("tabTriplets : "+tabTriplets);

        ArrayList<List<Integer>> tabCopy = tabTriple;

        // traitement 3 : verifier si les autres tuples du tableau sont utiliser dans le triplets
        /*for(int i =0;i<tabTriple.size();i++){
            for(int j=tabTriple.get(i).size()-1;j>-1;j--){
                if(tabTriplets.contains(tabTriple.get(i).get(j)) ){
                    tabCopy.get(i).remove(j);
                }
            }
        }
        */

        //conter le nombre de differente valeur dans le tableau
        List<Integer> ocurance = new ArrayList<Integer>();
        for(int i = 0; i<tabTriple.size();i++){
            for(int j = 0; j<tabTriple.get(i).size();j++){
                if(!ocurance.contains(tabTriple.get(i).get(j))){
                    ocurance.add(tabTriple.get(i).get(j));
                    
                }
            }
        }

        System.out.println("ocurance : "+ocurance);

        if (ocurance.size() == 3){
            return true;
            
        }

        return false;
    }

    @Override
    public boolean detect(Grid grille) {
        /*for(int i = 0; i<9;i++){
            if(detectTripleCarre(i,grille) || detectTriple(i,grille)){
                return true;
            }
        }*/
        if(detectTriple(3,grille)){
            return true;
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
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,9,3,0,0,0,0,
            0,0,0,5,4,0,0,0,0,
            0,0,0,0,8,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());

  

        //annotations pour les colonnes OK
        grille.getCell(0, 3).addAnnotation(1);
        grille.getCell(0, 3).addAnnotation(6);
        grille.getCell(0, 3).addAnnotation(7);

        grille.getCell(1, 3).addAnnotation(1);
        grille.getCell(1, 3).addAnnotation(2);
        grille.getCell(1, 3).addAnnotation(3);
        grille.getCell(1, 3).addAnnotation(6);
        grille.getCell(1, 3).addAnnotation(7);
        grille.getCell(1, 3).addAnnotation(8);

        grille.getCell(2, 3).addAnnotation(1);
        grille.getCell(2, 3).addAnnotation(2);
        grille.getCell(2, 3).addAnnotation(3);
        grille.getCell(2, 3).addAnnotation(7);
        grille.getCell(2, 3).addAnnotation(8);

        grille.getCell(5, 3).addAnnotation(1);
        grille.getCell(5, 3).addAnnotation(6); 
        grille.getCell(5, 3).addAnnotation(7);

        grille.getCell(6, 3).addAnnotation(1);
        grille.getCell(6, 3).addAnnotation(2);
        grille.getCell(6, 3).addAnnotation(6);
        grille.getCell(6, 3).addAnnotation(7);

        grille.getCell(7, 3).addAnnotation(1);
        grille.getCell(7, 3).addAnnotation(6);
        grille.getCell(7, 3).addAnnotation(7);

        /*grille.getCell(0, 3).addAnnotation(1);
        grille.getCell(0, 3).addAnnotation(3);
        grille.getCell(0, 3).addAnnotation(7);
        grille.getCell(0, 3).addAnnotation(8);
        grille.getCell(0, 3).addAnnotation(9);
        
        grille.getCell(1, 3).addAnnotation(3);
        grille.getCell(1, 3).addAnnotation(5);
        grille.getCell(1, 3).addAnnotation(7);
        grille.getCell(1, 3).addAnnotation(8);
        grille.getCell(1, 3).addAnnotation(9);

        grille.getCell(2, 3).addAnnotation(1);
        grille.getCell(2, 3).addAnnotation(5);
        grille.getCell(2, 3).addAnnotation(7);
        grille.getCell(2, 3).addAnnotation(8);
        grille.getCell(2, 3).addAnnotation(9);

        grille.getCell(6, 3).addAnnotation(3);
        grille.getCell(6, 3).addAnnotation(7);

        grille.getCell(7, 3).addAnnotation(5);
        grille.getCell(7, 3).addAnnotation(3);

        grille.getCell(8, 3).addAnnotation(5);
        grille.getCell(8, 3).addAnnotation(7);*/
        

        NakedTriples nakedTriple = new NakedTriples();
        System.out.println(nakedTriple.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.NakedTriples"
    }
}
