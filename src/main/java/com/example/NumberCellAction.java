package com.example;

public class NumberCellAction implements Action  {

    private int x, y;
    private int number;
    private int old_number;
    private Grid grid;

    public NumberCellAction(Grid grid, int x, int y, int number, int old_number) {
        this.x = x;
        this.y = y;
        this.number = number;
        this.old_number = old_number;
        this.grid = grid;
    }

    @Override
    public void doAction() {
        grid.getCell(x, y).setNumber(number);
    }

    @Override
    public void undoAction() {
        grid.getCell(x, y).setNumber(old_number);
    }
    @Override
    public String actionType(){
        return "actionNombre";
    }
}
