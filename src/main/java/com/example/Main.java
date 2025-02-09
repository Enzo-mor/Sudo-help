package com.example;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DBManager.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Grid g = DBManager.getGrid(8);
        System.out.println(g);
        List<int[]> errors = g.evaluate();
        for(int[] err: errors) {
            System.out.print("(" + err[0] + ", " + err[1] + ") ");
        }
        System.out.println();
    }
}
