package grp6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuFrame().setVisible(true));
    }
}

class SudokuFrame extends JFrame {
    private final JTextField[][] cells;

    public SudokuFrame() {
        setTitle("Jeu de Sudoku");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 9));
        cells = new JTextField[9][9];

        // Créer une grille de cases de Sudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.PLAIN, 20));
                cell.setPreferredSize(new Dimension(40, 40));
                panel.add(cell);
                cells[i][j] = cell;
            }
        }

        add(panel, BorderLayout.CENTER);

        // Exemple : remplir certaines cases pour visualiser une grille de départ
        initializeSudoku();
    }

    private void initializeSudoku() {
        // Remplir quelques cases avec des valeurs prédéfinies
        cells[0][0].setText("5");
        cells[1][1].setText("3");
        cells[4][4].setText("7");
        cells[8][8].setText("9");
    }
}
