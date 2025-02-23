package com.example;

/**
 * Exception levée lorsque la cellule attendu n'est pas modifiable
 * non modifiable.
 * @author Taise de Thèse
 * @version 1.0
 * @see ReadOnlyCell
 * @see Cell
 */
public class NoEditableCellExeception extends RuntimeException {
    public NoEditableCellExeception(String message) {
        super(message);
    }
    
}