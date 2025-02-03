package com.example;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DBManager.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Grid g = DBManager.getGrid(4);
        System.out.println(g);
    }
}
