package mainpackage;

import cells.Cell;
import cells.StateChangeCell;

import java.util.ArrayList;
import java.util.List;

public abstract class Grid {
    private Cell[][] myCellArray;

    public Grid(int rows, int columns, List<Cell> list){
        myCellArray = getNewGrid(rows, columns, list);
    }

    protected List<Cell> getImmediateNeighbors(Cell cell, Cell[][] myGrid){
      return new ArrayList<Cell>();
    }

    protected List<Cell> getAllNeighbors(Cell cell, Cell[][] myGrid){
        return new ArrayList<Cell>();
    }

    protected Cell[][] getNewGrid(int rows, int columns, List<Cell> list){
        Cell[][] newGrid = new Cell[rows][columns];
        for(Cell cell : list){
            newGrid[cell.getRow()][cell.getColumn()] = cell;
        }
        return newGrid;
    }


    protected List<Cell> getTypedNeighbors(Cell cell, String type) {
        List<Cell> neighbors = getNeighbors(cell);
        List<Cell> specificNeighbors=new ArrayList<Cell>();
        for(Cell neighbor: neighbors){
            if (((StateChangeCell) neighbor).getState().equals(type)) specificNeighbors.add(neighbor);
        }
        return specificNeighbors;
    }

    protected Cell getCell(int row, int column){
        return myCellArray[row][column];
    }

    protected int getHeight(){
        return myCellArray.length;
    }

    protected int getWidth(){
        return myCellArray[0].length;
    }


}
