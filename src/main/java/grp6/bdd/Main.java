package grp6.bdd;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DBManager.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Grid g = DBManager.getGrid(8);
        System.out.println(g);
    }
}
