package grp6.sudocore;

/**
 * cette classe permet de definir les types de données utilisées dans le jeu de sudoku
 * elle permet de definir les types d'actions, les types de cellules, les niveaux de difficulté et les etats du jeu etc..
 */
public final class SudoTypes {

    /**
     * Enum pour les types d'actions
     */
    public enum ActionType {
        /**
         * Enum pour les actions sur les cellules de type nombre
         */
        NUMBER_CELL_ACTION("Action pour nombre"), 
        /**
         * Enum pour les actions d'ajout d'annotation sur les cellules
         */
        ANNOTATION_CELL_ACTION("Action pour les annotations"), 
        /**
         * Enum pour les actions sur le jeu de type bonus
         */
        BONUS_ACTION("bonusCell"),
        /**
         * Enum pour les actions de suppression d'annotation sur les cellules
         */
        ANNOTATION_REMOVE_CELL_ACTION("action de suppression de l'annotation"),
        /**
         * 
         */
        ERASE_ACTION("action d'effacement de la cellule");

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
        /**
         * Enum pour les cellules de type fixe
         */
        FIXED_CELL, 
        /**
         * Enum pour les cellules de type flexible
         */
        FLEXIBLE_CELL;
    }

    /**
     * Enum pour les niveaux de difficulté
     * les niveaux de difficulté sont : facile, moyen et difficile et sont representés par les valeurs 1, 2 et 3
     */
    public enum Difficulty {
        /**
         * Enum pour le niveau de difficulté facile
         */
        EASY(1,"facile"), 
        /**
         * Enum pour le niveau de difficulté moyen
         */
        MEDIUM(2,"moyen"),
        /**
         * Enum pour le niveau de difficulté difficile
         */
         HARD(3,"difficile");

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
        /**
         * Enum pour l'etat du jeu non commencé ou en arrêt
         */
        NOT_STARTED("jeu est en arrêt"),
        /**
         * Enum pour l'etat du jeu en cours
         */
         IN_PROGRESS("jeu encours"),
        /**
         * Enum pour l'etat du jeu en pause
         */
          PAUSED("jeu en pause"), 
        /**
         * Enum pour l'etat du jeu terminé
         * le jeu est terminé lorsque le joueur a rempli toutes les cellules du jeu
         */
          FINISHED("jeu terminer");

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
