package com.grp6;

public interface SousGrille  {
     
    public boolean complete();
    public Cell[] emptyCell();
    public Cell[] fullCell();

    public int[][] emptyCellPos();
    public int[][] fullCellPos();

}
