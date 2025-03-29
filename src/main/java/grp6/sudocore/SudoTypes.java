package grp6.sudocore;

/**
 * Cette classe permet de definir les types de donnees utilises dans le jeu de sudoku.
 * Elle permet de definir les types d'actions, les types de cellules, les niveaux de difficulte, les etats du jeu, etc.
 * 
 * @author NGANGA YABIE Taïse de These
 * @author POUSSE Kilian
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
        ANNOTATION_REMOVE_CELL_ACTION("Action de suppression de l'annotation"),

        /**
         * Enum pour les actions d'effacement de la cellule
         */
        ERASE_ACTION("Action d'effacement de la cellule");

        /**
         * Description textuelle de l'action.
         */
        private final String description;
        
        /**
         * Constructeur de l'enumeration.
         * 
         * @param value La description associee à l'action.
         */
        ActionType(String value) {
            this.description = value;
        }

        /**
         * Retourne une représentation textuelle de l'action.
         *
         * @return La description de l'action.
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         * Methode pour retourner la valeur de l'enum
         * @return La valeur de l'enum
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
     * Enum pour les niveaux de difficulte.
     * Les niveaux de difficulte sont : facile, moyen et difficile, representes par les valeurs 1, 2 et 3.
     */
    public enum Difficulty {
        /**
         * Enum pour le niveau de difficulte facile
         */
        EASY(1, "facile"), 
        /**
         * Enum pour le niveau de difficulte moyen
         */
        MEDIUM(2, "moyen"),
        /**
         * Enum pour le niveau de difficulte difficile
         */
        HARD(3, "difficile");

        /**
         * Niveau de difficulte associe a l'enum.
         */
        private final int level;

        /**
         * Description textuelle de l'enum.
         */
        private final String description;

        /**
         * Constructeur de l'enumeration.
         * 
         * @param level Le niveau de difficulte.
         * @param description La description associée à l'enum.
         */
        Difficulty(int level, String description) {
            this.level = level;
            this.description = description;
        }

        /**
         * Permet d'obtenir la description de l'enum.
         * @return La description correspondant à l'enum.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Permet d'obtenir le niveau associe à l'enum.
         * @return Le niveau de difficulte.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Retourne une représentation textuelle de l'enum.
         *
         * @return La description de l'enum.
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         * Retourne la difficulte correspondant à un niveau specifique.
         * @param level Le niveau de difficulte (1, 2, 3).
         * @return La difficulte correspondant au niveau.
         * @throws IllegalArgumentException Si le niveau est invalide.
         */
        public static Difficulty fromLevel(int level) {
            for (Difficulty difficulty : Difficulty.values()) {
                if (difficulty.getLevel() == level) {
                    return difficulty;
                }
            }
            throw new IllegalArgumentException("Niveau invalide : " + level);
        }

        /**
         * Retourne la difficulte correspondant à une description donnee.
         * @param description La description de la difficulte.
         * @return La difficulte correspondant à la description.
         * @throws IllegalArgumentException Si la description est invalide.
         */
        public static Difficulty fromDescription(String description) {
            for (Difficulty difficulty : Difficulty.values()) {
                if (difficulty.getDescription().equals(description)) {
                    return difficulty;
                }
            }
            throw new IllegalArgumentException("Description invalide : " + description);
        }
    }

    /**
     * Enum pour les etats du jeu.
     */
    public enum GameState {
        /**
         * Enum pour l'etat du jeu non commence.
         */
        NOT_STARTED("Jeu est en arret"),
        /**
         * Enum pour l'etat du jeu en cours.
         */
        IN_PROGRESS("Jeu en cours"),
        /**
         * Enum pour l'etat du jeu en pause.
         */
        PAUSED("Jeu en pause"), 
        /**
         * Enum pour l'etat du jeu termine.
         * Le jeu est termine lorsque le joueur a rempli toutes les cellules du jeu.
         */
        FINISHED("Jeu termine");

        /**
         * Description textuelle de l'etat du jeu.
         */
        private final String description;

        /**
         * Constructeur de l'enumeration.
         * 
         * @param description La description associée à l'etat du jeu.
         */
        GameState(String description) {
            this.description = description;
        }

        /**
         * Retourne une représentation textuelle de l'etat du jeu.
         *
         * @return La description de l'etat du jeu.
         */
        @Override
        public String toString() {
            return this.description;
        }

        /**
         * Permet d'obtenir la description de l'etat du jeu.
         * @return La description de l'etat du jeu.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Permet d'obtenir le nom de l'etat du jeu sous forme de chaine de caracteres.
         * @return Le nom de l'etat du jeu.
         */
        public String getName() {
            switch (this) {
                case NOT_STARTED:
                    return "NOT_STARTED";
                case IN_PROGRESS:
                    return "IN_PROGRESS";
                case PAUSED:
                    return "PAUSED";
                case FINISHED:
                    return "FINISHED";
                default:
                    return "UNKNOWN";
            }
        }
    }

    /**
     * Méthode principale illustrant l'utilisation des énumérations du jeu.
     * Cette méthode sélectionne différents types d'actions, cellules, niveaux de difficulté 
     * et états du jeu, puis les affiche dans la console.
     * 
     * @param args Arguments de la ligne de commande (non utilisés).
     */
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
