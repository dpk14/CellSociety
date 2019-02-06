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

    public List<Cell> getImmediateNeighbors(Cell cell){
      return new ArrayList<Cell>();
    }

    public List<Cell> getAllNeighbors(Cell cell){
        return new ArrayList<Cell>();
    }

    public Cell[][] getNewGrid(int rows, int columns, List<Cell> list){
        Cell[][] newGrid = new Cell[rows][columns];
        for(Cell cell : list){
            newGrid[cell.getRow()][cell.getColumn()] = cell;
        }
        return newGrid;
    }

    public Cell getCell(int row, int column){
        return myCellArray[row][column];
    }

    protected int getHeight(){
        return myCellArray.length;
    }

    protected int getWidth(){
        return myCellArray[0].length;
    }


}
