package grp6.syshelp;

import grp6.sudocore.*;

/**
 * Technique : Triplets nus (Naked Triples)
 * Objectif : Réduire le nombre d’annotations dans un carré, une ligne ou une colonne.
 * Détection : 
 * - Si dans un bloc (carré, ligne ou colonne), il y a 3 cases contenant exactement 3 chiffres possibles, formant ainsi une combinaison unique (ex : {1,2,3}, {1,2}, {2,3}).
 * - Ces chiffres n'apparaissent pas ailleurs dans ce bloc.
 * - On peut alors supprimer ces 3 chiffres des annotations des autres cases du même bloc.
 */
public class NakedTriples implements InterfaceTech {

    private int[] remplirTableau(boolean[] tabBool){
        int[] tab = new int[9];
        int j = 0;
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab[j] = i;
                j++;
            }
        }
        return tab;
    }

    private boolean detectTripleCarre(int num,Grid grille) {

        //tableau de pairs
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
                        //tabTriple[cptTriple] = donnerTriple(tabBool);
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
        return false;
    }

    // traitement 0 : on récupère les annotations des lignes et colonnes
    // traitement 1 : retitré les annotations qui plus que 3 chiffres
    // traitement 2 : mettre dans un tableau les triplets 
    // traitement 3 : verifier si les autres tuples du tableau sont utiliser dans le triplets (tous les chiffres du tuples )
    // traitement 4 : si oui on retourne true sinon false
    private boolean detectTriple(int num,Grid grille){

        //tableau de pairs
        int [][] tabTripleCol = new int[9][3];
        int [][] tabTripleLine = new int[9][3];
        int indiceTab = 0;

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
                if(col[i].getAnnotationsBool()[k]){
                    tabBool = line[i].getAnnotationsBool();
                    tabTripleCol[indiceTab] = remplirTableau(tabBool);
                    indiceTab++;
                    
                }
                if(line[i].getAnnotationsBool()[k]){
                    tabBool = line[i].getAnnotationsBool();
                    tabTripleLine[indiceTab] = remplirTableau(tabBool);
                    indiceTab++;
                }
            }
          
            
                    
            
        }
        // On vérifie si on a trouvé un triplet
        /*for(int i = 0; i<9;i++){
            int[] temp = tabTriple[i];
            System.out.println("tabTemp["+i+"] : "+temp[0]+" "+temp[1]+" "+temp[2]);
            System.out.println("tabTriple["+i+"] : "+tabTriple[i][0]+" "+tabTriple[i][1] +" " +tabTriple[i][2]);
            for(int j = 0; j<9;j++){
                //if de la violence (désolée)
                if(temp[0] == tabTriple[j][0]  && temp[1] == tabTriple[j][1] && temp[2] == tabTriple[j][2] && temp[0] !=0 && i!=j){
                    System.out.println("true de ligne et colonne");
                    return true;
                }
            }
        }*/
        return false;
    }

    @Override
    public boolean detect(Grid grille) {
        System.out.println("detect");
        for(int i = 0; i<9;i++){
            if(detectTripleCarre(i,grille) || detectTriple(i,grille)){
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
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,9,3,0,0,0,0,
            0,0,0,5,4,0,0,0,0,
            0,0,0,0,8,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,
            0,0,0,4,0,0,0,0,0
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());

  

        //annotations pour les colonnes
        grille.getCell(0, 3).addAnnotation(2);
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

        grille.getCell(5, 3).addAnnotation(2);
        grille.getCell(5, 3).addAnnotation(6); 
        grille.getCell(5, 3).addAnnotation(7);

        grille.getCell(6, 3).addAnnotation(1);
        grille.getCell(6, 3).addAnnotation(2);
        grille.getCell(6, 3).addAnnotation(6);
        grille.getCell(6, 3).addAnnotation(7);

        grille.getCell(7, 3).addAnnotation(2);
        grille.getCell(7, 3).addAnnotation(6);
        grille.getCell(7, 3).addAnnotation(7);

        NakedTriples nakedTriple = new NakedTriples();
        System.out.println(nakedTriple.detect(grille));

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.NakedTriples"
    }
}
