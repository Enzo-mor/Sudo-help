package grp6.syshelp;

import grp6.sudocore.*;

import java.util.ArrayList;
/**
 * Technique : Singleton caché (Hidden Single)
 * Objectif : Trouver une case où un chiffre ne peut apparaître qu’à un seul endroit dans un bloc (ligne, colonne ou carré).
 * Détection :
 * - Pour chaque chiffre (1 à 9), vérifier dans un bloc (ligne, colonne ou carré).
 * - Si le chiffre n’apparaît qu’en annotation dans une seule case de ce bloc, alors cette case doit obligatoirement contenir ce chiffre.
 */
public class HiddenSingle implements InterfaceTech {

    @Override
    public boolean detect(Grid grille) {    
        int[] tab;
        int[] tab_line, tab_column, tab_square;
        for(int i=0; i<Grid.NB_NUM; i++){
            tab = grille.numToPosForSubGrid(i);
  
            tab_line=nb_Num_Annotations(grille.getLine(i));
            tab_column=nb_Num_Annotations(grille.getColumn(i));
            tab_square=nb_Num_Annotations(grille.getFlatSubGrid(tab[0], tab[1]));   
            
            for(int j=0; j<Grid.NB_NUM; j++){
                if(tab_line[j]==1 || tab_column[j]==1 || tab_square[j]==1){
                    return true;
                }
            }
            
        }
        return false;
    }

    private int[] nb_Num_Annotations(Cell[] tab){
        int[] compteur;
        compteur=new int[Grid.NB_NUM+1];
        
        for(int i=0;i<Grid.NB_NUM;i++){
            for(int j=0;j<Grid.NB_NUM;j++){
                if(tab[i].getAnnotationsBool()[j]==true){
                    compteur[j]++;
                    compteur[Grid.NB_NUM]=j-1;
                }
            }
            
        }

        return compteur;
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
                    commandes.add(new CommandeApplique(grille.getLine(i)[tab_line[Grid.NB_NUM]], j));
                }
                if(tab_column[j]==1 ){
                    commandes.add(new CommandeApplique(grille.getColumn(i)[tab_column[Grid.NB_NUM]], j));
                }
                if (tab_square[j]==1){
                    int temp = tab_square[Grid.NB_NUM];
                    commandes.add(new CommandeApplique(grille.getSubGrid(tab[0], tab[1])[temp/3][temp%3], j));
                }
            }
            
        }
        commandes.forEach(c -> c.executer());
        
    }


    public static void main(String[] args) {
        // Exemple de grille où un singleton caché peut être trouvé
        int[] data = {
            0, 0, 9, 0, 3, 2, 0, 0, 0,
            0, 0, 0, 7, 0, 0, 0, 0, 0,
            1, 6, 2, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 0, 0, 2, 0, 5, 6,
            0, 0, 0, 0, 9, 0, 0, 0, 0,
            0, 5, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 2, 6, 0, 0, 0, 0, 0, 0,
            0, 0, 5, 8, 0, 7, 0, 0, 0
        };
        Grid grille = new Grid(data);
        System.out.println(grille.toString());

        // Ajout d'annotations sur les cellules vides (indices connus)
        addAnnotations(grille.getCell(6, 0), new int[]{7, 8, 9});  // Seul 9 possible
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
        addAnnotations(grille.getCell(5, 4), new int[]{3, 4, 7, 8});

      
        System.out.println();

        for(int i=0; i<9;i++){
            System.out.println(grille.getCell(8, 1).getAnnotationsBool()[i]);
        }

        HiddenSingle hiddenSingle = new HiddenSingle();
        System.out.println(hiddenSingle.detect(grille));
        hiddenSingle.applique(grille);

        for(int i=0; i<9;i++){
            System.out.println(grille.getCell(8, 1).getAnnotationsBool()[i]);
        }

        // Commande pour exécuter
        // mvn compile exec:java -Dexec.mainClass="com.grp6.HiddenSingle"
    }

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
                System.out.println(this.cellule.getAnnotations());
                System.out.println(this.cellule.getPosition()[0] + " " + this.cellule.getPosition()[1]);
                if(i!=this.val){
                    System.out.println(this.cellule.getNumber());
                    cellule.removeAnnotation(i);
                }
            }
        }


    }
}
