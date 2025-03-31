package grp6.syshelp;

import grp6.sudocore.Cell;
import grp6.sudocore.Grid;

/**
 * Cette classe represente la technique du singleton cache.
 * Elle permet de savoir si dans une ligne, une colonne ou un carre on peut enlever des annotations.
 * 
 * @author DUBOIS Gabriel
 * @see InterfaceTech Contenant les methodes des techniques
 * @see Help
 * @see Grid
 */
public class HiddenSingle implements InterfaceTech {

    /**
     * Constructeur de la classe HiddenSingle
     */
    public HiddenSingle(){}

    /**
     * Classe implementant la technique du singleton cachees dans une grille Sudoku.
     * Cette technique permet d'identifier des annotations cachees dans une ligne, 
     * une colonne ou une sous-grille.
     * @param grille Grille sur laquelle chercher l'aide
     * @return l'aide HiddenSingle
     */
    @Override
    public Help getHelp(Grid grille) {    

        Help aide = new Help(getClass().getSimpleName());

        int[] tab;
        int[] tab_line, tab_column, tab_square;
        for(int i=0; i<Grid.NB_NUM; i++){
            tab = grille.numToPosForSubGrid(i);

            tab_line=nb_Num_Annotations(grille.getLine(i));
            tab_column=nb_Num_Annotations(grille.getColumn(i));
            tab_square=nb_Num_Annotations(grille.getFlatSubGrid(tab[0], tab[1]));   
            
            for(int j=0; j<Grid.NB_NUM; j++){
                
                if(tab_line[j]==1 ){
                    aide.addLine(i);
                    aide.setMessage(1,"Regarde dans tes annotations tu peux en enlever");
                    
                    aide.setMessage( 3,"Applique le sur cette ligne");
                    return aide;
                }if( tab_column[j]==1 ){
                    aide.addColumn(i);
                    aide.setMessage(1,"Regarde dans tes annotations tu peux en enlever");
                    
                    aide.setMessage( 3,"Applique le sur cette colonne");
                    return aide;
                } if (tab_square[j]==1){
                    aide.addSquare(i);
                    aide.setMessage(1,"Regarde dans tes annotations tu peux en enlever");
                    
                    aide.setMessage( 3,"Applique le sur ce carré");
                    return aide;

                }
            }
            
        }
        return null;
    }

    /**
     * Permet de savoir combien d'occurences de chiffre il y a dans le tableau de cellules
     * @param tab tableau de cellules
     * @return un tableau du nombre d'occurences de chaque chiffre
     */
    private int[] nb_Num_Annotations(Cell[] tab){
        int[] compteur;
        compteur=new int[Grid.NB_NUM+1];
        
        for(int i=0;i<Grid.NB_NUM;i++){
            for(int j=0;j<Grid.NB_NUM;j++){
                if(tab[i].getAnnotationsBool()[j]==true){
                    compteur[j]++;
                }
            }
           // System.out.println("compteur"+tab[i].getAnnotations());
        }

        for(int i=0;i<Grid.NB_NUM;i++){
            if(compteur[i]==1){
                for(int k=0;k<Grid.NB_NUM;k++){
                    for(int j=0;j<Grid.NB_NUM;j++){
                        if(tab[k].getAnnotationsBool()[j]==true && j == i){
                            compteur[Grid.NB_NUM]=k;
                        }
                    }
                }
            }
            
        }
        return compteur;
    }

    /*private static void addAnnotations(Cell cell, int[] numbers) {
        if (cell instanceof FlexCell) {
            for (int num : numbers) {
                ((FlexCell) cell).addAnnotation(num);
            }
        }
    }*/
    /*
    
    public void applique(Grid grille) {
        // TODO: Implémenter l'application de la technique du singleton caché
        ArrayList<Commande> commandes = new ArrayList<>();

        
        int[] tab;
        int[] tab_line, tab_column, tab_square;
        for(int i=0; i<Grid.NB_NUM; i++){
            tab = grille.numToPosForSubGrid(i); 
  
            tab_line=nb_Num_Annotations(grille.getLine(i));
            tab_column=nb_Num_Annotations(grille.getColumn(i));
            tab_square=nb_Num_Annotations(grille.getFlatSubGrid(tab[0], tab[1])); 

            
            for(int j=0; j<Grid.NB_NUM; j++){
                if(tab_line[j]==1 ){
                    commandes.add(new CommandeApplique(grille.getLine(i)[tab_line[Grid.NB_NUM]], j+1));
                }
                if(tab_column[j]==1 ){
                    commandes.add(new CommandeApplique(grille.getColumn(i)[tab_column[Grid.NB_NUM]], j+1));
                }
                if (tab_square[j]==1){
                    System.out.println("carre");
                    int temp = tab_square[Grid.NB_NUM];
                    commandes.add(new CommandeApplique(grille.getSubGrid(tab[0], tab[1])[temp/3][temp%3], j+1));
                }
            }
            
        }
        commandes.forEach(c -> c.executer());
        
    }*/

    /**
     * Main pour tester la classe
     * @param args non utilise
     */
    public static void main(String[] args) {
        // Exemple de grille où un singleton caché peut être trouvé
        int[] data = {
            0, 0, 9, 0, 3, 2, 0, 0, 0,
            0, 0, 0, 7, 0, 0, 0, 0, 0,
            1, 6, 2, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 2, 0, 5, 6, 0,
            0, 0, 0, 9, 0, 0, 0, 0, 0,
            0, 5, 0, 0, 0, 0, 1, 0, 7,
            0, 0, 0, 0, 0, 0, 4, 0, 3,
            0, 2, 6, 0, 0, 9, 0, 0, 0,
            0, 0, 5, 8, 7, 0, 0, 0, 0
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());

        // Ajout d'annotations sur les cellules vides (indices connus)
       /* addAnnotations(grille.getCell(6, 0), new int[]{7, 8, 9});  
        addAnnotations(grille.getCell(6, 1), new int[]{7, 8, 9});
        addAnnotations(grille.getCell(6, 2), new int[]{1, 7, 8});
        addAnnotations(grille.getCell(6, 3), new int[]{1, 7, 8});
        addAnnotations(grille.getCell(7, 0), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(7, 3), new int[]{3, 4, 7, 8, 1});
        addAnnotations(grille.getCell(8, 0), new int[]{3, 4, 9});
        addAnnotations(grille.getCell(8, 1), new int[]{3, 4, 9});
        addAnnotations(grille.getCell(4, 1), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(4, 2), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(4, 3), new int[]{3, 4, 7, 8, 1});
        addAnnotations(grille.getCell(5, 2), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(5, 0), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(7, 4), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(7, 3), new int[]{3, 4, 7, 8});
        addAnnotations(grille.getCell(5, 3), new int[]{3, 4, 7, 8, 1});
        addAnnotations(grille.getCell(5, 4), new int[]{3, 4, 7, 8});*/

        
        AutoAnnotation.generate(grille);
        
        System.out.println();

        

        HiddenSingle hiddenSingle = new HiddenSingle();
        System.out.println(hiddenSingle.getHelp(grille));

        

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.HiddenSingle"
    }
    /*
    private class CommandeApplique extends Commande{
        private Cell cellule;
        private int val;

        public CommandeApplique(Cell cellule, int val) {
            this.cellule = cellule;
            this.val = val;
        }

        @Override
        public void executer() {
            for(int i =1 ; i< Grid.NB_NUM ;i++){
                if(i!=this.val){
                    cellule.removeAnnotation(i);
                }
            }
        }


    }*/
}