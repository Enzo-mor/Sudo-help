package grp6.syshelp;

import java.util.ArrayList;
import java.util.List;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;
/**
 * Cette classe représente la technique NakedTriples.
 * 
 * Elle permet de trouver les triplets nus(3 chiffres apparaissant uniquement dans trois cases d'une unité (ligne colones carré)). 
 * @author GRAMMONT Dylan
 * @see InterfaceTech Contenant les méthodes des techniques
 * 
 */
public class NakedTriples implements InterfaceTech{
        Help aide = new Help(getClass().getSimpleName());


    private ArrayList<Integer> remplirList(boolean[] tabBool){
        ArrayList<Integer> tab = new ArrayList<Integer>();
        for(int i = 0; i<9;i++){
            if(tabBool[i]){
                tab.add(i+1);
            }
        }
        return tab;
    }   
  
/**
     * Permet de détecter les triples dans un carré
     * @param num
     * @param grille
     * @return boolean si on a trouvé une triples dans un carré
     */
    private boolean detectTripleCarre(int num,Grid grille) {

      
        // On récupère les cellules de la région
        int[] indiceCell = grille.numToPosForSubGrid(num);
        Cell mat[][] = grille.getSubGrid(indiceCell[0], indiceCell[1]);
        ArrayList<List<Integer>> tabCarre = new ArrayList<List<Integer>>();
        ArrayList<List<Integer>> tabTriple = new ArrayList<List<Integer>>();




        // On récupère les annotations de la région
        for (int i =0; i<3 ;i++){
            for (int j = 0; j<3 ;j++){
                tabCarre.add(mat[i][j].getAnnotations());
            }
        }

        //traitement 1 : retitré les annotations qui on moin que 3 chiffres
        for(int i=0;i<9;i++){
            if(tabCarre.get(i).size() > 0 && tabCarre.get(i).size() <= 3){
                tabTriple.add(tabCarre.get(i));
            }
        }

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
                aide.addSquare(num);
                aide.setMessage(1, "Fait attention aux carrés");
                aide.setMessage(2, "Fait attention aux "+ocurance.get(0)+" "+ocurance.get(1)+" "+ocurance.get(2));
                aide.setMessage(3, "Regarde les annotations"+ocurance.get(0)+" "+ocurance.get(1)+" "+ocurance.get(2)+"le carré : "+num);
               return true;
               
           }
   
     
        return false;
    }

/**
     * Permet de détecter les triples dans une ligne ou une colonne
     * @param num
     * @param grille
     * @return boolean si on a trouvé une triples dans une ligne ou une colonne
     */    private boolean detectTriple(int num,Grid grille){

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
            aide.addSquare(num);
                aide.setMessage(1, "Fait attention aux lignes et colonnes");
                aide.setMessage(2, "Fait attention aux "+ocurance.get(0)+" "+ocurance.get(1)+" "+ocurance.get(2));
                aide.setMessage(3, "Regarde les annotations"+ocurance.get(0)+" "+ocurance.get(1)+" "+ocurance.get(2)+" et la ligne/colone : "+num);
            return true;
            
        }

        return false;
    }


    @Override
    public Help getHelp(Grid grille) {
        for(int i = 0; i<9;i++){
            if(detectTripleCarre(i,grille) || detectTriple(i,grille)){
                return aide;
            }
        }
        return null;
    }
    
}