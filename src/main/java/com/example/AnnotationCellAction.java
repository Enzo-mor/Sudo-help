package com.example;

public class AnnotationCellAction implements Action  {

    private int x, y;
    private int number;
    private Grid grid;

    public AnnotationCellAction(Grid grid, int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.grid = grid;
    }

    @Override
    public void doAction() {
        grid.getCell(x, y).addAnnotation(number);
    }

    @Override
    public void undoAction() {
        grid.getCell(x, y).removeAnnotation(number);
    }
    
}
