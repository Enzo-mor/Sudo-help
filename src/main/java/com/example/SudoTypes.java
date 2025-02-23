package com.example;

/**
 * cette classe permet de definir les types de données utilisées dans le jeu de sudoku
 * elle permet de definir les types d'actions, les types de cellules, les niveaux de difficulté et les etats du jeu
 */
public class SudoTypes {

    /**
     * Enum pour les types d'actions
     */
    public enum ActionType {
        NUMBER_CELL_ACTION("Action pour nombre"), ANNOTATION_CELL_ACTION("Action pour les annotations"), BONUS_ACTION("bonusCell");

        private final String description;
        ActionType(String value) {
            this.description = value;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return this.description;
        }
        /**
         * methode pour retourner la valeur de l'enum
         * @return la valeur de l'enum
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * Enum pour les types de cellules
     */
    public enum CellType {
        FIXED_CELL, FLEXIBLE_CELL;
    }

    /**
     * Enum pour les niveaux de difficulté
     * les niveaux de difficulté sont : facile, moyen et difficile et sont representés par les valeurs 1, 2 et 3
     */
    public enum Difficulty {
        EASY(1,"facile"), MEDIUM(2,"moyen"), HARD(3,"difficile");

        public String getDescription() {
            return description;
        }
        private final int level;
        private final String description;

        Difficulty(int level, String description) {
            this.level = level;
            this.description=description;
        }
        /**
         * methode pour retourner le niveau de l'enum
         * @return
         */
        public int getLevel() {
            return level;
        }
        public String toString() {
            return this.description;
        }

        // Méthode statique pour retrouver la difficulté depuis un niveau
        public static Difficulty fromLevel(int level) {
            for (Difficulty difficulty : Difficulty.values()) {
                if (difficulty.getLevel() == level) {
                    return difficulty;
                }
            }
            throw new IllegalArgumentException("Niveau invalide : " + level);
        }

        
        // Méthode statique pour retrouver la difficulté depuis sa description
        public static Difficulty fromDescription(String description) {
            for (Difficulty difficulty : Difficulty.values()) {
                if (difficulty.getDescription().equals(description)) {
                    return difficulty;
                }
            }
            throw new IllegalArgumentException("description invalide : " +description);
        }
    }

    /**
     * Enum pour les états du jeu
     */
    public enum GameState {
        NOT_STARTED("jeu est en arrêt"), IN_PROGRESS("jeu encours"), PAUSED("jeu en pause"), FINISHED("jeu terminer");
        private final String description;
        GameState(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return this.description;
        }
        /**
         * methode pour retourner la valeur de l'enum
         * @return la valeur de l'enum
         */
        public String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) {
        // Exemple d'utilisation
        ActionType action = SudoTypes.ActionType.NUMBER_CELL_ACTION;
        CellType cell = CellType.FIXED_CELL;
        Difficulty diff = Difficulty.HARD;
        GameState state = GameState.IN_PROGRESS;

        // Affichage des valeurs
        System.out.println("Action sélectionnée : " + action);
        System.out.println("Type de cellule : " + cell);
        System.out.println("Difficulté : " + diff + " (Niveau " + diff.getLevel() + ")");
        System.out.println("État du jeu : " + state);
    }
}
