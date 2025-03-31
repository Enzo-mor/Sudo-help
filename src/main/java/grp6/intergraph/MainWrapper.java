package grp6.intergraph;

import java.io.PrintStream;

public class MainWrapper {
    public static void main(String[] args) throws Exception {
        System.setProperty("javafx.allowjs", "false");
        System.setOut(new PrintStream(new NullOutputStream()));
        System.setErr(new PrintStream(new NullOutputStream()));
        
        // Lance l'application principale
        Main.main(args); 
    }
}

class NullOutputStream extends java.io.OutputStream {
    @Override
    public void write(int b) {
        
    }
}
